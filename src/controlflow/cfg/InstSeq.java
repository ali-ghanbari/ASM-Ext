package controlflow.cfg;

import java.util.Collection;

import asmext.instruction.Inst;

/**
 * Represents an instruction sequence that makes up a basic block
 * 
 * @author Ali Ghanbari
 *
 */
public class InstSeq implements BasicBlock {
	public final Inst[] instructions;

	public InstSeq(Inst[] insts) {
		instructions = insts;
	}
	
	public Inst first() {
		return instructions[0];
	}
	
	public Inst last() {
		return instructions[instructions.length - 1];
	}
	
	public InstSeq(Collection<Inst> insts) {
		instructions = new Inst[insts.size()];
		Object[] oa = insts.toArray();
		for(int i = 0; i < instructions.length; i++) {
			instructions[i] = (Inst) oa[i];
		}
	}
}
