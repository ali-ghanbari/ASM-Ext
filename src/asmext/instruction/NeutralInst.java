package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * This does not correspond to a Java bytecode instruction, rather it represents a "neutral" instruction
 * that has no effect. It is used, only in special occasions, to create dummy head nodes during
 * construction of CFGs.
 * Objects of this class cannot be created through instruction factory.  
 * 
 * @author Ali Ghanbari
 *
 */
public class NeutralInst implements Inst {

	@Override
	public int opcode() {
		return -1;
	}

	@Override
	public Label getLabel() {
		return null;
	}

	@Override
	public void setLabel(Label label) {
		// nothing
	}
	
	@Override
	public String toString() {
		return "NEUTRAL";
	}

	@Override
	public List<TryCatchBlockNode> surroundingTCBs() {
		return null;
	}

}
