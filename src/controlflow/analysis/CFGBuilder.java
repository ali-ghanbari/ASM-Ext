package controlflow.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

import asmext.ASMExt;
import asmext.Method;
import asmext.instruction.AThrow;
import asmext.instruction.Allocation;
import asmext.instruction.ArrayLength;
import asmext.instruction.BinaryArithmatic;
import asmext.instruction.CheckCast;
import asmext.instruction.Control;
import asmext.instruction.Division;
import asmext.instruction.FieldAccess;
import asmext.instruction.GetInstanceField;
import asmext.instruction.Goto;
import asmext.instruction.Inst;
import asmext.instruction.InstanceOf;
import asmext.instruction.Invocation;
import asmext.instruction.InvokeStatic;
import asmext.instruction.JSR;
import asmext.instruction.LoadArraySlot;
import asmext.instruction.LookupSwitch;
import asmext.instruction.MonitorOperation;
import asmext.instruction.NeutralInst;
import asmext.instruction.NewArray;
import asmext.instruction.Remainder;
import asmext.instruction.Return;
import asmext.instruction.SetInstanceField;
import asmext.instruction.SingleTargetConditional;
import asmext.instruction.StoreArraySlot;
import asmext.instruction.TableSwitch;
import asmext.instruction.UntypedConditional;
import controlflow.cfg.CFG;
import controlflow.cfg.InstSeq;
import controlflow.cfg.TryCatchBlock;
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
	
	private static class TCBDesc {
		public int[] exceptings;
				
		public int handler;
		
		public TCBDesc(int[] exceptings, int handler) {
			this.exceptings = exceptings;
			this.handler = handler;
		}
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
		
		leadersPos.add(0); //entry is always a leader
		
		Inst previous = method.body.get(0);
		
		BitVector fallsThrough = new BitVector();
		
		for(int pos = 1; pos < method.body.size(); pos++) {
			Inst inst = method.body.get(pos);
			//boolean flag = false;
			List<TryCatchBlockNode> surroundingTCBs = inst.surroundingTCBs(); 
			if(inst.getLabel() != null //target of a jump
					|| previous instanceof Control //immediately after a jump/invocation/throw
					|| (surroundingTCBs != null 
					&& !surroundingTCBs.isEmpty())) { //protected by some try-catch block
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
		
		//determine if we should add a dummy head node
		List<TCBDesc> tcbDescs = new ArrayList<>();
		boolean createDummyHead = false;
		
		for(TryCatchBlock tcb : method.tcbs) {
			TCBDesc tcbd = locateExceptingAndHandlerNodes(tcb, iSeqs);
			tcbDescs.add(tcbd);
			createDummyHead = createDummyHead || tcbd.exceptings[0] == 0;				
		}
		
		if(createDummyHead) {
			InstSeq dummyHead = new InstSeq(new Inst[] {new NeutralInst()});
			iSeqs.add(0, dummyHead);
			fallsThrough.shr(); //suppose that dummy head does not fall through
			//shifting the position of tcb related nodes to show the effect of addition
			for(TCBDesc tcbd : tcbDescs) {
				for(int i = 0; i < tcbd.exceptings.length; i++) {
					tcbd.exceptings[i]++;
				}
				tcbd.handler++;
			}
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
		
		//connecting predecessors of excepting nodes to the corresponding handlers
		for(TCBDesc tcbd : tcbDescs) {
			for(int excepting : tcbd.exceptings) {
				int[] preds = cfg.preds(excepting);
				if(preds.length == 0) {
					/*make sure that a dummy head node lies there*/
					assert(((InstSeq) cfg.getNodeDescriptor(0)).first() instanceof NeutralInst);
					cfg.connect(0 /*index of dummy head*/, excepting);
					cfg.connect(0 /*index of dummy head*/, tcbd.handler);
				} else {
					for(int pred : preds) {
						cfg.connect(pred, tcbd.handler);
					}
				}
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
	
	/**
	 * Returns node indices corresponding to the excepting nodes inside the zone protected by tcb,
	 * and also the node index corresponding to its end and the beginning of its handler
	 *  
	 * @param tcb the try-catch block
	 * @param iSeqs instruction sequences
	 * @return a pair (z[], h), where z[] contains the indices of the nodes containing an excepting
	 * instructions inside the tcb, and h indicates the index of the first node in its handler 
	 */
	private static TCBDesc locateExceptingAndHandlerNodes(TryCatchBlock tcb, List<InstSeq> iSeqs) {
		int z = -1;
		int e = -1;
		int h = -1;
		
		for(int i = 0; i < iSeqs.size(); i++) {
			InstSeq is = iSeqs.get(i);
			Label label = is.first().getLabel();
			if(tcb.start.equals(label)) {
				z = i;
			}
			if(tcb.end.equals(label)) {
				e = i;
			}
			if(tcb.handler.equals(label)) {
				h = i;
			}
		}
		assert(z >= 0 && e >= 0 && h >= 0);
		int[] exceptings = new int[0];
		while(z < e) {
			InstSeq is = iSeqs.get(z);
			for(Inst inst : is.instructions) {
				List<TryCatchBlockNode> surroundingTCBs = inst.surroundingTCBs();
				assert(surroundingTCBs != null);
				if(surroundingTCBs.stream()
						.anyMatch(tcbn -> {
							String type = "java.lang.Throwable";
							if(tcbn.type != null) {
								type = tcbn.type.replace('/', '.');
							}
							return throwsCompatibleException(inst, type);
						})) {
					int[] exceptings_ext = new int[exceptings.length + 1];
					System.arraycopy(exceptings, 0, exceptings_ext, 0, exceptings.length);
					exceptings_ext[exceptings.length] = z;
					exceptings = exceptings_ext;
				}
			}
			z++;
		}
		return new TCBDesc(exceptings, h);
	}
		
	private static boolean jumpsTo(Inst inst, Label target) {
		List<TryCatchBlockNode> surroundingTCBs = inst.surroundingTCBs();
		if(surroundingTCBs != null) {
			if(surroundingTCBs.stream()
					.map(tcnb -> tcnb.handler.getLabel())
					.anyMatch(l -> target.equals(l))) {
				if(surroundingTCBs.stream()
						.anyMatch(tcbn -> {
							String type = "java.lang.Throwable";
							if(tcbn.type != null) {
								type = tcbn.type.replace('/', '.');
							}
							return throwsCompatibleException(inst, type);
						})) {
					return true;
				}
			}
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
	
	private static boolean safeSubClass(String subName, String supName) {
		Collection<String> subClasses = ASMExt.v().getSubClasses(supName);
		
		return subClasses != null && subClasses.contains(subName);
	}
	
	private static boolean compatible(String typeNameA, String typeNameB) {
		return typeNameA.equals(typeNameB) 
				|| safeSubClass(typeNameA, typeNameB)
				|| safeSubClass(typeNameB, typeNameA);
	}
	
	private static boolean throwsCompatibleException(Inst inst, String throwableTypeName) {
		//trivial; since all exceptions are subclasses of java.lang.Throwable
		if(throwableTypeName.equals("java.lang.Throwable")) {
			return true;
		}
		//asynchronous exceptions
		if(throwableTypeName.equals("java.lang.Error")
				|| throwableTypeName.equals("java.lang.VirtualMachineError")
				|| safeSubClass(throwableTypeName, "java.lang.VirtualMachineError")) {
			return true;
		}
		//John Jorgensen says this one, which is not mentioned in the specification of JVM,
		//is also asynchronous
		if(throwableTypeName.equals("java.lang.ThreadDeath")) {
			return true;
		}
		//These instructions may throw LinkageError or any of its 8 subclasses
		if(inst instanceof FieldAccess 
				|| inst instanceof Invocation 
				|| (inst instanceof Allocation && inst.opcode() != Opcodes.NEWARRAY) 
				|| inst instanceof CheckCast 
				|| inst instanceof InstanceOf) {
			if(throwableTypeName.equals("java.lang.LinkageError")
					|| safeSubClass(throwableTypeName, "java.lang.LinkageError")) {
				return true;
			}
		}
		//trivial; since all the remaining instructions throw subclasses of java.lang.Exception
		//and java.lang.RuntimeException
		if(throwableTypeName.equals("java.lang.Exception")
				|| throwableTypeName.equals("java.lang.RuntimeException")) { 
			return true;
		}
		if(inst instanceof LoadArraySlot || inst instanceof StoreArraySlot) {
			if(inst.opcode() == Opcodes.AASTORE) {
				if(throwableTypeName.equals("java.lang.ArrayStoreException")) {
					return true;
				}
			}
			if(throwableTypeName.equals("java.lang.IndexOutOfBoundsException") 
					|| throwableTypeName.equals("java.lang.NullPointerException")
					|| throwableTypeName.equals("java.lang.ArrayIndexOutOfBoundsException")) {
				return true;
			}
		}
		if(inst instanceof Division || inst instanceof Remainder) {
			BinaryArithmatic ba = (BinaryArithmatic) inst;
			if(ba.type == org.objectweb.asm.Type.INT || ba.type == org.objectweb.asm.Type.LONG) {
				if(throwableTypeName.equals("java.lang.ArithmeticException")) {
					return true;
				}
			}
		}
		if(inst instanceof Return) {
			if(throwableTypeName.equals("java.lang.IllegalMonitorStateException")) {
				return true;
			}
		}
		if(inst instanceof GetInstanceField 
				|| inst instanceof SetInstanceField
				|| (inst instanceof Invocation && !(inst instanceof InvokeStatic))
				|| inst instanceof ArrayLength
				|| inst instanceof AThrow
				|| inst instanceof MonitorOperation) {
			if(throwableTypeName.equals("java.lang.NullPointerException")) {
				return true;
			}
		}
		if(inst instanceof NewArray) {
			if(throwableTypeName.equals("java.lang.NegativeArraySizeException")) {
				return true;
			}
		}
		if(inst instanceof CheckCast) {
			if(throwableTypeName.equals("java.lang.ClassCastException")) {
				return true;
			}
		}
		if(inst instanceof AThrow) {
			return compatible(((AThrow) inst).inferredType.getClassName(), throwableTypeName);
		}
		if(inst instanceof Invocation) {
			List<Method> callees = ASMExt.v().cg.mayCall((Invocation) inst);
			for(Method method : callees) {
				if(method.exceptions != null) {
					for(String exceptionTypeName : method.exceptions) {
						if(compatible(exceptionTypeName, throwableTypeName)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
}