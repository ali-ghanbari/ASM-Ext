package asmext.instruction;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class LoadTypeConst extends LoadConst {

	public LoadTypeConst(int opcode, Type type, List<TryCatchBlockNode> surroundingTCBs) {
		super(opcode, surroundingTCBs);
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
