package asmext;

/**
 * Represents a type; either primitive or reference type
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class Type {
	public final String name;
	
	protected Type(String name) {
		this.name = name;
	}
	
	public static final String getClassName(org.objectweb.asm.Type type) {
		while(type.getSort() == org.objectweb.asm.Type.ARRAY) {
			type = type.getElementType(); 
		}
		if(type.getSort() == org.objectweb.asm.Type.OBJECT) {
			return type.getClassName();
		}
		return type.toString();
	}
	
	public static final boolean subType(org.objectweb.asm.Type sub,
			org.objectweb.asm.Type sup) {
		if(!sup.equals(sub)) {
			String subName = getClassName(sub);
			String supName = getClassName(sup);
			if(PrimitiveType.primitive(subName) 
					&& PrimitiveType.primitive(supName)) {
				return PrimitiveType.subType(subName, supName);
			}
			if (!PrimitiveType.primitive(subName) 
					&& !PrimitiveType.primitive(supName)) {
				return ASMExt.v().subClass(subName, supName);
			}
			return false;
		}
		return true;
	}
	
	public static boolean subType(String subName, String supName) {
		if(!subName.equals(supName)) {
			if(PrimitiveType.primitive(subName) 
					&& PrimitiveType.primitive(supName)) {
				return PrimitiveType.subType(subName, supName);
			}
			if (!PrimitiveType.primitive(subName) 
					&& !PrimitiveType.primitive(supName)) {
				return ASMExt.v().subClass(subName, supName);
			}
			return false;
		}
		return true;
	}
	
//	@Override
//	public boolean equals(Object o) {
//		if(o == null || !(o instanceof Type))
//			return false;
//		Type other = (Type) o;
//		return name.equals(other.name);
//	}
//	
//	@Override
//	public int hashCode() {
//		return name.hashCode();
//	}
}
