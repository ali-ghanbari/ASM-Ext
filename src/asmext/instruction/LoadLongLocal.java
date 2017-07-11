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
public class LoadLongLocal implements LoadLocal {
	private final int opcode;
	
	public final int varIndex;
	
	private Label label;
	
	private final List<TryCatchBlockNode> surroundingTCBs;
	
	public LoadLongLocal(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		assert(0x1e /*LLOAD_0*/ <= opcode && opcode <= 0x21 /*LLOAD_3*/);
		this.opcode = opcode;
		this.varIndex = opcode - 0x1e;
		this.label = null;
		this.surroundingTCBs = surroundingTCBs;
	}

	public LoadLongLocal(int opcode, int varIndex, List<TryCatchBlockNode> surroundingTCBs) {
		assert(opcode == Opcodes.LLOAD);
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
