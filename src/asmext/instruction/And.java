package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class And extends BitwiseLogical {

	public And(int opcode, List<Label> surroundingHandlers) {
		super(opcode, TypeTable.table[opcode - Opcodes.IAND], surroundingHandlers);
		assert(opcode == Opcodes.IAND || opcode == Opcodes.LAND);
	}

}