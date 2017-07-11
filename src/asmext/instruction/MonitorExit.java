package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class MonitorExit extends MonitorOperation {

	public MonitorExit(List<TryCatchBlockNode> surroundingTCBs) {
		super(Opcodes.MONITOREXIT, surroundingTCBs);
	}

}
