package util;

import java.io.Serializable;

/**
 * Represents a pair whose components, once assigned (during construction) cannot be replaced
 * @author Ali Ghanbari
 *
 */
public class ImmutablePair<F, S> implements Serializable {
	private static final long serialVersionUID = 1L;
	protected final F fst;
	protected final S snd;

	public ImmutablePair(F first, S second) {
		this.fst = first;
		this.snd = second;
	}

	public F first() {
		return fst;
	}

	public S second() {
		return snd;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof ImmutablePair))
			return false;
		ImmutablePair<?,?> other = (ImmutablePair<?,?>) o;
		return fst.equals(other.fst) && snd.equals(other.snd);
	}
	
	@Override
	public String toString() {
		return "(" + fst + ", " + snd + ")";
	}
}
