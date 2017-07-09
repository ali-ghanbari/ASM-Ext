package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Conversion implements Inst {
	private final int opcode;
	
	public final int fromType;
	
	public final int toType;
	
	private Label label;
	
	private final List<Label> surroundingHandlers;

	public Conversion(int opcode, List<Label> surroundingHandlers) {
		this.opcode = opcode;
		if(0x85 /*I2L*/ <= opcode && opcode <= 0x90 /*D2F*/) {
			fromType = TypeTable.table[(opcode - 0x85) / 3];
			toType = toType(opcode);
		} else {
			assert(0x91 /*I2B*/ <= opcode && opcode <= 0x93 /*I2S*/);
			fromType = TypeTable.table[0]; //INT
			toType = TypeTable.table[5 + (opcode - 0x91)];
		}
		this.label = null;
		this.surroundingHandlers = surroundingHandlers;
	}
	
	private static int toType(int opcode) {
		switch(opcode) {
		case Opcodes.L2I:
		case Opcodes.F2I:
		case Opcodes.D2I:
			return TypeTable.table[0]; //INT
		case Opcodes.I2L:
		case Opcodes.F2L:
		case Opcodes.D2L:
			return TypeTable.table[1]; //LONG
		case Opcodes.I2F:
		case Opcodes.L2F:
		case Opcodes.D2F:
			return TypeTable.table[2]; //FLOAT
		case Opcodes.I2D:
		case Opcodes.L2D:
		case Opcodes.F2D:
			return TypeTable.table[3]; //DOUBLE
		default:
				return -1;
		}
	}

	@Override
	public int opcode() {
		return opcode;
	}
	
	@Override
	public Label getLabel() {
		return label;
	}

	@Override
	public void setLabel(Label label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return MnemonicPrinter.instToString(this);
	}

	@Override
	public List<Label> surroundingHandlers() {
		return surroundingHandlers;
	}

}
