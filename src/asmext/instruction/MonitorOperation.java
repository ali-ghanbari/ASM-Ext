package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class MonitorOperation implements Inst {
	private final int opcode;
	
	private Label label;
	
	private final List<Label> surroundingHandlers;

	protected MonitorOperation(int opcode, List<Label> surroundingHandlers) {
		this.opcode = opcode;
		assert(opcode == Opcodes.MONITORENTER || opcode == Opcodes.MONITOREXIT);
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
	public List<Label> surroundingHandlers() {
		return surroundingHandlers;
	}
	
	@Override
	public String toString() {
		return MnemonicPrinter.instToString(this);
	}

}
