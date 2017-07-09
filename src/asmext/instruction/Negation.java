package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Negation extends UnaryArithmatic {

	public Negation(int opcode, List<Label> surroundingHandlers) {
		super(opcode, TypeTable.table[opcode - Opcodes.INEG], surroundingHandlers);
		assert(Opcodes.INEG <= opcode && opcode <= Opcodes.DNEG);
	}

}
