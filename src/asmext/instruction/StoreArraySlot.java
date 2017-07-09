package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class StoreArraySlot implements Store {
	private final int opcode;
	
	public final int type;
	
	private Label label;
	
	private final List<Label> surroundingHandlers;

	public StoreArraySlot(int opcode, List<Label> surroundingHandlers) {
		this.opcode = opcode;
		assert(0x4f /*IASTORE*/ <= opcode && opcode <= 0x56 /*SASTORE*/);
		type = TypeTable.table[opcode - 0x4f];
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
