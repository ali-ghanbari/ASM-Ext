package dataflow;

import java.util.List;

/**
 * Fixed point algorithm
 * 
 * @author Ali Ghanbari
 *
 */
public abstract class FixedPointAlgorithm<L extends SemiLattice> {
	protected final MonotoneFramework<L> instance;
	
	public FixedPointAlgorithm(MonotoneFramework<L> instance) {
		this.instance = instance;
	}
	
	public class MFP {
		protected List<L> entryValues;
		
		protected MFP(List<L> entryValues) {
			this.entryValues = entryValues;
		}
		
		public L entryValue(int nodeIndex) {
			return entryValues.get(nodeIndex);
		}
		
		public L exitValue(int nodeIndex) {
			return instance.transferFunction.apply(FixedPointAlgorithm.this.instance.cfg,
							nodeIndex,
							entryValue(nodeIndex));
		}
	}
	
	public abstract MFP perform();
}
