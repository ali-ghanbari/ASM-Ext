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
public class GetStaticField extends FieldAccess {

	public GetStaticField(Type owner, String name, String desc, List<TryCatchBlockNode> surroundingTCBs) {
		super(Opcodes.GETSTATIC, owner, name, desc, surroundingTCBs);
	}

}
