package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Negation extends UnaryArithmatic {

	public Negation(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, TypeTable.table[opcode - Opcodes.INEG], surroundingTCBs);
		assert(Opcodes.INEG <= opcode && opcode <= Opcodes.DNEG);
	}

}
