package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class LoadDoubleConst extends LoadConst {
	public final double value;

	public LoadDoubleConst(int opcode, List<Label> surroundingHandlers) {
		super(opcode, surroundingHandlers);
		assert(opcode == Opcodes.DCONST_0 || opcode == Opcodes.DCONST_1);
		value = opcode - Opcodes.DCONST_0;
	}
	
	public LoadDoubleConst(int opcode, double value, List<Label> surroundingHandlers) {
		super(opcode, surroundingHandlers);
		assert(opcode == Opcodes.LDC || opcode == 0x14 /*LDC2_W*/);
		this.value = value;
	}

}
