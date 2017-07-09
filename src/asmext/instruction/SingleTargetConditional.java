package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class SingleTargetConditional extends TypedConditional {
	public final Label target;

	public SingleTargetConditional(int opcode, Label target, List<Label> surroundingHandlers) {
		super(opcode, surroundingHandlers);
		assert((0x9f /*IF_ICMPEQ*/ <= opcode && opcode <= 0xa6 /*IF_ACMPNE*/)
				|| opcode == Opcodes.IFNULL || opcode == Opcodes.IFNONNULL);
		this.target = target;
	}
	
	@Override
	public String toString() {
		return MnemonicPrinter.instToString(this) + " " + target;
	}
}
