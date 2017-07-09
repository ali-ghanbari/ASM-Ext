package asmext.instruction.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import asmext.instruction.AThrow;
import asmext.instruction.Addition;
import asmext.instruction.And;
import asmext.instruction.ArrayLength;
import asmext.instruction.CheckCast;
import asmext.instruction.Comparison;
import asmext.instruction.Conversion;
import asmext.instruction.Division;
import asmext.instruction.GetInstanceField;
import asmext.instruction.GetStaticField;
import asmext.instruction.Goto;
import asmext.instruction.IInc;
import asmext.instruction.Inst;
import asmext.instruction.InstanceOf;
import asmext.instruction.InvokeDynamic;
import asmext.instruction.InvokeInterface;
import asmext.instruction.InvokeSpecial;
import asmext.instruction.InvokeStatic;
import asmext.instruction.InvokeVirtual;
import asmext.instruction.JSR;
import asmext.instruction.LoadArraySlot;
import asmext.instruction.LoadDoubleConst;
import asmext.instruction.LoadDoubleLocal;
import asmext.instruction.LoadFloatConst;
import asmext.instruction.LoadFloatLocal;
import asmext.instruction.LoadIntConst;
import asmext.instruction.LoadIntLocal;
import asmext.instruction.LoadLongConst;
import asmext.instruction.LoadLongLocal;
import asmext.instruction.LoadNullConst;
import asmext.instruction.LoadRefLocal;
import asmext.instruction.LoadStringConst;
import asmext.instruction.LoadTypeConst;
import asmext.instruction.LookupSwitch;
import asmext.instruction.MonitorEnter;
import asmext.instruction.MonitorExit;
import asmext.instruction.Multiplication;
import asmext.instruction.Negation;
import asmext.instruction.New;
import asmext.instruction.NewArray;
import asmext.instruction.Nop;
import asmext.instruction.Or;
import asmext.instruction.Remainder;
import asmext.instruction.Reserved;
import asmext.instruction.Ret;
import asmext.instruction.Return;
import asmext.instruction.SetInstanceField;
import asmext.instruction.SetStaticField;
import asmext.instruction.ShiftLeft;
import asmext.instruction.ShiftRight;
import asmext.instruction.SingleTargetConditional;
import asmext.instruction.StackInst;
import asmext.instruction.StoreArraySlot;
import asmext.instruction.StoreDoubleLocal;
import asmext.instruction.StoreFloatLocal;
import asmext.instruction.StoreIntLocal;
import asmext.instruction.StoreLongLocal;
import asmext.instruction.StoreRefLocal;
import asmext.instruction.Subtraction;
import asmext.instruction.TableSwitch;
import asmext.instruction.UntypedConditional;
import asmext.instruction.Wide;
import asmext.instruction.Xor;

/**
 * Provides a factory method to create instructions based on different parameters
 * 
 * @author Ali Ghanbari
 *
 */
public class InstFactory {
	private static InstFactory instance = null;
	
	private static enum Param {
		VAR_INDEX,
		VALUE,
		SINGLE_TARGET,
		MIN,
		MAX,
		DEF_LABEL,
		LABELS,
		KEYS,
		NAME,
		OWNER,
		DESC,
		TYPE,
		DIMS,
		SURROUNDING
	}
	
	private Map<Param, Object> options;
	
	private InstFactory() {
		options = new HashMap<>();
	}
	
	public static InstFactory v() {
		if(instance == null)
			instance = new InstFactory();
		instance.options.clear();
		return instance;
	}
	
	public InstFactory forVarIndex(int varIndex) {
		options.put(Param.VAR_INDEX, varIndex);
		return this;
	}
	
	public InstFactory forConstValue(Object value) {
		options.put(Param.VALUE, value);
		return this;
	}
	
	public InstFactory forSingleTarget(Label target) {
		options.put(Param.SINGLE_TARGET, target);
		return this;
	}
	
	public InstFactory forMin(int min) {
		options.put(Param.MIN, min);
		return this;
	}
	
	public InstFactory forMax(int max) {
		options.put(Param.MAX, max);
		return this;
	}
	
