package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class MonitorEnter extends MonitorOperation {

	public MonitorEnter(List<TryCatchBlockNode> surroundingTCBs) {
		super(Opcodes.MONITORENTER, surroundingTCBs);
	}

}
