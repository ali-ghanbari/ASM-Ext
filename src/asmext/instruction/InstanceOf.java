package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class InstanceOf implements Inst {
	public final Type type; 
	
	private Label label;
	
	private final List<Label> surroundingHandlers;

	public InstanceOf(Type type, List<Label> surroundingHandlers) {
		this.type = type;
		this.label = null;
		this.surroundingHandlers = surroundingHandlers;
	}

	@Override
	public int opcode() {
		return Opcodes.INSTANCEOF;
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
		return MnemonicPrinter.instToString(this) + " " + type;
	}

	@Override
	public List<Label> surroundingHandlers() {
		return surroundingHandlers;
	}

}
