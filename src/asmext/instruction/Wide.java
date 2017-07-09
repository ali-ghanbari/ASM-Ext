package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Wide implements Inst {
	private Label label;
	
	private final List<Label> surroundingHandlers;

	public Wide(List<Label> surroundingHandlers) {
		this.label = null;
		this.surroundingHandlers = surroundingHandlers;
	}

	@Override
	public int opcode() {
		return 0xc4 /*WIDE*/;
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
