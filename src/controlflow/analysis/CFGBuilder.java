package controlflow.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.Label;

import asmext.Method;
import asmext.instruction.Control;
import asmext.instruction.Goto;
import asmext.instruction.Inst;
import asmext.instruction.Invocation;
import asmext.instruction.JSR;
import asmext.instruction.LookupSwitch;
import asmext.instruction.SingleTargetConditional;
import asmext.instruction.TableSwitch;
import asmext.instruction.UntypedConditional;
import controlflow.cfg.CFG;
import controlflow.cfg.InstSeq;
import util.BitVector;

/**
 * Builds a control flow graph based on the method passed to constructor 
 * 
 * @author Ali Ghanbari
 *
 */
public class CFGBuilder {
	private final Method method;
	
	public CFGBuilder(Method method) {
		this.method = method;
	}
	
	/**
	 * constructs the CFG for the method passed to the class
	 * 
	 * @return the constructed CFG
	 */
	public CFG build() {
		List<Integer> heads = new ArrayList<>();
		List<Integer> tails = new ArrayList<>();
		
		if(method.body.isEmpty()) {
			return new CFG(0, heads, tails);
		}
		
		List<Integer> leadersPos = new ArrayList<>();
		
		leadersPos.add(0); //entry
		
		Inst previous = method.body.get(0);
		
		BitVector fallsThrough = new BitVector();
		
		for(int pos = 1; pos < method.body.size(); pos++) {
			Inst inst = method.body.get(pos);
			//boolean flag = false;
			List<Label> surroundingHandlers = inst.surroundingHandlers(); 
			if(inst.getLabel() != null //target of a jump
					|| previous instanceof Control //immediately after a jump/invocation/throw
					|| (surroundingHandlers != null 
					&& !surroundingHandlers.isEmpty())) { //protected by some try-catch block
				leadersPos.add(pos);
				if(!(previous instanceof Control) 
						|| (previous instanceof SingleTargetConditional 
						|| previous instanceof UntypedConditional 
						|| previous instanceof Invocation)) {
					fallsThrough.add(true);
				} else {
					fallsThrough.add(false);
				}
			}
			previous = inst;
		}
		fallsThrough.add(false);
		
		List<InstSeq> iSeqs = new ArrayList<>();
		
		for(int lp : leadersPos) {
			List<Inst> is = new ArrayList<>();
			do {
				is.add(method.body.get(lp++));
			} while(lp < method.body.size() && !leadersPos.contains(lp));
			iSeqs.add(new InstSeq(is));
		}
		
		CFG cfg = new CFG(iSeqs.size(), heads, tails);
		
		for(InstSeq is : iSeqs) {
			cfg.addNode(is);
		}
		
		for(int nodeIndex = 0; nodeIndex < iSeqs.size(); nodeIndex++) {
			Label target = iSeqs.get(nodeIndex).first().getLabel();
			if(target != null) { //the basic block is a possible destination
				for(int src = 0; src < iSeqs.size(); src++) {
					InstSeq js = iSeqs.get(src);
					if(jumpsTo(js.last(), target)) {
						cfg.connect(src, nodeIndex);
					}
				}
			}
			if(fallsThrough.get(nodeIndex)) {
				cfg.connect(nodeIndex, nodeIndex + 1);
			}
		}
		//populating heads and tails
		for(int nodeIndex = 0; nodeIndex < iSeqs.size(); nodeIndex++) {
			if(cfg.succs(nodeIndex).length == 0) {
				tails.add(nodeIndex);
			}
			if(cfg.preds(nodeIndex).length == 0) {
				heads.add(nodeIndex);
			}
		}
		return cfg;
	}
	
	private boolean jumpsTo(Inst inst, Label target) {
		List<Label> surroundingHandlers = inst.surroundingHandlers();
		if(surroundingHandlers != null 
				&& surroundingHandlers.stream().anyMatch(l -> target.equals(l))) {
			return true; //any instruction jumps to any handler protecting it
		}
		if(inst instanceof SingleTargetConditional) {
			return target.equals(((SingleTargetConditional) inst).target);
		}
		if(inst instanceof UntypedConditional) {
			return target.equals(((UntypedConditional) inst).target);
		}
		if(inst instanceof LookupSwitch) {
			boolean flag = ((LookupSwitch) inst).table.stream()
					.map(p -> p.second())
					.anyMatch(lbl -> target.equals(lbl));
			return flag || target.equals(((LookupSwitch) inst).defLabel);
		}
		if(inst instanceof TableSwitch) {
			boolean flag = Arrays.stream(((TableSwitch) inst).labels)
					.anyMatch(lbl -> target.equals(lbl));
			return flag || target.equals(((TableSwitch) inst).defLabel);
		}
		if(inst instanceof Goto) {
			return target.equals(((Goto) inst).target);
		}
		if(inst instanceof JSR) {
			return target.equals(((JSR) inst).target);
		}
		return false;
	}
	
}