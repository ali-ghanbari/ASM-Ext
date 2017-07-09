package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class StoreRefLocal implements StoreLocal {
	private final int opcode;
	
	public final int varIndex;
	
	private Label label;
	
	private final List<Label> surroundingHandlers;
	
	public StoreRefLocal(int opcode, List<Label> surroundingHandlers) {
		assert(0x4b /*ASTORE_0*/ <= opcode && opcode <= 0x4e /*ASTORE_3*/);
		this.opcode = opcode;
		this.varIndex = opcode - 0x4b;
		this.label = null;
		this.surroundingHandlers = surroundingHandlers;
	}

	public StoreRefLocal(int opcode, int varIndex, List<Label> surroundingHandlers) {
		assert(opcode == Opcodes.ASTORE);
		this.opcode = opcode;
		this.varIndex = varIndex;
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
		return MnemonicPrinter.instToString(this) + " var #" + varIndex;
	}

	@Override
	public List<Label> surroundingHandlers() {
		return surroundingHandlers;
	}

}
