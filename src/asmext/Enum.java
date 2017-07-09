package asmext;

import java.util.Iterator;

/**
 * Represents a Java enum
 * 
 * @author Ali Ghanbari
 *
 */
public class Enum extends ReferenceType {
	public Enum(String name, int access) {
		super(name, access);
	}

	@Override
	public Iterator<String> supersIterator() {
		return new Iterator<String>() {
			Iterator <String> it = null;
			
			@Override
			public boolean hasNext() {
				if(it == null)
					return true;
				return it.hasNext();
			}

			@Override
			public String next() {
				if(it == null) {
					it = superInterfaces.iterator();
					return "java.lang.Enum";
				}
				return it.next();
			}
		};
	}
}