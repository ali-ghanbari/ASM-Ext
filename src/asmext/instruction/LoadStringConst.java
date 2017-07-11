package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class LoadStringConst extends LoadConst {
	public final String value;

	public LoadStringConst(int opcode, String value, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, surroundingTCBs);
		assert(opcode == Opcodes.LDC || opcode == 0x13 /*LDC_W*/);
		this.value = value;
	}
	
	@Override
	public String toString() {
		return MnemonicPrinter.instToString(this) + " " + value;
	}

}
