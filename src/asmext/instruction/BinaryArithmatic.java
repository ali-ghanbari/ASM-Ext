package asmext.instruction;

import java.util.List;

import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class BinaryArithmatic extends Arithmatic {
	
	public BinaryArithmatic(int opcode, int type, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, type, surroundingTCBs);
	}

}
