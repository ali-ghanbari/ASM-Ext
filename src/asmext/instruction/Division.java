package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Division extends BinaryArithmatic {
	
	public Division(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, TypeTable.table[opcode - Opcodes.IDIV], surroundingTCBs);
		assert(Opcodes.IDIV <= opcode && opcode <= Opcodes.DDIV);
	}

}
