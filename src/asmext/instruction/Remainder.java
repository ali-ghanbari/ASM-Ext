package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Remainder extends BinaryArithmatic {

	public Remainder(int opcode, List<Label> surroundingHandlers) {
		super(opcode, TypeTable.table[opcode - Opcodes.IREM], surroundingHandlers);
		assert(Opcodes.IREM <= opcode && opcode <= Opcodes.DREM);
	}

}
