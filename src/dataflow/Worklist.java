package dataflow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An implementation of general worklist algorithm
 * 
 * @author Ali Ghanbari
 *
 */
public class Worklist<L extends SemiLattice> extends FixedPointAlgorithm<L> {

	public Worklist(MonotoneFramework<L> instance) {
		super(instance);
	}
	
	private static class ImmutableIntPair {
		public final int fst;
		
		public final int snd;
		
		public ImmutableIntPair(int fst, int snd) {
			this.fst = fst;
			this.snd = snd;
		}
		
	}

	@Override
	public MFP perform() {
		//initialization
		List<L> analysis = new ArrayList<>();
		List<ImmutableIntPair> W = new ArrayList<>();
		for(int i = 0; i < instance.cfg.size(); i++) {
			for(int dep : instance.dep.apply(instance.cfg, i)) {
				W.add(new ImmutableIntPair(i, dep));
			}
			analysis.add(instance.initializer.apply(instance.cfg, i));
		}
		//iteration
		Iterator<ImmutableIntPair> wit = W.iterator();
		while(wit.hasNext()) {
			ImmutableIntPair edge = wit.next();
			wit.remove();
			L y = instance.transferFunction.apply(instance.cfg,
					edge.fst,
					analysis.get(edge.fst));
			L succVal = analysis.get(edge.snd); 
			if(!instance.lte.test(y, succVal)) {
				analysis.set(edge.snd, instance.join.apply(succVal, y));
				for(int dep : instance.dep.apply(instance.cfg, edge.snd)) {
					W.add(new ImmutableIntPair(edge.snd, dep));
				}
				wit = W.iterator();
			}
		}
		return new MFP(analysis);
	}

}
