package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class StoreArraySlot implements Store {
	private final int opcode;
	
	public final int type;
	
	private Label label;
	
	private final List<TryCatchBlockNode> surroundingTCBs;

	public StoreArraySlot(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		this.opcode = opcode;
		assert(0x4f /*IASTORE*/ <= opcode && opcode <= 0x56 /*SASTORE*/);
		type = TypeTable.table[opcode - 0x4f];
		this.label = null;
		this.surroundingTCBs = surroundingTCBs;
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
	public List<TryCatchBlockNode> surroundingTCBs() {
		return surroundingTCBs;
	}

}
