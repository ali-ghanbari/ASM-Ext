package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class AThrow implements Unconditional {
	private final List<Label> surroundingHandlers;
	
	private Label label;

	public AThrow(List<Label> surroundingHandlers) {
		this.surroundingHandlers = surroundingHandlers;
		this.label = null;
	}

	@Override
	public int opcode() {
		return Opcodes.ATHROW;
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
		return MnemonicPrinter.instToString(this) + surroundingHandlers;
	}

	@Override
	public List<Label> surroundingHandlers() {
		return surroundingHandlers;
	}

}
