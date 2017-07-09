package controlflow.cfg;

import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

/**
 * A try-catch block; please note that this is not an executable basic block
 * 
 * @author Ali Ghanbari
 *
 */
public class TryCatchBlock implements BasicBlock {
	public Label start;
	
	public Label end;
	
	public Label handler;
	
	public final Type type;

	public TryCatchBlock(Label start, Label end, Label handler, Type type) {
		this.start = start;
		this.end = end;
		this.handler = handler;
		this.type = type;
	}

	@Override
	public String toString() {
		return "TRY-CATCH-BLOCK " + start + " " + end + " " + handler + " " + type; 
	}
}
