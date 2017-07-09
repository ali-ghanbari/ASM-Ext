package asmext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.Opcodes;

/**
 * Represents a reference type (non-primitive type)
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class ReferenceType extends Type {
	protected final int access;
	
	protected final List<String> superInterfaces;
	
	//either declared or inherited
	public final List<Method> methods;
	
	public final List<Field> fields;
	
	protected ReferenceType(String name, int access) {
		super(name);
		this.access = access;
		this.methods = new ArrayList<>();
		this.fields = new ArrayList<>();
		this.superInterfaces = new ArrayList<>();
	}
	
	public abstract Iterator<String> supersIterator();
	
	public boolean isAbstract() {
		return (access & Opcodes.ACC_ABSTRACT) != 0;
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
	
	public List<Method> getMethodByName(String name) {
		List<Method> meth = methods.stream()
				.filter(m -> m.name.equals(name))
				.collect(Collectors.toList());
		return meth;
	}
	
	public List<Method> getDeclaredMethodByName(String name) {
		List<Method> meth = methods.stream()
				.filter(m -> (m.name.equals(name) 
						&& m.getDeclaringClassName().equals(this.name)))
				.collect(Collectors.toList());
		return meth;
	}
}