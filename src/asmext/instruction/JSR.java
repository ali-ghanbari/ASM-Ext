package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class JSR implements CallSite {
	private final int opcode;
	
	public final Label target;
	
	private Label label;
	
	private final List<TryCatchBlockNode> surroundingTCBs;

	public JSR(int opcode, Label target, List<TryCatchBlockNode> surroundingTCBs) {
		assert(opcode == Opcodes.JSR || opcode == 0xc9 /*JSR_W*/);
		this.opcode = opcode;
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
