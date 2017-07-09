package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class Invocation implements CallSite {
	private final int opcode;
	
	private final Type owner;
	
	public final String methName;
	
	public final String desc;
	
	private Label label;
	
	private final List<Label> surroundingHandlers;

	protected Invocation(int opcode,
			Type owner,
			String methName,
			String desc,
			List<Label> surroundingHandlers) {
		this.opcode = opcode;
		this.owner = owner;
		this.methName = methName;
		this.desc = desc;
		this.label = null;
		this.surroundingHandlers = surroundingHandlers;
	}

	public Type getOwner() {
		return owner;
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
		return MnemonicPrinter.instToString(this) 
				+ " " + methName + "::" + owner + " " + desc + " " + surroundingHandlers;
	}

}
