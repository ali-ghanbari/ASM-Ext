package asmext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a primitive type
 * 
 * @author Ali Ghanbari
 *
 */
public class PrimitiveType extends Type {
	public PrimitiveType(String name) {
		super(name);
	}
	
	static private class Pair {
		final String typeName;
		final Set<String> subNames;
		
		Pair(String tn, Set<String> sn) {
			this.typeName = tn;
			this.subNames = sn;
		}
	}
	
	private static final Pair[] primitives = new Pair[] {
		new Pair("D", new HashSet<>(Arrays.asList("F", "J", "I", "C", "S", "B"))),
		new Pair("F", new HashSet<>(Arrays.asList("J", "I", "C", "S", "B"))),
		new Pair("J", new HashSet<>(Arrays.asList("I", "C", "S", "B"))),
		new Pair("I", new HashSet<>(Arrays.asList("C", "S", "B"))),
		new Pair("S", new HashSet<>(Arrays.asList("B"))),
		new Pair("B", new HashSet<>()),
		new Pair("C", new HashSet<>())
	};
		
	public static boolean subType(String subName, String supName) {
		for(Pair p : primitives) {
			if(p.typeName.equals(supName)) {
				return p.subNames.contains(subName);
			}
		}
		return false;
	}
		
	public static final boolean primitive(String typeName) {
		return typeName.matches("[VZCBSIFJD]");
	}
}
