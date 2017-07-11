package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Or extends BitwiseLogical {

	public Or(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, TypeTable.table[opcode - Opcodes.IOR], surroundingTCBs);
		assert(opcode == Opcodes.IOR || opcode == Opcodes.LOR);
	}

}
