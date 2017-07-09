package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Multiplication extends BinaryArithmatic {

	public Multiplication(int opcode, List<Label> surroundingHandlers) {
		super(opcode, TypeTable.table[opcode - Opcodes.IMUL], surroundingHandlers);
		assert(Opcodes.IMUL <= opcode && opcode <= Opcodes.DMUL);
	}

}
