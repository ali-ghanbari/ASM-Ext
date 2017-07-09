package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Or extends BitwiseLogical {

	public Or(int opcode, List<Label> surroundingHandlers) {
		super(opcode, TypeTable.table[opcode - Opcodes.IOR], surroundingHandlers);
		assert(opcode == Opcodes.IOR || opcode == Opcodes.LOR);
	}

}
