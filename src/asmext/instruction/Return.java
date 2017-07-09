package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Return implements Unconditional {
	private final int opcode;
	
	public final int type;
	
	private Label label;
	
	private final List<Label> surroundingHandlers;

	public Return(int opcode, List<Label> surroundingHandlers) {
		this.opcode = opcode;
		if(0xac /*IRETURN*/ <= opcode && opcode <= 0xb0 /*ARETURN*/) {
			this.type = TypeTable.table[opcode - 0xac];
		} else {
			assert(opcode == Opcodes.RETURN);
			this.type = TypeTable.table[8]; //VOID
		}
		this.label = null;
		this.surroundingHandlers = surroundingHandlers;
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
