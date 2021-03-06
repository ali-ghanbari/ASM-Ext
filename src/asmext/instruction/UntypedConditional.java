package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * These are called untyped for they do conditional jump based on a previous comparison.
 * The comparison might be performed on two long, float, or double values.
 * This might even be the result of an INSTANCEOF instruction.
 * At the time of control transfer we are not aware of the type of values that
 * have been compared. 
 *   
 * @author Ali Ghanbari
 *
 */
public class UntypedConditional implements Conditional {
	private final int opcode;
	
	public final Label target;
	
	private Label label;
	
	private final List<TryCatchBlockNode> surroundingTCBs;

	public UntypedConditional(int opcode, Label target, List<TryCatchBlockNode> surroundingTCBs) {
		this.opcode = opcode;
		assert(0x99 /*IFEQ*/ <= opcode && opcode <= 0x9e /*IFLE*/);
		this.target = target;
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
		return MnemonicPrinter.instToString(this) + " " + target;
	}

	@Override
	public List<TryCatchBlockNode> surroundingTCBs() {
		return surroundingTCBs;
	}

}
