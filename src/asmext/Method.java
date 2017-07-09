package asmext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import asmext.instruction.Inst;
import asmext.instruction.Invocation;
import controlflow.cfg.TryCatchBlock;
import util.Utilities;

/**
 * Represents a member method
 * 
 * @author Ali Ghanbari
 *
 */
public class Method {
	public final String name;
	
	private final String declClsName;
	
	private final int access;
	
	public final List<Inst> body;
	
	public final List<TryCatchBlock> tcbs;
	
	private final String subSignature;
	
	private final String fullSignature;
	
	private final String desc;
	
	public Method(String name,
			String declaringClassName,
			int access, 
			String desc,
			String[] exceptions,
			List<Inst> body) {
		this.name = name;
		this.declClsName = declaringClassName;
		this.access = access;
		this.body = body;
		this.desc = desc;
		subSignature = buildSubSignature(name, getType());
		fullSignature = buildFullSignature(name, getType(), exceptions);
		this.tcbs = new ArrayList<>();
	}
	
	public static String buildSubSignature(String methName, Type methType) {
		return methName
				+ "(" 
				+ Utilities.printArray(methType.getArgumentTypes(),
						Type::toString, ",", "")
				+ ")";	
	}
	
	public static String buildFullSignature(String methName,
			Type methType,
			String[] exceptions) {
		return methType.getReturnType() 
				+ " " 
				+ buildSubSignature(methName, methType)
				+ " throws "
				+ Utilities.printArray(exceptions,
						e -> Type.getType(e).toString(), ",", "");
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
	
	public boolean isAbstract() {
		return (access & Opcodes.ACC_ABSTRACT) != 0;
	}
	
	public boolean isNative() {
		return (access & Opcodes.ACC_NATIVE) != 0;
	}
	
	public boolean isSynchronized() {
		return (access & Opcodes.ACC_SYNCHRONIZED) != 0;
	}
	
	public boolean isStatic() {
		return (access & Opcodes.ACC_STATIC) != 0;
	}
	
	public boolean isConstructor() {
		return name.equals("<init>");
	}
	
	public String getFullSignature() {
		return fullSignature;
	}
	
	public String getSubSignature() {
		return subSignature;
	}
	
	public List<Invocation> retrieveCallSites() {
		return body.parallelStream()
				.filter(i -> i instanceof Invocation)
				.map(i -> (Invocation) i)
				.collect(Collectors.toList());
	}
	
//	@Override
//	public boolean equals(Object o) {
//		if(o == null || !(o instanceof Method))
//			return false;
//		Method other = (Method) o;
//		return declClsName.equals(other.declClsName) 
//				&& subSignature.equals(other.subSignature);
//	}
//	
//	@Override
//	public int hashCode() {
//		return (declClsName + "::" + subSignature).hashCode();
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