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
public class InvokeVirtual extends Invocation {
	public InvokeVirtual(Type owner,
			String methName,
			String desc,
			List<Label> surroundingHandlers) {
		super(Opcodes.INVOKEVIRTUAL, owner, methName, desc, surroundingHandlers);
	}
}
