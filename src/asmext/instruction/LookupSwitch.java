package asmext.instruction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.TryCatchBlockNode;

import util.ImmutablePair;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class LookupSwitch extends MultiTargetConditional {
	public final Label defLabel;
	
	public final List<ImmutablePair<Integer, Label>> table;
	
	public LookupSwitch(Label defl, int[] keys, Label[] labels, List<TryCatchBlockNode> surroundingTCBs) {
		super(Opcodes.LOOKUPSWITCH, surroundingTCBs);
		this.defLabel = defl;
		this.table = new ArrayList<>();
		assert(defl != null && keys.length == labels.length);
		for(int i = 0; i < keys.length; i++) {
			table.add(new ImmutablePair<>(keys[i], labels[i]));
		}
	}
	
	@Override
	public int opcode() {
		return Opcodes.LOOKUPSWITCH;
	}
	
	@Override
	public String toString() {
		String args = table.stream()
				.map(ImmutablePair::toString)
				.collect(Collectors.joining(" "));
		args += " def: " + defLabel;
		return MnemonicPrinter.instToString(this) + " " + args;
	}

}
