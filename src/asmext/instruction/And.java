package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class And extends BitwiseLogical {

	public And(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, TypeTable.table[opcode - Opcodes.IAND], surroundingTCBs);
		assert(opcode == Opcodes.IAND || opcode == Opcodes.LAND);
	}

}
