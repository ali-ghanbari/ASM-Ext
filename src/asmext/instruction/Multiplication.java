package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Multiplication extends BinaryArithmatic {

	public Multiplication(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, TypeTable.table[opcode - Opcodes.IMUL], surroundingTCBs);
		assert(Opcodes.IMUL <= opcode && opcode <= Opcodes.DMUL);
	}

}
