package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;

/**
 * An instruction
 * 
 * @author Ali Ghanbari
 *
 */
public interface Inst {	
	public int opcode();
	
	public Label getLabel();
	
	public void setLabel(Label label);
	
	public List<Label> surroundingHandlers();
}