	public InstFactory forDefaultLabel(Label defLabel) {
		options.put(Param.DEF_LABEL, defLabel);
		return this;
	}
	
	public InstFactory forLabels(Label[] labels) {
		options.put(Param.LABELS, labels);
		return this;
	}
	
	public InstFactory forKeys(int[] keys) {
		options.put(Param.KEYS, keys);
		return this;
	}
	
	public InstFactory forOwner(Type owner) {
		options.put(Param.OWNER, owner);
		return this;
	}
	
	public InstFactory forName(String name) {
		options.put(Param.NAME, name);
		return this;
	}
	
	public InstFactory forTypeDescriptor(String typeDesc) {
		options.put(Param.DESC, typeDesc);
		return this;
	}
	
	public InstFactory forType(Type type) {
		options.put(Param.TYPE, type);
		return this;
	}
	
	public InstFactory forDimentions(int dims) {
		options.put(Param.DIMS, dims);
		return this;
	}
	
	public InstFactory forSurroundingHandlers(List<Label> surroundingHandlers) {
		options.put(Param.SURROUNDING, surroundingHandlers);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public Inst create(int opcode) {
		Object value;
		int varIndex;
		Label label;
		int min;
		int max;
		Label[] labels;
		int[] keys;
		String name;
		Type owner;
		String desc;
		int dims = 1;
		Type type;
		List<Label> surroundingHandlers;
		
		//all of instruction have this
		surroundingHandlers = (List<Label>) options.get(Param.SURROUNDING);
		
		switch(opcode) {
		case Opcodes.NOP:
			return new Nop(surroundingHandlers);
		case Opcodes.ACONST_NULL:
			return new LoadNullConst(surroundingHandlers);
		case Opcodes.ICONST_M1:
		case Opcodes.ICONST_0:
		case Opcodes.ICONST_1:
		case Opcodes.ICONST_2:
		case Opcodes.ICONST_3:
		case Opcodes.ICONST_4:
		case Opcodes.ICONST_5:
			return new LoadIntConst(opcode, surroundingHandlers);
		case Opcodes.LCONST_0:
		case Opcodes.LCONST_1:
			return new LoadLongConst(opcode, surroundingHandlers);
		case Opcodes.FCONST_0:
		case Opcodes.FCONST_1:
			return new LoadFloatConst(opcode, surroundingHandlers);
		case Opcodes.DCONST_0:
		case Opcodes.DCONST_1:
			return new LoadDoubleConst(opcode, surroundingHandlers);
		case Opcodes.BIPUSH:
			value = options.get(Param.VALUE);
			if(value instanceof Byte) {
				return new LoadIntConst(opcode, (byte) value, surroundingHandlers);
			}
			throw new RuntimeException("invalid value type: byte expected");
		case Opcodes.SIPUSH:
			value = options.get(Param.VALUE);
			if(value instanceof Short) {
				return new LoadIntConst(opcode, (short) value, surroundingHandlers);
			}
			throw new RuntimeException("invalid value type: short expected");
		case Opcodes.LDC:
		case 0x13: /*LDC_W*/
			value = options.get(Param.VALUE);
			if(value instanceof String) {
				return new LoadStringConst(opcode, (String) value, surroundingHandlers);
			} else if(value instanceof Type) {
				return new LoadTypeConst(opcode, (Type) value, surroundingHandlers);
			} else if(value instanceof Integer) {
				return new LoadIntConst(opcode, (int) value, surroundingHandlers);
			} else if(value instanceof Long) {
				return new LoadLongConst(opcode, (long) value, surroundingHandlers);
			} else if(value instanceof Float) {
				return new LoadFloatConst(opcode, (float) value, surroundingHandlers);
			} else if(value instanceof Double) {
				return new LoadDoubleConst(opcode, (double) value, surroundingHandlers);
			}
			throw new RuntimeException("invalid value type: string/type expected");
		case 0x14:/*LDC2_W*/
			throw new RuntimeException("unsupported opcode");
		case 0x1a: /*ILOAD_0*/
		case 0x1b: /*ILOAD_1*/
		case 0x1c: /*ILOAD_2*/
		case 0x1d: /*ILOAD_3*/
			return new LoadIntLocal(opcode, surroundingHandlers);
		case Opcodes.ILOAD:
			varIndex = (int) options.get(Param.VAR_INDEX);
			if(varIndex >= 0) {
				return new LoadIntLocal(opcode, varIndex, surroundingHandlers);
			}
			throw new RuntimeException("invalid variable index");
		case 0x1e: /*LLOAD_0*/
		case 0x1f: /*LLOAD_1*/
		case 0x20: /*LLOAD_2*/
		case 0x21: /*LLOAD_3*/
			return new LoadLongLocal(opcode, surroundingHandlers);
		case Opcodes.LLOAD:
			varIndex = (int) options.get(Param.VAR_INDEX);
			if(varIndex >= 0) {
				return new LoadLongLocal(opcode, varIndex, surroundingHandlers);
			}
			throw new RuntimeException("invalid variable index");
		case 0x22: /*FLOAD_0*/
		case 0x23: /*FLOAD_1*/
		case 0x24: /*FLOAD_2*/
		case 0x25: /*FLOAD_3*/
			return new LoadFloatLocal(opcode, surroundingHandlers);
		case Opcodes.FLOAD:
			varIndex = (int) options.get(Param.VAR_INDEX);
			if(varIndex >= 0) {
				return new LoadFloatLocal(opcode, varIndex, surroundingHandlers);
			}
			throw new RuntimeException("invalid variable index");
		case 0x26: /*DLOAD_0*/
		case 0x27: /*DLOAD_1*/
		case 0x28: /*DLOAD_2*/
		case 0x29: /*DLOAD_3*/
			return new LoadDoubleLocal(opcode, surroundingHandlers);
		case Opcodes.DLOAD:
			varIndex = (int) options.get(Param.VAR_INDEX);
			if(varIndex >= 0) {
				return new LoadDoubleLocal(opcode, varIndex, surroundingHandlers);
			}
			throw new RuntimeException("invalid variable index");
		case 0x2a: /*ALOAD_0*/
		case 0x2b: /*ALOAD_1*/
		case 0x2c: /*ALOAD_2*/
		case 0x2d: /*ALOAD_3*/
			return new LoadRefLocal(opcode, surroundingHandlers);
		case Opcodes.ALOAD:
			varIndex = (int) options.get(Param.VAR_INDEX);
			if(varIndex >= 0) {
				return new LoadRefLocal(opcode, varIndex, surroundingHandlers);
			}
			throw new RuntimeException("invalid variable index");
		case Opcodes.IALOAD:
		case Opcodes.LALOAD:
		case Opcodes.FALOAD:
		case Opcodes.DALOAD:
		case Opcodes.AALOAD:
		case Opcodes.BALOAD:
		case Opcodes.CALOAD:
		case Opcodes.SALOAD:
			return new LoadArraySlot(opcode, surroundingHandlers);
		case 0x3b: /*ISTORE_0*/
		case 0x3c: /*ISTORE_1*/
		case 0x3d: /*ISTORE_2*/
		case 0x3e: /*ISTORE_3*/
			return new StoreIntLocal(opcode, surroundingHandlers);
		case Opcodes.ISTORE:
			varIndex = (int) options.get(Param.VAR_INDEX);
			if(varIndex >= 0) {
				return new StoreIntLocal(opcode, varIndex, surroundingHandlers);
			}
			throw new RuntimeException("invalid variable index");
		case 0x3f: /*LSTORE_0*/
		case 0x40: /*LSTORE_1*/
		case 0x41: /*LSTORE_2*/
		case 0x42: /*LSTORE_3*/
			return new StoreLongLocal(opcode, surroundingHandlers);
		case Opcodes.LSTORE:
			varIndex = (int) options.get(Param.VAR_INDEX);
			if(varIndex >= 0) {
				return new StoreLongLocal(opcode, varIndex, surroundingHandlers);
			}
			throw new RuntimeException("invalid variable index");
		case 0x43: /*FSTORE_0*/
		case 0x44: /*FSTORE_1*/
		case 0x45: /*FSTORE_2*/
		case 0x46: /*FSTORE_3*/
			return new StoreFloatLocal(opcode, surroundingHandlers);
		case Opcodes.FSTORE:
			varIndex = (int) options.get(Param.VAR_INDEX);
			if(varIndex >= 0) {
				return new StoreFloatLocal(opcode, varIndex, surroundingHandlers);
			}
			throw new RuntimeException("invalid variable index");
		case 0x47: /*DSTORE_0*/
		case 0x48: /*DSTORE_1*/
		case 0x49: /*DSTORE_2*/
		case 0x4a: /*DSTORE_3*/
			return new StoreDoubleLocal(opcode, surroundingHandlers);
		case Opcodes.DSTORE:
			varIndex = (int) options.get(Param.VAR_INDEX);
			if(varIndex >= 0) {
				return new StoreDoubleLocal(opcode, varIndex, surroundingHandlers);
			}
			throw new RuntimeException("invalid variable index");
		case 0x4b: /*ASTORE_0*/
		case 0x4c: /*ASTORE_1*/
		case 0x4d: /*ASTORE_2*/
		case 0x4e: /*ASTORE_3*/
			return new StoreRefLocal(opcode, surroundingHandlers);
		case Opcodes.ASTORE:
			varIndex = (int) options.get(Param.VAR_INDEX);
			if(varIndex >= 0) {
				return new StoreRefLocal(opcode, varIndex, surroundingHandlers);
			}
			throw new RuntimeException("invalid variable index");
		case Opcodes.IASTORE:
		case Opcodes.LASTORE:
		case Opcodes.FASTORE:
		case Opcodes.DASTORE:
		case Opcodes.AASTORE:
		case Opcodes.BASTORE:
		case Opcodes.CASTORE:
		case Opcodes.SASTORE:
			return new StoreArraySlot(opcode, surroundingHandlers);
		case Opcodes.POP:
		case Opcodes.POP2:
		case Opcodes.DUP:
		case Opcodes.DUP_X1:
		case Opcodes.DUP_X2:
		case Opcodes.DUP2:
		case Opcodes.DUP2_X1:
		case Opcodes.DUP2_X2:
		case Opcodes.SWAP:
			return new StackInst(opcode, surroundingHandlers);
		case Opcodes.IADD:
		case Opcodes.LADD:
		case Opcodes.FADD:
		case Opcodes.DADD:
			return new Addition(opcode, surroundingHandlers);
		case Opcodes.ISUB:
		case Opcodes.LSUB:
		case Opcodes.FSUB:
		case Opcodes.DSUB:
			return new Subtraction(opcode, surroundingHandlers);
		case Opcodes.IMUL:
		case Opcodes.LMUL:
		case Opcodes.FMUL:
		case Opcodes.DMUL:
			return new Multiplication(opcode, surroundingHandlers);
		case Opcodes.IDIV:
		case Opcodes.LDIV:
		case Opcodes.FDIV:
		case Opcodes.DDIV:
			return new Division(opcode, surroundingHandlers);
		case Opcodes.IREM:
		case Opcodes.LREM:
		case Opcodes.FREM:
		case Opcodes.DREM:
			return new Remainder(opcode, surroundingHandlers);
		case Opcodes.INEG:
		case Opcodes.LNEG:
		case Opcodes.FNEG:
		case Opcodes.DNEG:
			return new Negation(opcode, surroundingHandlers);
		case Opcodes.ISHL:
		case Opcodes.LSHL:
			return new ShiftLeft(opcode, surroundingHandlers);
		case Opcodes.ISHR:
		case Opcodes.LSHR:
		case Opcodes.IUSHR:
		case Opcodes.LUSHR:
			return new ShiftRight(opcode, surroundingHandlers);
		case Opcodes.IAND:
		case Opcodes.LAND:
			return new And(opcode, surroundingHandlers);
		case Opcodes.IOR:
		case Opcodes.LOR:
			return new Or(opcode, surroundingHandlers);
		case Opcodes.IXOR:
		case Opcodes.LXOR:
			return new Xor(opcode, surroundingHandlers);
		case Opcodes.IINC:
			varIndex = (int) options.get(Param.VAR_INDEX);
			value = options.get(Param.VALUE);
			if(value instanceof Integer && varIndex >= 0) {
				return new IInc(varIndex, (int) value, surroundingHandlers);
			}
			throw new RuntimeException("invalid index or value type");
		case Opcodes.I2L:
		case Opcodes.I2F:
		case Opcodes.I2D:
		case Opcodes.L2I:
		case Opcodes.L2F:
		case Opcodes.L2D:
		case Opcodes.F2I:
		case Opcodes.F2L:
		case Opcodes.F2D:
		case Opcodes.D2I:
		case Opcodes.D2L:
		case Opcodes.D2F:
		case Opcodes.I2B:
		case Opcodes.I2C:
		case Opcodes.I2S:
			return new Conversion(opcode, surroundingHandlers);
		case Opcodes.LCMP:
		case Opcodes.FCMPL:
		case Opcodes.FCMPG:
		case Opcodes.DCMPL:
		case Opcodes.DCMPG:
			return new Comparison(opcode, surroundingHandlers);
		case Opcodes.IFEQ:
		case Opcodes.IFNE:
		case Opcodes.IFLT:
		case Opcodes.IFGE:
		case Opcodes.IFGT:
		case Opcodes.IFLE:
			label = (Label) options.get(Param.SINGLE_TARGET);
			if(label != null) {
				return new UntypedConditional(opcode, label, surroundingHandlers);
			}
			throw new RuntimeException("a jump target expected");
		case Opcodes.IF_ICMPEQ:
		case Opcodes.IF_ICMPNE:
		case Opcodes.IF_ICMPLT:
		case Opcodes.IF_ICMPGE:
		case Opcodes.IF_ICMPGT:
		case Opcodes.IF_ICMPLE:
		case Opcodes.IF_ACMPEQ:
		case Opcodes.IF_ACMPNE:
		case Opcodes.IFNULL:
		case Opcodes.IFNONNULL:
			label = (Label) options.get(Param.SINGLE_TARGET);
			if(label != null) {
				return new SingleTargetConditional(opcode, label, surroundingHandlers);
			}
			throw new RuntimeException("a jump target expected");
		case 0xc8: /*GOTO_W*/
		case Opcodes.GOTO:
			label = (Label) options.get(Param.SINGLE_TARGET);
			if(label != null) {
				return new Goto(opcode, label, surroundingHandlers);
			}
			throw new RuntimeException("a jump target expected");
		case 0xc9: /*JSR_W*/
		case Opcodes.JSR:
			label = (Label) options.get(Param.SINGLE_TARGET);
			if(label != null) {
				return new JSR(opcode, label, surroundingHandlers);
			}
			throw new RuntimeException("a jump target expected");
		case Opcodes.RET:
			varIndex = (int) options.get(Param.VAR_INDEX);
			if(varIndex >= 0) {
				return new Ret(varIndex, surroundingHandlers);
			}
			throw new RuntimeException("invalid variable index");
		case Opcodes.TABLESWITCH:
			label = (Label) options.get(Param.DEF_LABEL);
			min = (int) options.get(Param.MIN);
			max = (int) options.get(Param.MAX);
			labels = (Label[]) options.get(Param.LABELS);
			if(label != null && labels.length == (max - min + 1)) {
				return new TableSwitch(min, max, label, labels, surroundingHandlers);			
			}
			throw new RuntimeException("TABLESWITCH provided with invalid argument(s)");
		case Opcodes.LOOKUPSWITCH:
			label = (Label) options.get(Param.DEF_LABEL);
			keys = (int[]) options.get(Param.KEYS);
			labels = (Label[]) options.get(Param.LABELS);
			if(label != null && labels.length == keys.length) {
				return new LookupSwitch(label, keys, labels, surroundingHandlers);			
			}
			throw new RuntimeException("LOOKUPSWITCH provided with invalid argument(s)");
		case Opcodes.IRETURN:
		case Opcodes.LRETURN:
		case Opcodes.FRETURN:
		case Opcodes.DRETURN:
		case Opcodes.ARETURN:
		case Opcodes.RETURN:
			return new Return(opcode, surroundingHandlers);
		case Opcodes.GETSTATIC:
		case Opcodes.PUTSTATIC:
		case Opcodes.GETFIELD:
		case Opcodes.PUTFIELD:
			name = (String) options.get(Param.NAME);
			desc = (String) options.get(Param.DESC);
			owner = (Type) options.get(Param.OWNER);
			if(name != null && desc != null && owner != null) {
				if(opcode == Opcodes.GETSTATIC)
					return new GetStaticField(owner, name, desc, surroundingHandlers);
				if(opcode == Opcodes.PUTSTATIC)
					return new SetStaticField(owner, name, desc, surroundingHandlers);
				if(opcode == Opcodes.PUTFIELD)
					return new SetInstanceField(owner, name, desc, surroundingHandlers);
				if(opcode == Opcodes.GETFIELD)
					return new GetInstanceField(owner, name, desc, surroundingHandlers);
			}
			throw new RuntimeException("field access instruction provided with invalid argument(s)");
		case Opcodes.INVOKEDYNAMIC:
			name = (String) options.get(Param.NAME);
			desc = (String) options.get(Param.DESC);
			if(name != null && desc != null) {
				return new InvokeDynamic(name, desc, surroundingHandlers);
			}
			throw new RuntimeException("INVOKEDYNAMIC instruction provided with invalid argument(s)");
		case Opcodes.INVOKEINTERFACE:
		case Opcodes.INVOKESPECIAL:
		case Opcodes.INVOKESTATIC:
		case Opcodes.INVOKEVIRTUAL:
			name = (String) options.get(Param.NAME);
			desc = (String) options.get(Param.DESC);
			owner = (Type) options.get(Param.OWNER);
			if(name != null && desc != null && owner != null) {
				if(opcode == Opcodes.INVOKEINTERFACE)
					return new InvokeInterface(owner, name, desc, surroundingHandlers);
				if(opcode == Opcodes.INVOKESPECIAL)
					return new InvokeSpecial(owner, name, desc, surroundingHandlers);
				if(opcode == Opcodes.INVOKESTATIC)
					return new InvokeStatic(owner, name, desc, surroundingHandlers);
				if(opcode == Opcodes.INVOKEVIRTUAL)
					return new InvokeVirtual(owner, name, desc, surroundingHandlers);
			}
			throw new RuntimeException("method invocation instruction provided with invalid argument(s)");
		case Opcodes.MULTIANEWARRAY:
			dims = (int) options.get(Param.DIMS);
		case Opcodes.ANEWARRAY:
		case Opcodes.NEWARRAY:
			type = (Type) options.get(Param.TYPE);
			if(type != null && dims >= 1) {
				return new NewArray(opcode, type, dims, surroundingHandlers);
			}
			throw new RuntimeException("array allocation instruction provided with invalid argument(s)");
		case Opcodes.NEW:
			type = (Type) options.get(Param.TYPE);
			if(type != null) {
				return new New(type, surroundingHandlers);
			}
			throw new RuntimeException("object allocation instruction provided with invalid argument(s)");
		case Opcodes.ARRAYLENGTH:
			return new ArrayLength(surroundingHandlers);
		case Opcodes.ATHROW:
			return new AThrow(surroundingHandlers);
		case Opcodes.MONITORENTER:
			return new MonitorEnter(surroundingHandlers);
		case Opcodes.MONITOREXIT:
			return new MonitorExit(surroundingHandlers);
		case Opcodes.CHECKCAST:
			type = (Type) options.get(Param.TYPE);
			if(type != null) {
				return new CheckCast(type, surroundingHandlers);
			}
			throw new RuntimeException("CHECKCAST instruction provided with invalid argument(s)");
		case Opcodes.INSTANCEOF:
			type = (Type) options.get(Param.TYPE);
			if(type != null) {
				return new InstanceOf(type, surroundingHandlers);
			}
			throw new RuntimeException("INSTANCEOF instruction provided with invalid argument(s)");
		case 0xc4: /*WIDE*/
			return new Wide(surroundingHandlers);
		case 0xca: /*BREAKPOINT*/
		case 0xfe: /*IMPDEP1*/
		case 0xff: /*IMPDEP2*/
			return new Reserved(opcode, surroundingHandlers);
		default:
			throw new RuntimeException("invalid opcode");	
		}
	}
}
