package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Remainder extends BinaryArithmatic {

	public Remainder(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, TypeTable.table[opcode - Opcodes.IREM], surroundingTCBs);
		assert(Opcodes.IREM <= opcode && opcode <= Opcodes.DREM);
	}

}
