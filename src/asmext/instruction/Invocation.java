package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class Invocation implements CallSite {
	private final int opcode;
	
	public final Type owner;
	
	public final String methName;
	
	public final String desc;
	
	private Label label;
	
	private final List<TryCatchBlockNode> surroundingTCBs;

	protected Invocation(int opcode,
			Type owner,
			String methName,
			String desc,
			List<TryCatchBlockNode> surroundingTCBs) {
		this.opcode = opcode;
		this.owner = owner;
		this.methName = methName;
		this.desc = desc;
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
		return MnemonicPrinter.instToString(this) 
				+ " " + methName + "::" + owner + " " + desc;
	}

}
