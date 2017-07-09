package asmext;

import java.util.Iterator;

/**
 * Represents a Java interface
 *  
 * @author Ali Ghanbari
 *
 */
public class Interface extends ReferenceType {
	public Interface(String name, int access) {
		super(name, access);
	}

	@Override
	public Iterator<String> supersIterator() {
		return superInterfaces.iterator();
	}
}