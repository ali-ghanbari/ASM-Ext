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
public class LoadDoubleLocal implements LoadLocal {
	private final int opcode;
	
	public final int varIndex;
	
	private Label label;
	
	private final List<TryCatchBlockNode> surroundingTCBs;
	
	public LoadDoubleLocal(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		assert(0x26 /*DLOAD_0*/ <= opcode && opcode <= 0x29 /*DLOAD_3*/);
		this.opcode = opcode;
		this.varIndex = opcode - 0x26;
		this.label = null;
		this.surroundingTCBs = surroundingTCBs;
	}

	public LoadDoubleLocal(int opcode, int varIndex, List<TryCatchBlockNode> surroundingTCBs) {
		assert(opcode == Opcodes.DLOAD);
		this.opcode = opcode;
		this.varIndex = varIndex;
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
		return MnemonicPrinter.instToString(this) + " var #" + varIndex;
	}

	@Override
	public List<TryCatchBlockNode> surroundingTCBs() {
		return surroundingTCBs;
	}

}
