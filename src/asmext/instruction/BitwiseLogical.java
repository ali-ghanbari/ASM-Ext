package asmext.instruction;

import java.util.List;

import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class BitwiseLogical extends BitOperation {

	public BitwiseLogical(int opcode, int type, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, type, surroundingTCBs);
	}

}
