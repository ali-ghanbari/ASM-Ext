package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class ShiftRight extends BitOperation {
	public static enum Kind {SIGNED, UNSIGNED};
	
	public final Kind kind;

	public ShiftRight(int opcode, List<Label> surroundingHandlers) {
		super(opcode, type(opcode), surroundingHandlers);
		this.kind = kind(opcode);
		assert(this.kind != null);
	}
	
	private static int type(int opcode) {
		switch(opcode) {
		case Opcodes.ISHR:
		case Opcodes.LSHR:
			return TypeTable.table[opcode - Opcodes.ISHR];
		case Opcodes.IUSHR:
		case Opcodes.LUSHR:
			return TypeTable.table[opcode - Opcodes.IUSHR];
		default:
			return -1;
		}
	}
	
	private static Kind kind(int opcode) {
		switch(opcode) {
		case Opcodes.ISHR:
		case Opcodes.LSHR:
			return Kind.SIGNED;
		case Opcodes.IUSHR:
		case Opcodes.LUSHR:
			return Kind.UNSIGNED;
		default:
			return null;
		}
	}

}
