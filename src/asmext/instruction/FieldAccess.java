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
public abstract class FieldAccess implements Inst {
	private final int opcode;
	
	private final Type owner;
	
	public final String fieldDescriptor;
	
	public final String name;
	
	private Label label;
	
	private final List<TryCatchBlockNode> surroundingTCBs;

	protected FieldAccess(int opcode,
			Type owner,
			String name,
			String desc,
			List<TryCatchBlockNode> surroundingTCBs) {
		this.opcode = opcode;
		this.owner = owner;
		this.name = name;
		this.fieldDescriptor = desc;
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
	
	public Type getOwner() {
		return owner;
	}
	
	public Type getFieldType() {
		return Type.getType(fieldDescriptor);
	}
	
	@Override
	public List<TryCatchBlockNode> surroundingTCBs() {
		return surroundingTCBs;
	}
	
	@Override
	public String toString() {
		return MnemonicPrinter.instToString(this) 
				+ " " + name + "::" + owner + " " + getFieldType();
	}

}
