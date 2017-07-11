package asmext.instruction;

import java.util.List;

import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * These are called typed for they compare one or more object of certain type.
 * After comparison, based on the result they conduct control transfer.
 * These include instructions IF_ICMPEQ, ..., IF_ACMPNE, IFNULL, IFNONNULL,
 * TABLESWITCH, and LOOKUPSWITCH.
 * The last two instructions are called multi-target conditionals.
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class TypedConditional extends Comparison implements Conditional {

	protected TypedConditional(int opcode, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, surroundingTCBs, 0 /*dummy*/);
	}
}
