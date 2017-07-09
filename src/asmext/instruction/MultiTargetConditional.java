package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class MultiTargetConditional extends TypedConditional {

	protected MultiTargetConditional(int opcode, List<Label> surroundingHandlers) {
		super(opcode, surroundingHandlers);
	}

}
