package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class LoadFloatConst extends LoadConst {
	public final float value;

	public LoadFloatConst(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, surroundingTCBs);
		assert(Opcodes.FCONST_0 <= opcode && opcode <= Opcodes.FCONST_2);
		value = opcode - Opcodes.FCONST_0;
	}
	
	public LoadFloatConst(int opcode, float value, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, surroundingTCBs);
		assert(opcode == Opcodes.LDC || opcode == 0x13 /*LDC_W*/);
		this.value = value;
	}

}
