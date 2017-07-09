package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class MonitorEnter extends MonitorOperation {

	public MonitorEnter(List<Label> surroundingHandlers) {
		super(Opcodes.MONITORENTER, surroundingHandlers);
	}

}
