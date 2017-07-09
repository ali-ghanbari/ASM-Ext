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
public class InvokeInterface extends Invocation {
	public InvokeInterface(Type owner,
			String methName,
			String desc,
			List<Label> surroundingHandlers) {
		super(Opcodes.INVOKEINTERFACE, owner, methName, desc, surroundingHandlers);
	}
}
