package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class Allocation implements Inst {
	private final int opcode;
	
	public final Type type;
	
	private Label label;
	
	private final List<TryCatchBlockNode> surroundingTCBs;

	protected Allocation(int opcode, Type type, List<TryCatchBlockNode> surroundingTCBs) {
		this.opcode = opcode;
		this.type = type;
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
	public List<TryCatchBlockNode> surroundingTCBs() {
		return surroundingTCBs;
	}
	
	@Override
	public String toString() {
		return MnemonicPrinter.instToString(this) + " " + type;
	}
	
}
