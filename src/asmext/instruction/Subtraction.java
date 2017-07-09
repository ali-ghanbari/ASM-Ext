package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Subtraction extends BinaryArithmatic {

	public Subtraction(int opcode, List<Label> surroundingHandlers) {
		super(opcode, TypeTable.table[opcode - Opcodes.ISUB], surroundingHandlers);
		assert(Opcodes.ISUB <= opcode && opcode <= Opcodes.DSUB);
	}

}
