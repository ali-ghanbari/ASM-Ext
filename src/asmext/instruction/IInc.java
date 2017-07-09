package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class IInc implements LoadLocal, StoreLocal {
	public final int varIndex;
	
	public final int value;
	
	private Label label;
	
	private final List<Label> surroundingHandlers;
	
	public IInc(int varIndex, int value, List<Label> surroundingHandlers) {
		this.varIndex = varIndex;
		this.value = value;
		this.label = null;
		this.surroundingHandlers = surroundingHandlers;
	}
	
	@Override
	public int opcode() {
		return Opcodes.IINC;
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
		return MnemonicPrinter.instToString(this) 
				+ " var #" + varIndex + " by " + value;
	}

	@Override
	public List<Label> surroundingHandlers() {
		return surroundingHandlers;
	}

}
