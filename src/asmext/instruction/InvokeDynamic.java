package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class InvokeDynamic extends Invocation {
	public InvokeDynamic(String methName, String desc, List<Label> surroundingHandlers) {
		super(Opcodes.INVOKEDYNAMIC, null, methName, desc, surroundingHandlers);
	}
}
