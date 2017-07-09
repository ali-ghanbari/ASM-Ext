package asmext.instruction;

import org.objectweb.asm.Type;

/**
 * 
 * @author Ali Ghanbari
 *
 */
class TypeTable {

	static final int[] table = new int[] {
			Type.INT,
			Type.LONG,
			Type.FLOAT,
			Type.DOUBLE,
			Type.OBJECT,
			Type.BYTE, //Byte or Boolean
			Type.CHAR,
			Type.SHORT,
			Type.VOID
	};
}
