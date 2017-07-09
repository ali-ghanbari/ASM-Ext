package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Addition extends BinaryArithmatic {
	public Addition(int opcode, List<Label> surroundingHandlers) {
		super(opcode, TypeTable.table[opcode - Opcodes.IADD], surroundingHandlers);
		assert(Opcodes.IADD <= opcode && opcode <= Opcodes.DADD);
	}
}
