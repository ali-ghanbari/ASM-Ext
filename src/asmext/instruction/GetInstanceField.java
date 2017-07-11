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
public class GetInstanceField extends FieldAccess {

	public GetInstanceField(Type owner, String name, String desc, List<TryCatchBlockNode> surroundingTCBs) {
		super(Opcodes.GETFIELD, owner, name, desc, surroundingTCBs);
	}

}
