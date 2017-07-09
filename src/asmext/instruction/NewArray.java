package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class NewArray extends Allocation {
	public final int dimentions;
	
	public NewArray(int opcode, Type type, int dimentions, List<Label> surroundingHandlers) {
		super(opcode, type, surroundingHandlers);
		assert(opcode == Opcodes.MULTIANEWARRAY 
				|| opcode == Opcodes.NEWARRAY 
				|| opcode == Opcodes.ANEWARRAY);
		assert(dimentions >= 1);
		this.dimentions = dimentions;
	}
	
	@Override
	public String toString() {
		return MnemonicPrinter.instToString(this) + " " + dimentions;
	}

}
