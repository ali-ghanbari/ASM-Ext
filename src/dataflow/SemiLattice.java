package dataflow;

/**
 * Represents a semi-lattice
 * 
 * @author Ali Ghanbari
 *
 */
public interface SemiLattice {
	public SemiLattice join(SemiLattice other);
	
	public SemiLattice meet(SemiLattice other);
	
	public boolean lte(SemiLattice other);
	
	public default boolean gte(SemiLattice other) {
		return other.lte(this);
	}
}
