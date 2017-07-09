package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class LoadFloatConst extends LoadConst {
	public final float value;

	public LoadFloatConst(int opcode, List<Label> surroundingHandlers) {
		super(opcode, surroundingHandlers);
		assert(Opcodes.FCONST_0 <= opcode && opcode <= Opcodes.FCONST_2);
		value = opcode - Opcodes.FCONST_0;
	}
	
	public LoadFloatConst(int opcode, float value, List<Label> surroundingHandlers) {
		super(opcode, surroundingHandlers);
		assert(opcode == Opcodes.LDC || opcode == 0x13 /*LDC_W*/);
		this.value = value;
	}

}
