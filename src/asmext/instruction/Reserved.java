package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class Reserved implements Inst {
	private final int opcode;
	
	private Label label;
	
	private final List<Label> surroundingHandlers;

	public Reserved(int opcode, List<Label> surroundingHandlers) {
		assert(opcode == 0xca /*BREAKPOINT*/ 
				|| opcode == 0xfe /*IMPDEP1*/ 
				|| opcode == 0xff /*IMPDEP2*/);
		this.opcode = opcode;
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
