package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class LoadStringConst extends LoadConst {
	public final String value;

	public LoadStringConst(int opcode, String value, List<Label> surroundingHandlers) {
		super(opcode, surroundingHandlers);
		assert(opcode == Opcodes.LDC || opcode == 0x13 /*LDC_W*/);
		this.value = value;
	}
	
	@Override
	public String toString() {
		return MnemonicPrinter.instToString(this) + " " + value;
	}

}
