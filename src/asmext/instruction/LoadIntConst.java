package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class LoadIntConst extends LoadConst {
	public final int value;

	public LoadIntConst(int opcode, List<Label> surroundingHandlers) {
		super(opcode, surroundingHandlers);
		assert(Opcodes.ICONST_M1 <= opcode && opcode <= Opcodes.ICONST_5);
		/*the field value will be -1 in case that opcode equals ICONST_M1*/
		value = opcode - Opcodes.ICONST_0;
	}
	
	public LoadIntConst(int opcode, byte value, List<Label> surroundingHandlers) {
		super(opcode, surroundingHandlers);
		assert(opcode == Opcodes.BIPUSH);
		this.value = value;
	}
	
	public LoadIntConst(int opcode, short value, List<Label> surroundingHandlers) {
		super(opcode, surroundingHandlers);
		assert(opcode == Opcodes.SIPUSH);
		this.value = value;
	}
	
	public LoadIntConst(int opcode, int value, List<Label> surroundingHandlers) {
		super(opcode, surroundingHandlers);
		assert(opcode == Opcodes.LDC || opcode == 0x13 /*LDC_W*/);
		this.value = value;
	}
}
