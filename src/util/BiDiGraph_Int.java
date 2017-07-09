package util;

import java.util.Arrays;

/**
 * An efficient implementation for digraphs based on integers
 * Based on the size of the graph, the class decides to either save space and use bytes or
 * simply use integers
 *  
 * @author Ali Ghanbari
 *
 */
public class BiDiGraph_Int {
	private static final int[] EMPTY_IA = new int[0];
	
	private static final byte[] EMPTY_BA = new byte[0];
	
	private static final int PRED = 0;
	
	private static final int SUCC = 1;
	
	private static final int BYTE_MASK = 0xff;
	
	private Object[] graph;
	
	private boolean large;

	public BiDiGraph_Int(int size) {
		graph = new Object[size];
		Arrays.fill(graph, null);
		large = size > 256;
	}
	
	private Object allocSlot() {
		return large ? new int[][] {EMPTY_IA, EMPTY_IA} :
			new byte[][] {EMPTY_BA, EMPTY_BA};
	}
	
	private static int[] toIntArray(byte[] ba) {
		int[] ia = new int[ba.length];
		for(int i = 0; i < ba.length; i++) {
			ia[i] = ((int) ba[i]) & BYTE_MASK;
		}
		return ia;
	}
	
	public int[] succs(int node) {
		assert(0 <= node && node < graph.length);
		return get(node, SUCC);
	}
	
	private int[] get(int node, int dir) {
		if(graph[node] == null)
			return null;
		if(large)
			return ((int[][]) graph[node])[dir];
		return toIntArray(((byte[][]) graph[node])[dir]);
	}
	
	public int[] preds(int node) {
		assert(0 <= node && node < graph.length);
		return get(node, PRED);
	}
	
	public int size() {
		return graph.length;
	}
	
	public void connect(int src, int dst) {
		assert(0 <= src && src < graph.length);
		assert(0 <= dst && dst < graph.length);
		if(graph[src] == null)
			graph[src] = allocSlot();
		if(graph[dst] == null)
			graph[dst] = allocSlot();
		add(src, SUCC, dst);
		add(dst, PRED, src);
	}
	
	private void add(int node, int dir, int value) {
		if(large) {
			_add(node, dir, value);
		} else {
			_add(node, dir, (byte) value);
		}
	}
	
	private void _add(int node, int dir, int value) {
		int[][] slot = (int[][]) graph[node];
		int[] new_array = new int[slot[dir].length + 1];
		System.arraycopy(slot[dir], 0, new_array, 0, slot[dir].length);
		new_array[slot[dir].length] = value;
		slot[dir] = new_array;
	}
	
	private void _add(int node, int dir, byte value) {
		byte[][] slot = (byte[][]) graph[node];
		byte[] new_array = new byte[slot[dir].length + 1];
		System.arraycopy(slot[dir], 0, new_array, 0, slot[dir].length);
		new_array[slot[dir].length] = value;
		slot[dir] = new_array;
	}
	
	public boolean connected(int node0, int node1) {
		assert(0 <= node0 && node0 < graph.length);
		assert(0 <= node1 && node1 < graph.length);
		assert(graph[node0] != null);
		assert(graph[node1] != null);
		if(large) {
			int[] succs = ((int[][]) graph[node0])[SUCC];
			for(int s : succs) {
				if(s == node1) {
					return true;
				}
			}
		} else {
			byte[] succs = ((byte[][]) graph[node0])[SUCC];
			for(int s : succs) {
				if((s & BYTE_MASK) == node1) {
					return true;
				}
			}
		}
		return false;
	}
}
