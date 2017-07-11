package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Xor extends BitwiseLogical {

	public Xor(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, TypeTable.table[opcode - Opcodes.IXOR], surroundingTCBs);
		assert(opcode == Opcodes.IXOR || opcode == Opcodes.LXOR);
	}

}
