package asmext.instruction;

import java.util.List;

import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class MultiTargetConditional extends TypedConditional {

	protected MultiTargetConditional(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, surroundingTCBs);
	}

}
