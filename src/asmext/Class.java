package asmext;

import java.util.Iterator;

/**
 * Represents a java class
 * 
 * @author Ali Ghanbari
 *
 */
public class Class extends ReferenceType {
	public final String superName;
	
	public Class(String name, String superName, int access) {
		super(name, access);
		assert(superName != null || name.equals("java.lang.Object"));
		this.superName = superName;
	}
	
	@Override
	public Iterator<String> supersIterator() {
		return new Iterator<String>() {
			Iterator<String> it = null;

			@Override
			public boolean hasNext() {
				if(it == null)
					return superName != null;
				return it.hasNext();
			}

			@Override
			public String next() {
				if(it == null) {
					it = superInterfaces.iterator();
					return superName;
				}
				return it.next();
			}
		};
	}
}