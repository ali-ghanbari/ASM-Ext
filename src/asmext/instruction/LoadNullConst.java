package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class LoadNullConst extends LoadConst {

	public LoadNullConst(List<Label> surroundingHandlers) {
		super(Opcodes.ACONST_NULL, surroundingHandlers);
	}

}
