package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Comparison implements Inst {
	private final int opcode;
	
	public final int type;
	
	private Label label;
	
	private final List<Label> surroundingHandlers;
	
	protected Comparison(int opcode, List<Label> surroundingHandlers, int __) {
		this.opcode = opcode;
		/*typed conditional instructions*/
		/*we call these typed as they compare one or more object of certain type*/
		/*(0x9f IF_ICMPEQ <= opcode && opcode <= 0xa6 IF_ACMPNE)
				|| opcode == Opcodes.IFNULL || opcode == Opcodes.IFNONNULL
				|| opcode == Opcodes.TABLESWITCH || opcode == Opcodes.LOOKUPSWITCH)*/ 
		this.type = type(opcode);
		this.label = null;
		this.surroundingHandlers = surroundingHandlers;
	}

	public Comparison(int opcode, List<Label> surroundingHandlers) {
		assert(0x94 /*LCMP*/ <= opcode && opcode <= 0x98 /*DCMPG*/);
		this.opcode = opcode;
		this.type = type(opcode);
		this.label = null;
		this.surroundingHandlers = surroundingHandlers;
	}
	
	private static int type(int opcode) {
		switch(opcode) {
		case Opcodes.TABLESWITCH:
		case Opcodes.LOOKUPSWITCH:
		case Opcodes.IF_ICMPEQ:
		case Opcodes.IF_ICMPNE:
		case Opcodes.IF_ICMPLT:
		case Opcodes.IF_ICMPGE:
		case Opcodes.IF_ICMPGT:
		case Opcodes.IF_ICMPLE:
			return TypeTable.table[0]; //INT
		case Opcodes.LCMP:
			return TypeTable.table[1]; //LONG
		case Opcodes.FCMPL:
		case Opcodes.FCMPG:
			return TypeTable.table[2]; //FLOAT
		case Opcodes.DCMPL:
		case Opcodes.DCMPG:
			return TypeTable.table[3]; //DOUBLE
		case Opcodes.IF_ACMPEQ:
		case Opcodes.IF_ACMPNE:
		case Opcodes.IFNULL:
		case Opcodes.IFNONNULL:
			return TypeTable.table[4]; //OBJECT
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
