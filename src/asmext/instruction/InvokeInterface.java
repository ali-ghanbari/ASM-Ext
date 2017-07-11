package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class InvokeInterface extends Invocation {
	public InvokeInterface(Type owner,
			String methName,
			String desc,
			List<TryCatchBlockNode> surroundingTCBs) {
		super(Opcodes.INVOKEINTERFACE, owner, methName, desc, surroundingTCBs);
	}
}
