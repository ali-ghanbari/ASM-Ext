package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Subtraction extends BinaryArithmatic {

	public Subtraction(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, TypeTable.table[opcode - Opcodes.ISUB], surroundingTCBs);
		assert(Opcodes.ISUB <= opcode && opcode <= Opcodes.DSUB);
	}

}
