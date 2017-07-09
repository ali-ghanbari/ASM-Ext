package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class MonitorExit extends MonitorOperation {

	public MonitorExit(List<Label> surroundingHandlers) {
		super(Opcodes.MONITOREXIT, surroundingHandlers);
	}

}
