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
public class New extends Allocation {

	public New(Type type, List<TryCatchBlockNode> surroundingTCBs) {
		super(Opcodes.NEW, type, surroundingTCBs);
	}

}
