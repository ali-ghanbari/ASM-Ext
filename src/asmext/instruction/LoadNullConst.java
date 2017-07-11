package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class LoadNullConst extends LoadConst {

	public LoadNullConst(List<TryCatchBlockNode> surroundingTCBs) {
		super(Opcodes.ACONST_NULL, surroundingTCBs);
	}

}
