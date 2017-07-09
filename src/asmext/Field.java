package asmext;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Represents a class field
 * 
 * @author Ali Ghanbari
 *
 */
public class Field {
	public final String name;
	
	public final Object value;
	
	private final String declClsName;
	
	private final int access;
	
	private final String desc;
	
	public Field(String name,
			String desc,
			int access,
			String declaringClassName,
			Object value) {
		this.name = name;
		this.declClsName = declaringClassName;
		this.desc = desc;
		this.access = access;
		this.value = value;
	}
	
	public boolean isPublic() {
		return (access & Opcodes.ACC_PUBLIC) != 0;
	}
	
	public boolean isProtected() {
		return (access & Opcodes.ACC_PROTECTED) != 0;
	}
	
	public boolean isPrivate() {
		return (access & Opcodes.ACC_PRIVATE) != 0;
	}
	
	public boolean isFinal() {
		return (access & Opcodes.ACC_FINAL) != 0;
	}
	
	public boolean isStatic() {
		return (access & Opcodes.ACC_STATIC) != 0;
	}
	
//	@Override
//	public boolean equals(Object o) {
//		if(o == null || !(o instanceof Field))
//			return false;
//		Field other = (Field) o;
//		return declClsName.equals(other.declClsName) 
//				&& name.equals(other.name);
//	}
//	
//	@Override
//	public int hashCode() {
//		return (declClsName + "::" + name).hashCode();
//	}
	
	public String getDeclaringClassName() {
		return declClsName;
	}
	
	public ReferenceType getDeclaringClass() {
		return ASMExt.v().getClassByName(declClsName);
	}
	
	public Type getType() {
		return Type.getType(desc);
	}
}
