package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class AThrow implements Unconditional {
	private final List<TryCatchBlockNode> surroundingTCBs;
	
	private Label label;
	
	public final Type inferredType;

	public AThrow(List<TryCatchBlockNode> surroundingTCBs, Type inferredType) {
		this.surroundingTCBs = surroundingTCBs;
		this.label = null;
		this.inferredType = inferredType;
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
		return MnemonicPrinter.instToString(this);
	}

	@Override
	public List<TryCatchBlockNode> surroundingTCBs() {
		return surroundingTCBs;
	}

}
