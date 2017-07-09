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
public class GetInstanceField extends FieldAccess {

	public GetInstanceField(Type owner, String name, String desc, List<Label> surroundingHandlers) {
		super(Opcodes.GETFIELD, owner, name, desc, surroundingHandlers);
	}

}
