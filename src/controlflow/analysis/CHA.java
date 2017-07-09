package controlflow.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;

import asmext.ASMExt;
import asmext.Method;
import asmext.instruction.Invocation;
import asmext.instruction.InvokeSpecial;
import asmext.instruction.InvokeStatic;
import controlflow.callgraph.CallGraph;

/**
 * Constructs a call graph; the call graph is lazy in that it computes the edges on demand
 * 
 * @author Ali Ghanbari
 *
 */
public class CHA {

	public CHA() {
		
	}
	
	/**
	 * 
	 * @param owner
	 * @param methName
	 * @param desc
	 * @param virtual this parameter indicates that the call target is not a private
	 * or static method.
	 * @return
	 */
	private static List<Method> lookUp(Type owner,
			String methName,
			String desc,
			boolean virtual) {
		List<Method> methods = new ArrayList<>();
		String ownerClassName = "java.lang.Object";
		if(owner.getSort() == Type.OBJECT)
			ownerClassName = owner.getClassName();
		List<Method> candidateMethods = ASMExt.v()
				.getClassByName(ownerClassName)
				.getMethodByName(methName);
		methods.addAll(candidateMethods);
		//method calls on arrays will be directed to "java.lang.Object"
		if(virtual && owner.getSort() != Type.ARRAY) {
			for(String subName : ASMExt.v().getSubClasses(ownerClassName)) {
				candidateMethods = ASMExt.v()
						.getClassByName(subName)
						.getDeclaredMethodByName(methName);
				methods.addAll(candidateMethods);
			}
		}
		Type[] actualTypes = Type.getType(desc).getArgumentTypes();
		Map<String, List<Method>> groups = methods.stream()
			.filter(meth -> {
				Type[] formalTypes = meth.getType().getArgumentTypes();
				if(formalTypes.length == actualTypes.length) {
					for(int i = 0; i < actualTypes.length; i++) {
						if(!asmext.Type.subType(actualTypes[i], formalTypes[i]))
							return false;
					}
					return true;
				}
				return false;
			})
			.collect(Collectors.groupingBy(Method::getDeclaringClassName));
		return groups.entrySet().parallelStream()
				.map(e -> bestMatch(e.getValue()))
				.collect(Collectors.toList());
	}
	
	private static Method bestMatch(List<Method> methods) {
		Iterator<Method> it = methods.iterator();
		Method best = it.next();
		Type[] bestFormalTypes = best.getType().getArgumentTypes();
		while(it.hasNext()) {
			Method meth = it.next();
			Type[] formalTypes = meth.getType().getArgumentTypes();
			for(int i = 0; i < formalTypes.length; i++) {
				if(asmext.Type.subType(formalTypes[i], bestFormalTypes[i])) {
					best = meth;
					bestFormalTypes = formalTypes;
				}
			}
		}
		return best;
	}
	
	public CallGraph doCFA() {
		return new CG();
	}
	
	private static class CG implements CallGraph {
		/**
		 * 
		 * @return duplicates, due to multiple invocation of the same method, is not
		 * removed
		 */
		@Override
		public Collection<Method> mayCall(Method m) {
			List<Method> methods = new ArrayList<>();
			for(Invocation callSite : m.retrieveCallSites()) {
				boolean virtual = true;
				if(callSite instanceof InvokeStatic 
						|| callSite instanceof InvokeSpecial)
					virtual = false;
				methods.addAll(lookUp(callSite.getOwner(),
						callSite.methName, callSite.desc, virtual));
			}
			return methods;
		}

		@Override
		public Collection<Method> mayCall(Invocation callSite) {
			boolean virtual = true;
			if(callSite instanceof InvokeStatic 
					|| callSite instanceof InvokeSpecial)
				virtual = false;
			return lookUp(callSite.getOwner(),
					callSite.methName, callSite.desc, virtual);
		}
	}
}