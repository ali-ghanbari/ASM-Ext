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
public abstract class MonitorOperation implements Inst {
	private final int opcode;
	
	private Label label;
	
	private final List<TryCatchBlockNode> surroundingTCBs;

	protected MonitorOperation(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		this.opcode = opcode;
		assert(opcode == Opcodes.MONITORENTER || opcode == Opcodes.MONITOREXIT);
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
		return MnemonicPrinter.instToString(this);
	}

}
