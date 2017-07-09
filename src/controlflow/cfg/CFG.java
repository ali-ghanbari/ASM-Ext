package controlflow.cfg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import util.BiDiGraph_Int;
import util.ImmutablePair;
import util.Utilities;

/**
 * Control Flow Graph as a subclass of directed graph
 * 
 * @author Ali Ghanbari
 *
 */
public class CFG extends BiDiGraph_Int implements Iterable<BasicBlock> {
	private final BasicBlock[] desc;
	
	private final List<Integer> headsPos;
	
	private final List<Integer> tailsPos;
	
	private int index;

	/**
	 * 
	 * @param size
	 * @param headsPos for this and the following parameter, you don't need to specify, at the time of,
	 * creation which nodes are heads and which nodes are tails; you can just pass references that can
	 * later be populated appropriately.
	 * @param headsPos
	 */
	public CFG(int size, List<Integer> headsPos, List<Integer> tailsPos) {
		super(size);
		desc = new BasicBlock[size];
		Arrays.fill(desc, null);
		index = 0;
		this.headsPos = headsPos;
		this.tailsPos = tailsPos;
	}
	
	public BasicBlock getNodeDescriptor(int index) {
		return desc[index];
	}
	
	public int addNode(BasicBlock bb) {
		desc[index] = bb;
		return index++;
	}
		
	public List<Integer> heads() {
		return headsPos;
	}
	
	public boolean isHead(int index) {
		return headsPos.contains(index);
	}
	
	public List<Integer> tails() {
		return tailsPos;
	}
	
	public boolean isTail(int index) {
		return tailsPos.contains(index);
	}
	
	@Override
	public void connect(int srcIndex, int dstIndex) {
		assert(desc[srcIndex] != null && desc[dstIndex] != null);
		super.connect(srcIndex, dstIndex);
	}

	public void dot() {
		System.out.println("digraph CFG {");
		int sg = 0;
		Map<Integer, ImmutablePair<String, String>> imap = new HashMap<>();
		for(int i = 0; i < desc.length; i++) {
			BasicBlock bb = desc[i];
			if(bb != null) {
				if(bb instanceof TryCatchBlock) {
					String label = "\"" + bb.toString() + "\"";
					String name = "TCB" + i;
					System.out.println("\t" + name + "[label=" + label + ",shape=rectangle];");
					System.out.println("\t" + name + ";");
				} else {
					InstSeq is = (InstSeq) bb;
					System.out.println("\tsubgraph cluster_CFG" + sg + " {");
					System.out.println("\t\tnode[style=filled,color=white];");
					System.out.println("\t\tstyle=filled;");
					System.out.println("\t\tcolor=lightgrey;");
					List<String> lst = new ArrayList<>();
					String first = null;
					for(int j = 0; j < is.instructions.length; j++) {
						String label = "\"" + is.instructions[j]
										.toString()
										.replace("\"", "\\\"") + "\"";
						String name = "NODE" + sg + "" + j; 
						lst.add(name);
						if(j == 0)
							first = name;
						if(j == is.instructions.length - 1) {
							imap.put(i, new ImmutablePair<>(first, name));
						}
						System.out.println("\t\t" + name + "[label=" + label 
								+ ",shape=rectangle];");
					}
					System.out.println();
					System.out.println("\t\t" + Utilities.printCollection(lst,
							Function.identity(),
							"->",
							"null") + ";");
					System.out.println("\t}");
					sg++;
				}
			}
		}
		for(int i = 0; i < desc.length; i++) {
			BasicBlock bb = desc[i];
			if(bb instanceof InstSeq) {
				String src = imap.get(i).second();
				int[] tmp = succs(i);
				if(tmp != null)
				for(int j : /*graph.*//*succs(i)*/tmp) {
					String dst = imap.get(j).first();
					System.out.println(src + "->" + dst + ";");
				}
			}
		}
		System.out.println("}");
	}

	@Override
	public int[] succs(int node) {
		assert(desc[node] != null);
		return super.succs(node);
	}

	@Override
	public int[] preds(int node) {
		assert(desc[node] != null);
		return super.preds(node);
	}

	@Override
	public boolean connected(int node0, int node1) {
		assert(desc[node0] != null && desc[node1] != null);
		return super.connected(node0, node1);
	}

	@Override
	public Iterator<BasicBlock> iterator() {
		return new Iterator<BasicBlock>() {
			private int cursor = 0;
			
			@Override
			public boolean hasNext() {
				return cursor < desc.length;
			}

			@Override
			public BasicBlock next() {
				return desc[cursor++];
			}
		};
	}
}
