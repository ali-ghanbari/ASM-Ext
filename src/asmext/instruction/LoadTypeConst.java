package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class LoadTypeConst extends LoadConst {

	public LoadTypeConst(int opcode, Type type, List<Label> surroundingHandlers) {
		super(opcode, surroundingHandlers);
		assert(opcode == Opcodes.LDC || opcode == 0x13 /*LDC_W*/);
		switch(type.getSort()) {
		case Type.OBJECT:
			//
			break;
		case Type.ARRAY:
			//
			break;
		case Type.METHOD:
			//
			break;
		default:
			throw new Error();
		}
	}

}
