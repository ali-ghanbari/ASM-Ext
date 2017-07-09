package dataflow;

import java.util.HashSet;

/**
 * An implementation of FiniteSet based on hash sets
 * 
 * @author Ali Ghanbari
 *
 */
public class FiniteHashSet<E> extends HashSet<E> implements FiniteSet<E> {
	private static final long serialVersionUID = 1L;

	public FiniteHashSet() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public SemiLattice join(SemiLattice other) {
		for(Object o : (FiniteHashSet<?>)other) {
			add((E) o);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SemiLattice meet(SemiLattice other) {
		for(Object o : (FiniteHashSet<?>)other) {
			remove((E) o);
		}
		return this;
	}

	@Override
	public boolean lte(SemiLattice o) {
		FiniteHashSet<?> other = (FiniteHashSet<?>) o;
		for(E e : this) {
			if(!other.contains(e)) {
				return false;
			}
		}	
		return true;
	}

}
