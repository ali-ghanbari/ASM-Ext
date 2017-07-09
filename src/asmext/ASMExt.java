package asmext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.objectweb.asm.ClassReader;

/**
 * Main class of ASM Ext
 * 
 * @author Ali Ghanbari
 *
 */
public class ASMExt {
	private static ASMExt instance = null;
	
	public static class Pair {
		public ReferenceType typeObject;
		public Set<String> subclassNames;
		
		public Pair(ReferenceType typeObject, Set<String> subclassNames) {
			this.typeObject = typeObject;
			int i = 0;
			this.subclassNames = subclassNames;
		}
	}
	
	private List<String> appClassFileNames;
	
	private Map<String, Pair> allClasses;
	
	private List<String> appClassNames;
	
	private ASMExt() {
		allClasses = new HashMap<>();
		appClassFileNames = obtainClassFileName(Config.classPath);
		int stepForward = Config.classPath.endsWith(File.separator) ? 0 : 1;
		int start = Config.classPath.length() + stepForward;
		int backup = ".class".length();
		appClassNames = appClassFileNames.stream()
				.map(cfn -> cfn.substring(start, cfn.length() - backup).replace(File.separatorChar, '.'))
				.collect(Collectors.toList());
	}
	
	public static ASMExt v() {
		if(instance == null)
			instance = new ASMExt();
		return instance;
	}
	
	public boolean subClass(String sub, String sup) {
		return allClasses.get(sup).subclassNames.contains(sub);
	}
	
	public boolean isApplicationClass(String typeName) {
		return appClassNames.parallelStream().anyMatch(tn -> tn.equals(typeName));
	}
	
	public boolean isApplicationClass(Type type) {
		return isApplicationClass(type.name);
	}
	
	public ReferenceType getClassByName(String typeName) {
		return allClasses.get(typeName).typeObject;
	}
	
	public Set<String> getClasses() {
		return allClasses.keySet();
	}
	
	public Set<String> getSubClasses(String supName) {
		return allClasses.get(supName).subclassNames;
	}
	
	private Set<String> visited;
	
	private void loadAllClasses(String mainClassName) {
		visited = new HashSet<>();
		try {
			_loadAllClasses(mainClassName);
			/*computing immediate subclasses*/
			Set<String> keySet = allClasses.keySet();
			for(String supName : keySet) {
				allClasses.get(supName).subclassNames = keySet.parallelStream()
						.filter(t -> {
							Iterator<String> it = allClasses.get(t)
									.typeObject
									.supersIterator();
							while(it.hasNext()) {
								if(it.next().equals(supName)) {
									return true;
								}
							}
							return false;
						})
						.collect(Collectors.toSet());
			}
			/*computing transitive closure*/
			transitiveClosure();
			/*propagating public/protected fields and methods to subclasses*/
			for(String supName : allClasses.keySet()) {
				ReferenceType sup = allClasses.get(supName).typeObject;
				List<Field> candidateFields = sup.fields.stream()
						.filter(f -> !f.isPrivate())
						.collect(Collectors.toList());
				List<Method> candidateMethods = sup.methods.stream()
						.filter(m -> !m.isPrivate() && !m.isConstructor())
						.collect(Collectors.toList());
				allClasses.get(supName).subclassNames
				.parallelStream()
				.forEach(subName -> {
					//making sure that subclasses are computed correctly
					ReferenceType sub = allClasses.get(subName).typeObject;
					List<Field> inheritableFields = candidateFields.stream()
							.filter(cf -> !sub.fields.stream()
									.anyMatch(df -> df.name.equals(cf.name)))
							.collect(Collectors.toList());
					sub.fields.addAll(inheritableFields);
					List<Method> inheritableMethods = candidateMethods.stream()
							.filter(cm -> !sub.methods.stream()
									.anyMatch(dm -> overrides(dm, cm)))
							.collect(Collectors.toList());
					sub.methods.addAll(inheritableMethods);
				});	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean overrides(Method subMeth, Method supMeth) {
		return subMeth.getSubSignature().equals(supMeth.getSubSignature());
	}
	
	private void _loadAllClasses(String className) throws IOException {
		if(Config.verbose) {
			System.out.print(String.format("loading class %-50.50s%s\t",
					className,
					className.length() > 50 ? "..." : ""));
		}
		visited.add(className);
		Set<String> referencedTypeNames = new HashSet<>();
		FileInputStream fis = null;
		ClassReader cr;
		try {
			if(isApplicationClass(className)) {
				fis = new FileInputStream(pathFor(className));
				cr = new ClassReader(fis);
			} else {
				cr = new ClassReader(className);
			}
		} catch (IOException e) {
			if(!e.getMessage().contains("Class not found")) {
				throw e;
			} else {
				System.out.println("Class not found: " + className);
				System.exit(0);
				return;
			}
		}
		ClassInfoLoader cil = new ClassInfoLoader(referencedTypeNames);
		cr.accept(cil, ClassReader.SKIP_DEBUG);
		allClasses.put(className, new Pair(cil.theClass(), new HashSet<>()));
		if(fis != null) {
			fis.close();
		}
		if(Config.verbose) {
			System.out.println("[OK]");
		}
		for(String tn : referencedTypeNames) {
			if(!visited.contains(tn)) {
				_loadAllClasses(tn);
			}
		}
	}
	
	private void transitiveClosure() {
		class Flag {
			boolean changed;
		}
		Flag flag = new Flag();
		do {
			flag.changed = false;
			for(String supName : allClasses.keySet()) {
				Set<String> immSubs = allClasses.get(supName).subclassNames;
				Set<String> newSubs = new HashSet<>();
				for(String subName : immSubs) {
					newSubs.addAll(allClasses.get(subName).subclassNames);
				}
				int oldSize = immSubs.size();
				immSubs.addAll(newSubs);
				int newSize = immSubs.size();
				flag.changed = flag.changed || (newSize != oldSize);
			}
		} while(flag.changed);
	}
	
	private String pathFor(String appClassName) {
		int index = appClassNames.indexOf(appClassName);
		return appClassFileNames.get(index);
	}
	
	private static List<String> obtainClassFileName(String path) {
		List<String> lstFileNames = new ArrayList<>();
		for(File file : (new File(path)).listFiles()) {
			if(file.isFile() && file.getName().endsWith(".class"))
				lstFileNames.add(file.getPath());
			else if(file.isDirectory())
				lstFileNames.addAll(obtainClassFileName(file.getPath()));
		}
		return lstFileNames;
	}
	
	public static void main(String[] args) {
		if(args.length < 1) {
			throw new RuntimeException("At least the name of the main class is expected.");
		}
		ASMExt.v().loadAllClasses(args[0]);
	}
}