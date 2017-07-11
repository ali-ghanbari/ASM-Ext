package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class LoadDoubleConst extends LoadConst {
	public final double value;

	public LoadDoubleConst(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, surroundingTCBs);
		assert(opcode == Opcodes.DCONST_0 || opcode == Opcodes.DCONST_1);
		value = opcode - Opcodes.DCONST_0;
	}
	
	public LoadDoubleConst(int opcode, double value, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, surroundingTCBs);
		assert(opcode == Opcodes.LDC || opcode == 0x14 /*LDC2_W*/);
		this.value = value;
	}

}
