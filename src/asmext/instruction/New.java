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
public class New extends Allocation {

	public New(Type type, List<Label> surroundingHandlers) {
		super(Opcodes.NEW, type, surroundingHandlers);
	}

}
