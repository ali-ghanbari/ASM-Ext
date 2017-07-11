package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class BitOperation implements Inst {
	private final int opcode;
	
	public final int type;
	
	private Label label;
	
	private final List<TryCatchBlockNode> surroundingTCBs;
	
	public BitOperation(int opcode, int type, List<TryCatchBlockNode> surroundingTCBs) {
		this.opcode = opcode;
		this.type = type;
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
