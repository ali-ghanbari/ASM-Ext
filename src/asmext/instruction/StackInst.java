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
public class StackInst implements Inst {
	private final int opcode;
	
	private Label label;
	
	private final List<TryCatchBlockNode> surroundingTCBs;

	public StackInst(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		this.opcode = opcode;
		assert(Opcodes.POP <= opcode && opcode <= Opcodes.SWAP);
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
