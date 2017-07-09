package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class LoadLongConst extends LoadConst {
	public final long value;

	public LoadLongConst(int opcode, List<Label> surroundingHandlers) {
		super(opcode, surroundingHandlers);
		assert(opcode == Opcodes.LCONST_0 || opcode == Opcodes.LCONST_1);
		value = opcode - Opcodes.LCONST_0;
	}

	public LoadLongConst(int opcode, long value, List<Label> surroundingHandlers) {
		super(opcode, surroundingHandlers);
		assert(opcode == Opcodes.LDC || opcode == 0x14 /*LDC2_W*/);
		this.value = value;
	}
}
