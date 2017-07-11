package asmext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import asmext.instruction.Inst;
import asmext.instruction.factory.InstFactory;
import controlflow.cfg.TryCatchBlock;

/**
 * Loads information about each class, and also computed all referenced type names inside the class
 * 
 * @author Ali Ghanbari
 *
 */
class ClassInfoLoader extends ClassNode {
	private final Set<String> referencedTypeNames;
	
	private ReferenceType theClass;
	
	private List<TryCatchBlockNode> activeTCBs;
	
	private TypeInference typeInfo;
	
	public ClassInfoLoader(Set<String> referencedTypeNames) {
		super(Opcodes.ASM5);
		this.referencedTypeNames = referencedTypeNames;
	}
	
	public ReferenceType theClass() {
		String _name = name.replace('/', '.');
		if((access & Opcodes.ACC_INTERFACE) != 0)
			theClass = new Interface(_name, access);
		else if((access & Opcodes.ACC_ENUM) != 0)
			theClass = new Enum(_name, access);
		else {
			String _superName = 
					superName != null ? superName.replace('/', '.') : null;
			if(_superName != null)
				referencedTypeNames.add(_superName);
			theClass = new Class(_name, _superName, access);
		}
		for(Object o : interfaces) {
			String si = (String) o;
			si = si.replace('/', '.');
			referencedTypeNames.add(si);
			theClass.superInterfaces.add(si);
		}
		visitFields();
		visitMethods();
		return theClass;
	}
	
	private void visitFields() {
		for(Object o : fields) {
			FieldNode fieldNode = (FieldNode) o;
			addReferencedType(org.objectweb.asm.Type.getType(fieldNode.desc));
			Field field = new Field(fieldNode.name,
					fieldNode.desc,
					fieldNode.access,
					theClass.name,
					fieldNode.value);
			theClass.fields.add(field);
		}		
	}
		
	private void addReferencedType(org.objectweb.asm.Type type) {
		while(type.getSort() == Type.ARRAY) {
			type = type.getElementType(); 
		}
		if(type.getSort() == Type.OBJECT) {
			referencedTypeNames.add(type.getClassName().replace('/', '.'));
		}
		if(type.getSort() == Type.METHOD) {
			assert(!type.getDescriptor().startsWith("L"));
			referencedTypeNames.add(type.getDescriptor().replace('/', '.'));
		}
	}

	private void visitMethods() {
		for(Object o : methods) {
			MethodNode methNode = (MethodNode) o;
			if((methNode.access & Opcodes.ACC_BRIDGE) == 0) {
				org.objectweb.asm.Type methType = 
						org.objectweb.asm.Type.getType(methNode.desc);
				addReferencedType(methType.getReturnType());
				for(org.objectweb.asm.Type paramType : methType.getArgumentTypes()) {
					addReferencedType(paramType);
				}
				String[] exceptions = null;
				if(methNode.exceptions != null) {
					exceptions = new String[methNode.exceptions.size()];
					Iterator<?> ei = methNode.exceptions.iterator();
					int i = 0;
					while(ei.hasNext()) {
						exceptions[i] = ((String) ei.next()).replace('/', '.');
						referencedTypeNames.add(exceptions[i]);
						i++;
					}
				}
				Method method = new Method(methNode.name,
						theClass.name,
						methNode.access,
						methNode.desc,
						exceptions,
						new ArrayList<>()); //NOTE : HERE WE COULD PASS methNode.instructions
				theClass.methods.add(method);
				//adjusting labels
				adjustLabels(methNode.instructions, methNode.tryCatchBlocks);
				//visit the modified body and try-catch blocks
				visitMethodBody(methNode, method.body);
				for(Object obj : methNode.tryCatchBlocks) {
					TryCatchBlockNode tcbn = (TryCatchBlockNode) obj;
					org.objectweb.asm.Type type = null;
					//if type = null, the block can catch any exception
					if(tcbn.type != null) {
						type = org.objectweb.asm.Type.getType(tcbn.type);
						addReferencedType(type);
					}
					method.tcbs.add(new TryCatchBlock(tcbn.start.getLabel(),
							tcbn.end.getLabel(),
							tcbn.handler.getLabel(),
							type));
				}
			}
		}
	}
	
