package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class ShiftLeft extends BitOperation {

	public ShiftLeft(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, TypeTable.table[opcode - Opcodes.ISHL], surroundingTCBs);
		assert(opcode == Opcodes.ISHL  || opcode == Opcodes.LSHL);
	}

}
