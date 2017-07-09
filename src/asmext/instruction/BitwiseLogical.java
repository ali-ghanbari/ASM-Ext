package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class BitwiseLogical extends BitOperation {

	public BitwiseLogical(int opcode, int type, List<Label> surroundingHandlers) {
		super(opcode, type, surroundingHandlers);
	}

}
