package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class ShiftLeft extends BitOperation {

	public ShiftLeft(int opcode, List<Label> surroundingHandlers) {
		super(opcode, TypeTable.table[opcode - Opcodes.ISHL], surroundingHandlers);
		assert(opcode == Opcodes.ISHL  || opcode == Opcodes.LSHL);
	}

}
