package dataflow;

/**
 * A three variable function
 * 
 * @author Ali Ghanbari
 *
 */
@FunctionalInterface
public interface ThreeVarFunction<P1, P2, P3, R> {
	public R apply(P1 param1, P2 param2, P3 param3);
}
