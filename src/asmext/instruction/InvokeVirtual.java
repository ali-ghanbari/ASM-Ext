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
public class InvokeVirtual extends Invocation {
	public InvokeVirtual(Type owner,
			String methName,
			String desc,
			List<TryCatchBlockNode> surroundingTCBs) {
		super(Opcodes.INVOKEVIRTUAL, owner, methName, desc, surroundingTCBs);
	}
}