	/**
	 * in the case of two consecutive labels,
	 * replaces the first label with the second one,
	 * and updates the body, as well as try-catch blocks, appropriately
	 * 
	 * @param body
	 * @param tryCatchBlocks
	 */
	private void adjustLabels(InsnList body, List<?> tryCatchBlocks) {
		ListIterator<?> lit = body.iterator();
		while(lit.hasNext()) {
			AbstractInsnNode first = (AbstractInsnNode) lit.next();
			if(first instanceof LabelNode) {
				if(lit.hasNext()) {
					AbstractInsnNode second = (AbstractInsnNode) lit.next();
					if(second instanceof LabelNode) {
						replace(body,
								tryCatchBlocks,
								(LabelNode) first,
								(LabelNode) second);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void replace(InsnList body,
			List<?> tryCatchBlocks,
			LabelNode first,
			LabelNode second) {
		ListIterator<?> lit = body.iterator();
		while(lit.hasNext()) {
			AbstractInsnNode ain = (AbstractInsnNode) lit.next();
			if(ain instanceof JumpInsnNode) {
				JumpInsnNode jin = (JumpInsnNode) ain;
				if(jin.label.equals(first)) {
					jin.label = second;
				}
			}
			if(ain instanceof LookupSwitchInsnNode) {
				LookupSwitchInsnNode lsin = (LookupSwitchInsnNode) ain;
				lsin.labels = (List<?>) lsin.labels.stream()
						.map(o -> o.equals(first) ? second : o)
						.collect(Collectors.toList());
				if(lsin.dflt.equals(first)) {
					lsin.dflt = second;
				}
			}
			if(ain instanceof TableSwitchInsnNode) {
				TableSwitchInsnNode tsin = (TableSwitchInsnNode) ain;
				tsin.labels = (List<?>) tsin.labels.stream()
						.map(o -> o.equals(first) ? second : o)
						.collect(Collectors.toList());
				if(tsin.dflt.equals(first)) {
					tsin.dflt = second;
				}
			}
		}
		tryCatchBlocks.stream()
				.map(o -> (TryCatchBlockNode) o)
				.forEach(tcbn -> {
					if(tcbn.start.equals(first)) {
						tcbn.start = second;
					}
					if(tcbn.end.equals(first)) {
						tcbn.end = second;
					}
					if(tcbn.handler.equals(first)) {
						tcbn.handler = second;
					}
				});
	}
	
	private InstFactory visitFieldInst(FieldInsnNode fin,
			int instIndex,
			InstFactory factory) {
		org.objectweb.asm.Type ownerType = 
				org.objectweb.asm.Type.getType(fin.owner);
		addReferencedType(ownerType);
		return factory.forTypeDescriptor(fin.desc)
				.forName(fin.name)
				.forOwner(ownerType);
	}
	
	private InstFactory visitIIncInst(IincInsnNode iin,
			int instIndex,
			InstFactory factory) {
		return factory.forVarIndex(iin.var).forConstValue(iin.incr);
	}
	
	private InstFactory visitInst(InsnNode in,
			int instIndex,
			InstFactory factory) {
		if(in.getOpcode() == Opcodes.ATHROW) {
			org.objectweb.asm.Type inferredType = typeInfo.getTopOfStack(instIndex);
			if(inferredType == null)
				inferredType = Type.getType("java.lang.Throwable");
			factory = factory.forInferredType(inferredType);
		}
		return factory;
	}
	
	private InstFactory visitIntInst(IntInsnNode iin,
			int instIndex,
			InstFactory factory) {
		if(iin.getOpcode() == Opcodes.BIPUSH) {
			factory = factory.forConstValue((byte) iin.operand);
		} if(iin.getOpcode() == Opcodes.SIPUSH) {
			factory = factory.forConstValue((short) iin.operand);
		} if(iin.getOpcode() == Opcodes.NEWARRAY) {
			org.objectweb.asm.Type type;
			switch(iin.operand) {
			case Opcodes.T_BOOLEAN:
				type = org.objectweb.asm.Type.BOOLEAN_TYPE;
				break;
			case Opcodes.T_BYTE:
				type = org.objectweb.asm.Type.BYTE_TYPE;
				break;
			case Opcodes.T_CHAR:
				type = org.objectweb.asm.Type.CHAR_TYPE;
				break;
			case Opcodes.T_DOUBLE:
				type = org.objectweb.asm.Type.DOUBLE_TYPE;
				break;
			case Opcodes.T_FLOAT:
				type = org.objectweb.asm.Type.FLOAT_TYPE;
				break;
			case Opcodes.T_INT:
				type = org.objectweb.asm.Type.INT_TYPE;
				break;
			case Opcodes.T_LONG:
				type = org.objectweb.asm.Type.LONG_TYPE;
				break;
			case Opcodes.T_SHORT:
				type = org.objectweb.asm.Type.SHORT_TYPE;
				break;
			default:
				throw new RuntimeException("invalid type identifier");
			}
			factory = factory.forType(type);
		}
		return factory;
	}
	
	private InstFactory visitInvokeDynamicInst(InvokeDynamicInsnNode idin,
			int instIndex,
			InstFactory factory) {
		return factory.forTypeDescriptor(idin.desc)
				.forName(idin.name);
	}
	
	private InstFactory visitJumpInst(JumpInsnNode jin,
			int instIndex,
			InstFactory factory) {
		return factory.forSingleTarget(jin.label.getLabel());
	}
	
	private InstFactory visitLdcInst(LdcInsnNode lin,
			int instIndex,
			InstFactory factory) {
		return factory.forConstValue(lin.cst);
	}
	
	private InstFactory visitLookupSwitchInst(LookupSwitchInsnNode lsin,
			int instIndex,
			InstFactory factory) {
		@SuppressWarnings("unchecked")
		IntStream is = lsin.keys.stream()
				.mapToInt(o -> ((Integer) o).intValue());
		org.objectweb.asm.Label[] ls = new org.objectweb.asm.Label[lsin.labels.size()];
		Iterator<?> lsit = lsin.labels.iterator();
		int i = 0;
		while(lsit.hasNext()) {
			ls[i++] = ((LabelNode) lsit.next()).getLabel();
		}
		return factory.forKeys(is.toArray())
				.forLabels(ls)
				.forDefaultLabel(lsin.dflt.getLabel());
	}
	
	private InstFactory visitMethodInst(MethodInsnNode min,
			int instIndex,
			InstFactory factory) {
		org.objectweb.asm.Type ownerType = 
				org.objectweb.asm.Type.getType(min.owner);
		addReferencedType(ownerType);
		return factory.forTypeDescriptor(min.desc)
				.forOwner(ownerType)
				.forName(min.name);
	}
	
	private InstFactory visitMultiANewArrayInst(MultiANewArrayInsnNode manain,
			int instIndex,
			InstFactory factory) {
		org.objectweb.asm.Type type = 
				org.objectweb.asm.Type.getType(manain.desc);
		addReferencedType(type);
		return factory.forType(type).forDimentions(manain.dims);
	}
	
	private InstFactory visitTableSwitchInst(TableSwitchInsnNode tsin,
			int instIndex,
			InstFactory factory) {
		org.objectweb.asm.Label[] ls = new org.objectweb.asm.Label[tsin.labels.size()];
		Iterator<?> lsit = tsin.labels.iterator();
		int i = 0;
		while(lsit.hasNext()) {
			ls[i++] = ((LabelNode) lsit.next()).getLabel();
		}
		return factory.forMin(tsin.min)
				.forMax(tsin.max)
				.forLabels(ls)
				.forDefaultLabel(tsin.dflt.getLabel());
	}
	
	private InstFactory visitTypeInst(TypeInsnNode tin,
			int instIndex,
			InstFactory factory) {
		org.objectweb.asm.Type type = 
				org.objectweb.asm.Type.getType(tin.desc);
		addReferencedType(type);
		return factory.forType(type);
	}
	
	private InstFactory visitVarInst(VarInsnNode vin,
			int instIndex,
			InstFactory factory) {
		return factory.forVarIndex(vin.var);
	}
	
	@SuppressWarnings("unchecked")
	private void visitMethodBody(MethodNode methNode, List<Inst> body) {
		/*inferring types for ATHROW instructions*/
		typeInfo = TypeInference.v()
				.forMethod(methNode)
				.ofClass(this)
				.maxOperands(1)
				.wanting(opcode -> opcode == Opcodes.ATHROW)
				.doAnalysis();
		
		activeTCBs = new ArrayList<>();
		
		ListIterator<?> lit = methNode.instructions.iterator();
		Label label = null;
		int i = 0;
		
		while(lit.hasNext()) {
			AbstractInsnNode ai = (AbstractInsnNode) lit.next();
			
			InstFactory factory = InstFactory.v();
			
//			List<Label> surroundingHandlers = activeTCBs.stream()
//					.map(tcnb -> tcnb.handler.getLabel())
//					.collect(Collectors.toList());
//			if(surroundingHandlers.isEmpty())
//				surroundingHandlers = null; //save heap space!
			List<TryCatchBlockNode> surroundingTCBs = activeTCBs.isEmpty() ? 
					null : new ArrayList<>(activeTCBs);
			//this is common for all instructions
			factory.forSurroundingTCBs(surroundingTCBs);
			
			if(ai instanceof FieldInsnNode) {
				factory = visitFieldInst((FieldInsnNode) ai, i, factory);				
			} else if(ai instanceof IincInsnNode) {
				factory = visitIIncInst((IincInsnNode) ai, i, factory);
			} else if(ai instanceof InsnNode) {
				factory = visitInst((InsnNode) ai, i, factory);				
			} else if(ai instanceof IntInsnNode) {
				factory = visitIntInst((IntInsnNode) ai, i, factory);
			} else if(ai instanceof InvokeDynamicInsnNode) {
				factory = visitInvokeDynamicInst((InvokeDynamicInsnNode) ai,
						i, factory);
			} else if(ai instanceof JumpInsnNode) {
				factory = visitJumpInst((JumpInsnNode) ai, i, factory);
			} else if(ai instanceof LabelNode) {
				LabelNode ln = (LabelNode) ai;
				label = ln.getLabel();
				//checking entries
				activeTCBs.addAll((List<TryCatchBlockNode>) methNode.tryCatchBlocks.stream()
							.filter(o -> ((TryCatchBlockNode) o).start.equals(ln))
							.collect(Collectors.toList()));
				//checking exits
				activeTCBs = activeTCBs.stream()
						.filter(tcbn -> !(tcbn.end.equals(ln)))
						.collect(Collectors.toList());
			} else if(ai instanceof LdcInsnNode) {
				factory = visitLdcInst((LdcInsnNode) ai, i, factory);
			} else if(ai instanceof LineNumberNode) {
				//TODO ... ?
			} else if(ai instanceof LookupSwitchInsnNode) {
				factory = visitLookupSwitchInst((LookupSwitchInsnNode) ai, i, factory);				
			} else if(ai instanceof MethodInsnNode) {
				factory = visitMethodInst((MethodInsnNode) ai, i, factory);
			} else if(ai instanceof MultiANewArrayInsnNode) {
				factory = visitMultiANewArrayInst((MultiANewArrayInsnNode) ai,
						i, factory);
			} else if(ai instanceof TableSwitchInsnNode) {
				factory = visitTableSwitchInst((TableSwitchInsnNode) ai, i, factory);
			} else if(ai instanceof TypeInsnNode) {
				factory = visitTypeInst((TypeInsnNode) ai, i, factory);				
			} else if(ai instanceof VarInsnNode) {
				factory = visitVarInst((VarInsnNode) ai, i, factory);
			} else if(ai instanceof FrameNode) {
				// TODO ... ?
			} else {
				throw new RuntimeException("unexpected opcode");
			}
			if(!(ai instanceof LabelNode) 
					&& !(ai instanceof LineNumberNode)
					&& !(ai instanceof FrameNode)) {
				Inst inst = factory.create(ai.getOpcode());
				inst.setLabel(label);
				body.add(inst);
				label = null; //consume the label
			}
			i++;
		}
	}
}
