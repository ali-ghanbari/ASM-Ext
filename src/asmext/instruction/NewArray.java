package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class NewArray extends Allocation {
	public final int dimentions;
	
	public NewArray(int opcode, Type type, int dimentions, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, type, surroundingTCBs);
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
