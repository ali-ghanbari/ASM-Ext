package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Division extends BinaryArithmatic {
	
	public Division(int opcode, List<Label> surroundingHandlers) {
		super(opcode, TypeTable.table[opcode - Opcodes.IDIV], surroundingHandlers);
		assert(Opcodes.IDIV <= opcode && opcode <= Opcodes.DDIV);
	}

}
