package asmext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.Frame;

/**
 * Infers type of the object on top of the stack for all instructions of interest in a method body;
 * Until now, we have not used this class
 * 
 * @author Ali Ghanbari
 *
 */
@Deprecated
class TypeInference {
	private static TypeInference instance = null;
	private Map<Integer, Type[]> info;
	private String className;
	private MethodNode methNode;
	private IntPredicate opcodeWant;
	private int maxOperands;
	
	private TypeInference() {
		info = new HashMap<>();
	}
	
	public static TypeInference v() {
		if(instance == null)
			instance = new TypeInference();
		instance.className = null;
		instance.methNode = null;
		instance.opcodeWant = o -> true;
		instance.maxOperands = 1;
		instance.info.clear();
		return instance;
	}
	
	public TypeInference ofClass(ClassNode classNode) {
		className = classNode.name;
		return this;
	}

	public TypeInference forMethod(MethodNode methNode) {
		this.methNode = methNode;
		return this;
	}
		
	public TypeInference wanting(IntPredicate op) {
		opcodeWant = op;
		return this;
	}
	
	public TypeInference maxOperands(int mp) {
		maxOperands = mp;
		return this;
	}
	
	public TypeInference doAnalysis() {
		Analyzer a = new Analyzer(new BasicInterpreter(){
			@Override
			public BasicValue newValue(Type type) {
				if(type != null) {
					switch(type.getSort()) {
					case Type.OBJECT:
					case Type.ARRAY:
						return new BasicValue(type);
					}
				}	
				return super.newValue(type);
			}	        		
    	});
		try {
			a.analyze(className, methNode);
			AbstractInsnNode[] insts = methNode.instructions.toArray();
			Frame[] frames = a.getFrames();
			assert(frames.length == insts.length);
			for(int i = 0; i < frames.length; i++) {
				/*check to see if the instruction is wanted and is reachable*/
				if(opcodeWant.test(insts[i].getOpcode()) && frames[i] != null) {
					final int sz = frames[i].getStackSize();
					int st = 0;
					int asz = sz;
					if(sz > maxOperands) {
						st = sz - maxOperands;
						asz = maxOperands;
					}
					Type[] types = new Type[asz];
					for(int j = st; j < sz; j++) {
						//this might equal the null type object wrapped by
						//BasicValue.REFERENCE_VALUE, i.e. types[j - st] maybe null.
						//it can equal type object corresponding to the
						//descriptor Lnull;. this type descriptor describes type of
						//null objects that are thrown
						types[j - st] = 
								((BasicValue) frames[i].getStack(j)).getType();
					}
					info.put(i, types);
				}
			}
		} catch (AnalyzerException ae) {
			ae.printStackTrace();
		}
		return this;
	}
	
	public Type[] getInfo(int instIndex) {
		return info.get(instIndex);
	}

}
