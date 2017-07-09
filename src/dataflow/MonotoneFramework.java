package dataflow;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import controlflow.cfg.CFG;

/**
 * Monotone framework is a 6-place tuple
 * 
 * @author Ali Ghanbari
 *
 */
public class MonotoneFramework<L extends SemiLattice> {
	public final CFG cfg;
	
	public final BiFunction<CFG, Integer, int[]> dep;
	
	public final BiFunction<CFG, Integer, L> initializer;
	
	public final BiPredicate<L, L> lte;
	
	public final BiFunction<L, L, L> join;
	
	public final ThreeVarFunction<CFG, Integer, L, L> transferFunction;
	
	public MonotoneFramework(CFG cfg,
		BiFunction<CFG, Integer, int[]> dep,
		BiFunction<CFG, Integer, L> initializer,
		BiPredicate<L, L> lte,
		BiFunction<L, L, L> join,
		ThreeVarFunction<CFG, Integer, L, L> transferFunction) {
		this.cfg = cfg;
		this.dep = dep;
		this.initializer = initializer;
		this.lte = lte;
		this.join = join;	
		this.transferFunction = transferFunction;
	}
	
	public FixedPointAlgorithm<L>.MFP doAnalysis(FixedPointAlgorithm<L> strategy) {
		return strategy.perform();
	}
}