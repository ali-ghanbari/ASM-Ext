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
public class SetStaticField extends FieldAccess {

	public SetStaticField(Type owner, String name, String desc, List<TryCatchBlockNode> surroundingTCBs) {
		super(Opcodes.PUTSTATIC, owner, name, desc, surroundingTCBs);
	}

}
