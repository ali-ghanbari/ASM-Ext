package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Addition extends BinaryArithmatic {
	public Addition(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, TypeTable.table[opcode - Opcodes.IADD], surroundingTCBs);
		assert(Opcodes.IADD <= opcode && opcode <= Opcodes.DADD);
	}
}
