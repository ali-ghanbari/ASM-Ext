package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class InvokeDynamic extends Invocation {
	public InvokeDynamic(String methName, String desc, List<TryCatchBlockNode> surroundingTCBs) {
		super(Opcodes.INVOKEDYNAMIC, null, methName, desc, surroundingTCBs);
	}
}
