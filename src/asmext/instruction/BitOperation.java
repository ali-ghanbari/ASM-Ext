package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class BitOperation implements Inst {
	private final int opcode;
	
	public final int type;
	
	private Label label;
	
	private final List<Label> surroundingHandlers;
	
	public BitOperation(int opcode, int type, List<Label> surroundingHandlers) {
		this.opcode = opcode;
		this.type = type;
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