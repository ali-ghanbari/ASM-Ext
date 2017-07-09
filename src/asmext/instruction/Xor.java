package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Xor extends BitwiseLogical {

	public Xor(int opcode, List<Label> surroundingHandlers) {
		super(opcode, TypeTable.table[opcode - Opcodes.IXOR], surroundingHandlers);
		assert(opcode == Opcodes.IXOR || opcode == Opcodes.LXOR);
	}

}
