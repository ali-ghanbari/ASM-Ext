package asmext.instruction;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

/**
 * 
 * @author Ali Ghanbari
 *
 */
public class TableSwitch extends MultiTargetConditional {
	public final int min;
	
	public final int max;
	
	public final Label defLabel;
	
	public final Label[] labels;

	public TableSwitch(int min, int max, Label defl, Label[] labels, List<Label> surroundingHandlers) {
		super(Opcodes.TABLESWITCH, surroundingHandlers);
		this.min = min;
		this.max = max;
		this.defLabel = defl;
		this.labels = labels;
		assert(defl != null && labels.length == (max - min + 1));
	}

	@Override
	public int opcode() {
		return Opcodes.TABLESWITCH;
	}
	
	@Override
	public String toString() {
		String args = "[" + min + ", " + max + "]";
		args += " " + Arrays.stream(labels)
				.map(Label::toString)
				.collect(Collectors.joining(" "));
		args += " def: " + defLabel;
		return MnemonicPrinter.instToString(this) + " " + args;
	}

}
