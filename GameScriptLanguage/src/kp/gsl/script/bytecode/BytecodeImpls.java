/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.bytecode;

import java.util.Objects;
import kp.gsl.lang.GSLDataType;
import kp.gsl.script.Instruction;
import static kp.gsl.script.bytecode.ByteOps.b2t;
import static kp.gsl.script.bytecode.ByteOps.i2t;

/**
 *
 * @author Asus
 */
public interface BytecodeImpls
{
    class NOOP extends NoParsBytecode
    {
        @Override protected byte inst() { return Instruction.NOOP; }
        @Override protected int stack(boolean pars) { return 0; }
        @Override public String getName() { return "NOOP"; }
    }
    
    class LOAD_CONST extends OneWordRefBytecode
    {
        public LOAD_CONST(int reference) { super(reference); }
        public LOAD_CONST(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.LOAD_CONST : Instruction.LOAD_CONST16); }
        @Override protected int stack(boolean pars) { return pars ? 0 : 1; }
        @Override protected String getName(boolean is16) { return is16 ? "LOAD_CONST" : "LOAD_CONST16"; }
    }
    
    class LOAD_VAR extends OneByteRefBytecode
    {
        public LOAD_VAR(int ref_or_byte) { super(ref_or_byte); }
        
        @Override protected byte inst() { return Instruction.LOAD_VAR; }
        @Override protected int stack(boolean pars) { return pars ? 0 : 1; }
        @Override public String getName() { return "LOAD_VAR"; }
    }
    
    class LOAD_FUNCTION extends OneWordRefBytecode
    {
        public LOAD_FUNCTION(int funcIdx) { super(funcIdx); }
        public LOAD_FUNCTION(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.LOAD_FUNCTION : Instruction.LOAD_FUNCTION16); }
        @Override protected int stack(boolean pars) { return pars ? 0 : 1; }
        @Override protected String getName(boolean is16) { return is16 ? "LOAD_FUNCTION" : "LOAD_FUNCTION16"; }
    }
    
    class LOAD_CLOSURE extends ByteWordRefBytecode
    {
        public LOAD_CLOSURE(int parsCount, int funcIdx) { super(parsCount, funcIdx); }
        public LOAD_CLOSURE(byte b0, byte b1, byte b2) { super(b0, b1, b2); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.LOAD_CLOSURE : Instruction.LOAD_CLOSURE16); }
        @Override protected int stack(boolean pars) { return pars ? b0.getValue() : 1; }
        @Override protected String getName(boolean is16) { return is16 ? "LOAD_CLOSURE" : "LOAD_CLOSURE16"; }
    }
    
    class LOAD_GLOBAL extends OneWordRefBytecode
    {
        public LOAD_GLOBAL(int identifier) { super(identifier); }
        public LOAD_GLOBAL(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.LOAD_GLOBAL : Instruction.LOAD_GLOBAL16); }
        @Override protected int stack(boolean pars) { return pars ? 0 : 1; }
        @Override protected String getName(boolean is16) { return is16 ? "LOAD_GLOBAL" : "LOAD_GLOBAL16"; }
    }
    
    class LOAD_NULL extends NoParsBytecode
    {
        @Override protected byte inst() { return Instruction.LOAD_NULL; }
        @Override protected int stack(boolean pars) { return pars ? 0 : 1; }
        @Override public String getName() { return "LOAD_NULL"; }
    }
    
    class STORE_GLOBAL extends OneWordRefBytecode
    {
        public STORE_GLOBAL(int identifier) { super(identifier); }
        public STORE_GLOBAL(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.STORE_GLOBAL : Instruction.STORE_GLOBAL16); }
        @Override protected int stack(boolean pars) { return pars ? 1 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "STORE_GLOBAL" : "STORE_GLOBAL16"; }
    }
    
    class STORE_VAR extends OneByteRefBytecode
    {
        public STORE_VAR(int ref_or_byte) { super(ref_or_byte); }
        
        @Override protected byte inst() { return Instruction.STORE_VAR; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 0; }
        @Override public String getName() { return "STORE_VAR"; }
    }
    
    class ST_VAR_T extends OneByteRefBytecode
    {
        private final GSLDataType type;
        
        public ST_VAR_T(int ref_or_byte, GSLDataType type) { super(ref_or_byte); this.type = Objects.requireNonNull(type); }
        public ST_VAR_T(int ref_or_byte, int type) { this(ref_or_byte, i2t(type)); }
        public ST_VAR_T(int ref_or_byte, byte type) { this(ref_or_byte, b2t(type)); }
        
        @Override protected byte inst()
        {
            switch(type)
            {
                default:
                case NULL: return Instruction.ST_VAR_NULL;
                case INTEGER: return Instruction.ST_VAR_INT;
                case FLOAT: return Instruction.ST_VAR_FLOAT;
                case BOOLEAN: return Instruction.ST_VAR_BOOL;
                case STRING: return Instruction.ST_VAR_STRING;
                case CONST_TUPLE: return Instruction.ST_VAR_CTUPLE;
                case CONST_MAP: return Instruction.ST_VAR_CMAP;
                case FUNCTION: return Instruction.ST_VAR_FUNCTION;
                case LIST: return Instruction.ST_VAR_LIST;
                case TUPLE: return Instruction.ST_VAR_TUPLE;
                case MAP: return Instruction.ST_VAR_MAP;
                case STRUCT: return Instruction.ST_VAR_STRUCT;
                case BLUEPRINT: return Instruction.ST_VAR_BPRINT;
                case OBJECT: return Instruction.ST_VAR_OBJECT;
                case ITERATOR: return Instruction.ST_VAR_ITERATOR;
                case RAW_BYTES: return Instruction.ST_VAR_BYTES;
                case NATIVE: return Instruction.ST_VAR_NATIVE;
            }
        }
        @Override protected int stack(boolean pars) { return pars ? 1 : 0; }
        @Override public String getName()
        {
            switch(type)
            {
                default:
                case NULL: return "ST_VAR_NULL";
                case INTEGER: return "ST_VAR_INT";
                case FLOAT: return "ST_VAR_FLOAT";
                case BOOLEAN: return "ST_VAR_BOOL";
                case STRING: return "ST_VAR_STRING";
                case CONST_TUPLE: return "ST_VAR_CTUPLE";
                case CONST_MAP: return "ST_VAR_CMAP";
                case FUNCTION: return "ST_VAR_FUNCTION";
                case LIST: return "ST_VAR_LIST";
                case TUPLE: return "ST_VAR_TUPLE";
                case MAP: return "ST_VAR_MAP";
                case STRUCT: return "ST_VAR_STRUCT";
                case BLUEPRINT: return "ST_VAR_BPRINT";
                case OBJECT: return "ST_VAR_OBJECT";
                case ITERATOR: return "ST_VAR_ITERATOR";
                case RAW_BYTES: return "ST_VAR_BYTES";
                case NATIVE: return "ST_VAR_NATIVE";
            }
        }
    }
    
    class LIST_NEW extends NoParsBytecode
    {
        @Override protected byte inst() { return Instruction.LIST_NEW; }
        @Override protected int stack(boolean pars) { return pars ? 0 : 1; }
        @Override public String getName() { return "LIST_NEW"; }
    }
    
    class TUPLE_NEW extends NoParsBytecode
    {
        @Override protected byte inst() { return Instruction.TUPLE_NEW; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "TUPLE_NEW"; }
    }
    
    class MAP_NEW extends NoParsBytecode
    {
        @Override protected byte inst() { return Instruction.MAP_NEW; }
        @Override protected int stack(boolean pars) { return pars ? 0 : 1; }
        @Override public String getName() { return "MAP_NEW"; }
    }
    
    class STRUCT_NEW extends NoParsBytecode
    {
        @Override protected byte inst() { return Instruction.STRUCT_NEW; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "STRUCT_NEW"; }
    }
    
    class BLUEPRINT_NEW extends NoParsBytecode
    {
        @Override protected byte inst() { return Instruction.BLUEPRINT_NEW; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "BLUEPRINT_NEW"; }
    }
    
    class OBJECT_NEW extends OneByteBytecode
    {
        public OBJECT_NEW(int argsCount) { super(argsCount); }
        public OBJECT_NEW(byte b0) { super(b0); }
        
        @Override protected byte inst() { return Instruction.OBJECT_NEW; }
        @Override protected int stack(boolean pars) { return pars ? (b0.getValue() != 0 ? 1 : 0) : 1; }
        @Override public String getName() { return "OBJECT_NEW"; }
    }
    
    class ITERATOR_NEW extends NoParsBytecode
    {
        @Override protected byte inst() { return Instruction.ITERATOR_NEW; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "ITERATOR_NEW"; }
    }
    
    class BYTES_NEW extends NoParsBytecode
    {
        @Override protected byte inst() { return Instruction.BYTES_NEW; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "BYTES_NEW"; }
    }
    
    class CAST_T extends NoParsBytecode
    {
        private final GSLDataType type;
        
        public CAST_T(GSLDataType type) { this.type = Objects.requireNonNull(type); }
        public CAST_T(int type) { this(i2t(type)); }
        public CAST_T(byte type) { this(b2t(type)); }
        
        @Override protected byte inst()
        {
            switch(type)
            {
                case INTEGER: return Instruction.CAST_INT;
                case FLOAT: return Instruction.CAST_FLOAT;
                case BOOLEAN: return Instruction.CAST_BOOL;
                case STRING: return Instruction.CAST_STRING;
                case CONST_TUPLE: return Instruction.CAST_CTUPLE;
                case CONST_MAP: return Instruction.CAST_CMAP;
                case FUNCTION: return Instruction.CAST_FUNCTION;
                case LIST: return Instruction.CAST_LIST;
                case TUPLE: return Instruction.CAST_TUPLE;
                case MAP: return Instruction.CAST_MAP;
                case STRUCT: return Instruction.CAST_STRUCT;
                case BLUEPRINT: return Instruction.CAST_BPRINT;
                case OBJECT: return Instruction.CAST_OBJECT;
                case ITERATOR: return Instruction.CAST_ITERATOR;
                case RAW_BYTES: return Instruction.CAST_BYTES;
                default:
                case NATIVE: return Instruction.CAST_NATIVE;
            }
        }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName()
        {
            switch(type)
            {
                case INTEGER: return "CAST_INT";
                case FLOAT: return "CAST_FLOAT";
                case BOOLEAN: return "CAST_BOOL";
                case STRING: return "CAST_STRING";
                case CONST_TUPLE: return "CAST_CTUPLE";
                case CONST_MAP: return "CAST_CMAP";
                case FUNCTION: return "CAST_FUNCTION";
                case LIST: return "CAST_LIST";
                case TUPLE: return "CAST_TUPLE";
                case MAP: return "CAST_MAP";
                case STRUCT: return "CAST_STRUCT";
                case BLUEPRINT: return "CAST_BPRINT";
                case OBJECT: return "CAST_OBJECT";
                case ITERATOR: return "CAST_ITERATOR";
                case RAW_BYTES: return "CAST_BYTES";
                default:
                case NATIVE: return "CAST_NATIVE";
            }
        }
    }
    
    class REF_NEW extends OneByteRefBytecode
    {
        public REF_NEW(int ref_or_byte) { super(ref_or_byte); }
        
        @Override protected byte inst() { return Instruction.REF_NEW; }
        @Override protected int stack(boolean pars) { return pars ? 0 : 1; }
        @Override public String getName() { return "REF_NEW"; }
    }
    
    class LOAD_REF extends NoParsBytecode
    {
        public LOAD_REF() {}
        
        @Override protected byte inst() { return Instruction.LOAD_REF; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "LOAD_REF"; }
    }
    
    class STORE_REF extends NoParsBytecode
    {
        public STORE_REF() {}
        
        @Override protected byte inst() { return Instruction.STORE_REF; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 0; }
        @Override public String getName() { return "STORE_REF"; }
    }
    
    class POP extends NoParsBytecode
    {
        public POP() {}
        
        @Override protected byte inst() { return Instruction.POP; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 0; }
        @Override public String getName() { return "POP"; }
    }
    
    class SWAP extends NoParsBytecode
    {
        public SWAP() {}
        
        @Override protected byte inst() { return Instruction.SWAP; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 2; }
        @Override public String getName() { return "SWAP"; }
    }
    
    class SWAP2 extends NoParsBytecode
    {
        public SWAP2() {}
        
        @Override protected byte inst() { return Instruction.SWAP2; }
        @Override protected int stack(boolean pars) { return pars ? 3 : 3; }
        @Override public String getName() { return "SWAP2"; }
    }
    
    class DUP extends NoParsBytecode
    {
        public DUP() {}
        
        @Override protected byte inst() { return Instruction.DUP; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 2; }
        @Override public String getName() { return "DUP"; }
    }
    
    class DUP2 extends NoParsBytecode
    {
        public DUP2() {}
        
        @Override protected byte inst() { return Instruction.DUP2; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 3; }
        @Override public String getName() { return "DUP2"; }
    }
    
    class GOTO extends BranchBytecode
    {
        public GOTO(int identifier) { super(identifier); }
        public GOTO(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.GOTO : Instruction.GOTO16); }
        @Override protected int stack(boolean pars) { return pars ? 0 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "GOTO" : "GOTO16"; }
    }
    
    class RETURN extends NoParsBytecode
    {
        public RETURN() {}
        
        @Override protected byte inst() { return Instruction.RETURN; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 0; }
        @Override public String getName() { return "RETURN"; }
    }
    
    class RETURN_T extends NoParsBytecode
    {
        private final GSLDataType type;
        
        public RETURN_T(GSLDataType type) { this.type = Objects.requireNonNull(type); }
        public RETURN_T(int type) { this(i2t(type)); }
        public RETURN_T(byte type) { this(b2t(type)); }
        
        @Override protected byte inst()
        {
            switch(type)
            {
                default:
                case NULL: return Instruction.RETURN_NULL;
                case INTEGER: return Instruction.RETURN_INT;
                case FLOAT: return Instruction.RETURN_FLOAT;
                case BOOLEAN: return Instruction.RETURN_BOOL;
                case STRING: return Instruction.RETURN_STRING;
                case CONST_TUPLE: return Instruction.RETURN_CTUPLE;
                case CONST_MAP: return Instruction.RETURN_CMAP;
                case FUNCTION: return Instruction.RETURN_FUNCTION;
                case LIST: return Instruction.RETURN_LIST;
                case TUPLE: return Instruction.RETURN_TUPLE;
                case MAP: return Instruction.RETURN_MAP;
                case STRUCT: return Instruction.RETURN_STRUCT;
                case BLUEPRINT: return Instruction.RETURN_BPRINT;
                case OBJECT: return Instruction.RETURN_OBJECT;
                case ITERATOR: return Instruction.RETURN_ITERATOR;
                case RAW_BYTES: return Instruction.RETURN_BYTES;
                case NATIVE: return Instruction.RETURN_NATIVE;
            }
        }
        @Override protected int stack(boolean pars) { return pars ? (type == GSLDataType.NULL ? 0 : 1) : 0; }
        @Override public String getName()
        {
            switch(type)
            {
                default:
                case NULL: return "RETURN_NULL";
                case INTEGER: return "RETURN_INT";
                case FLOAT: return "RETURN_FLOAT";
                case BOOLEAN: return "RETURN_BOOL";
                case STRING: return "RETURN_STRING";
                case CONST_TUPLE: return "RETURN_CTUPLE";
                case CONST_MAP: return "RETURN_CMAP";
                case FUNCTION: return "RETURN_FUNCTION";
                case LIST: return "RETURN_LIST";
                case TUPLE: return "RETURN_TUPLE";
                case MAP: return "RETURN_MAP";
                case STRUCT: return "RETURN_STRUCT";
                case BLUEPRINT: return "RETURN_BPRINT";
                case OBJECT: return "RETURN_OBJECT";
                case ITERATOR: return "RETURN_ITERATOR";
                case RAW_BYTES: return "RETURN_BYTES";
                case NATIVE: return "RETURN_NATIVE";
            }
        }
    }
    
    class PLUS extends NoParsBytecode
    {
        public PLUS() {}
        
        @Override protected byte inst() { return Instruction.PLUS; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "PLUS"; }
    }
    
    class MINUS extends NoParsBytecode
    {
        public MINUS() {}
        
        @Override protected byte inst() { return Instruction.MINUS; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "MINUS"; }
    }
    
    class MULTIPLY extends NoParsBytecode
    {
        public MULTIPLY() {}
        
        @Override protected byte inst() { return Instruction.MULTIPLY; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "MULTIPLY"; }
    }
    
    class DIVIDE extends NoParsBytecode
    {
        public DIVIDE() {}
        
        @Override protected byte inst() { return Instruction.DIVIDE; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "DIVIDE"; }
    }
    
    class REMAINDER extends NoParsBytecode
    {
        public REMAINDER() {}
        
        @Override protected byte inst() { return Instruction.REMAINDER; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "REMAINDER"; }
    }
    
    class NEGATIVE extends NoParsBytecode
    {
        public NEGATIVE() {}
        
        @Override protected byte inst() { return Instruction.NEGATIVE; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "NEGATIVE"; }
    }
    
    class INCREASE extends NoParsBytecode
    {
        public INCREASE() {}
        
        @Override protected byte inst() { return Instruction.INCREASE; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "INCREASE"; }
    }
    
    class DECREASE extends NoParsBytecode
    {
        public DECREASE() {}
        
        @Override protected byte inst() { return Instruction.DECREASE; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "DECREASE"; }
    }
    
    class BW_SFH_L extends NoParsBytecode
    {
        public BW_SFH_L() {}
        
        @Override protected byte inst() { return Instruction.BW_SFH_L; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "BW_SFH_L"; }
    }
    
    class BW_SFH_R extends NoParsBytecode
    {
        public BW_SFH_R() {}
        
        @Override protected byte inst() { return Instruction.BW_SFH_R; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "BW_SFH_R"; }
    }
    
    class BW_AND extends NoParsBytecode
    {
        public BW_AND() {}
        
        @Override protected byte inst() { return Instruction.BW_AND; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "BW_AND"; }
    }
    
    class BW_OR extends NoParsBytecode
    {
        public BW_OR() {}
        
        @Override protected byte inst() { return Instruction.BW_OR; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "BW_OR"; }
    }
    
    class BW_XOR extends NoParsBytecode
    {
        public BW_XOR() {}
        
        @Override protected byte inst() { return Instruction.BW_XOR; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "BW_XOR"; }
    }
    
    class BW_NOT extends NoParsBytecode
    {
        public BW_NOT() {}
        
        @Override protected byte inst() { return Instruction.BW_NOT; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "BW_NOT"; }
    }
    
    class EQ extends NoParsBytecode
    {
        public EQ() {}
        
        @Override protected byte inst() { return Instruction.EQ; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "EQ"; }
    }
    
    class NOEQ extends NoParsBytecode
    {
        public NOEQ() {}
        
        @Override protected byte inst() { return Instruction.NOEQ; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "NOEQ"; }
    }
    
    class T_EQ extends NoParsBytecode
    {
        public T_EQ() {}
        
        @Override protected byte inst() { return Instruction.T_EQ; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "T_EQ"; }
    }
    
    class T_NOEQ extends NoParsBytecode
    {
        public T_NOEQ() {}
        
        @Override protected byte inst() { return Instruction.T_NOEQ; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "T_NOEQ"; }
    }
    
    class GR extends NoParsBytecode
    {
        public GR() {}
        
        @Override protected byte inst() { return Instruction.GR; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "GR"; }
    }
    
    class SM extends NoParsBytecode
    {
        public SM() {}
        
        @Override protected byte inst() { return Instruction.SM; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "SM"; }
    }
    
    class GREQ extends NoParsBytecode
    {
        public GREQ() {}
        
        @Override protected byte inst() { return Instruction.GREQ; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "GREQ"; }
    }
    
    class SMEQ extends NoParsBytecode
    {
        public SMEQ() {}
        
        @Override protected byte inst() { return Instruction.SMEQ; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "SMEQ"; }
    }
    
    class IS_DEF extends NoParsBytecode
    {
        public IS_DEF() {}
        
        @Override protected byte inst() { return Instruction.IS_DEF; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "IS_DEF"; }
    }
    
    class IS_UNDEF extends NoParsBytecode
    {
        public IS_UNDEF() {}
        
        @Override protected byte inst() { return Instruction.IS_UNDEF; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "IS_UNDEF"; }
    }
    
    class NOT extends NoParsBytecode
    {
        public NOT() {}
        
        @Override protected byte inst() { return Instruction.NOT; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "NOT"; }
    }
    
    class CONCAT extends NoParsBytecode
    {
        public CONCAT() {}
        
        @Override protected byte inst() { return Instruction.CONCAT; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "CONCAT"; }
    }
    
    class LENGTH extends NoParsBytecode
    {
        public LENGTH() {}
        
        @Override protected byte inst() { return Instruction.LENGTH; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "LENGTH"; }
    }
    
    class TYPEID extends NoParsBytecode
    {
        public TYPEID() {}
        
        @Override protected byte inst() { return Instruction.TYPEID; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "TYPEID"; }
    }
    
    class ITERATOR extends NoParsBytecode
    {
        public ITERATOR() {}
        
        @Override protected byte inst() { return Instruction.ITERATOR; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "ITERATOR"; }
    }
    
    class GET extends NoParsBytecode
    {
        public GET() {}
        
        @Override protected byte inst() { return Instruction.GET; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "GET"; }
    }
    
    class GET_I extends OneByteBytecode
    {
        public GET_I(int value) { super(value); }
        public GET_I(byte value) { super(value); }
        
        @Override protected byte inst() { return Instruction.GET_I; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "GET_I"; }
    }
    
    class SET extends NoParsBytecode
    {
        public SET() {}
        
        @Override protected byte inst() { return Instruction.SET; }
        @Override protected int stack(boolean pars) { return pars ? 3 : 1; }
        @Override public String getName() { return "SET"; }
    }
    
    class SET_I extends OneByteBytecode
    {
        public SET_I(int value) { super(value); }
        public SET_I(byte value) { super(value); }
        
        @Override protected byte inst() { return Instruction.SET_I; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 1; }
        @Override public String getName() { return "SET_I"; }
    }
    
    class ADD extends NoParsBytecode
    {
        public ADD() {}
        
        @Override protected byte inst() { return Instruction.ADD; }
        @Override protected int stack(boolean pars) { return pars ? 2 : 0; }
        @Override public String getName() { return "ADD"; }
    }
    
    class PROPERTY_GET extends OneWordRefBytecode
    {
        public PROPERTY_GET(int identifier) { super(identifier); }
        public PROPERTY_GET(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.PROPERTY_GET : Instruction.PROPERTY_GET16); }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override protected String getName(boolean is16) { return is16 ? "PROPERTY_GET" : "PROPERTY_GET16"; }
    }
    
    class PROPERTY_SET extends OneWordRefBytecode
    {
        public PROPERTY_SET(int identifier) { super(identifier); }
        public PROPERTY_SET(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.PROPERTY_SET : Instruction.PROPERTY_SET16); }
        @Override protected int stack(boolean pars) { return pars ? 2 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "PROPERTY_SET" : "PROPERTY_SET16"; }
    }
    
    class IF extends BranchBytecode
    {
        public IF(int identifier) { super(identifier); }
        public IF(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.IF : Instruction.IF16); }
        @Override protected int stack(boolean pars) { return pars ? 1 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "IF" : "IF16"; }
    }
    
    class IF_EQ extends BranchBytecode
    {
        public IF_EQ(int identifier) { super(identifier); }
        public IF_EQ(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.IF_EQ : Instruction.IF_EQ16); }
        @Override protected int stack(boolean pars) { return pars ? 2 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "IF_EQ" : "IF_EQ16"; }
    }
    
    class IF_NOEQ extends BranchBytecode
    {
        public IF_NOEQ(int identifier) { super(identifier); }
        public IF_NOEQ(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.IF_NOEQ : Instruction.IF_NOEQ16); }
        @Override protected int stack(boolean pars) { return pars ? 2 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "IF_NOEQ" : "IF_NOEQ16"; }
    }
    
    class IF_T_EQ extends BranchBytecode
    {
        public IF_T_EQ(int identifier) { super(identifier); }
        public IF_T_EQ(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.IF_T_EQ : Instruction.IF_T_EQ16); }
        @Override protected int stack(boolean pars) { return pars ? 2 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "IF_T_EQ" : "IF_T_EQ16"; }
    }
    
    class IF_TNEQ extends BranchBytecode
    {
        public IF_TNEQ(int identifier) { super(identifier); }
        public IF_TNEQ(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.IF_TNEQ : Instruction.IF_TNEQ16); }
        @Override protected int stack(boolean pars) { return pars ? 2 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "IF_TNEQ" : "IF_TNEQ16"; }
    }
    
    class IF_GR extends BranchBytecode
    {
        public IF_GR(int identifier) { super(identifier); }
        public IF_GR(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.IF_GR : Instruction.IF_GR16); }
        @Override protected int stack(boolean pars) { return pars ? 2 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "IF_GR" : "IF_GR16"; }
    }
    
    class IF_SM extends BranchBytecode
    {
        public IF_SM(int identifier) { super(identifier); }
        public IF_SM(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.IF_SM : Instruction.IF_SM16); }
        @Override protected int stack(boolean pars) { return pars ? 2 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "IF_SM" : "IF_SM16"; }
    }
    
    class IF_GREQ extends BranchBytecode
    {
        public IF_GREQ(int identifier) { super(identifier); }
        public IF_GREQ(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.IF_GREQ : Instruction.IF_GREQ16); }
        @Override protected int stack(boolean pars) { return pars ? 2 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "IF_GREQ" : "IF_GREQ16"; }
    }
    
    class IF_SMEQ extends BranchBytecode
    {
        public IF_SMEQ(int identifier) { super(identifier); }
        public IF_SMEQ(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.IF_SMEQ : Instruction.IF_SMEQ16); }
        @Override protected int stack(boolean pars) { return pars ? 2 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "IF_SMEQ" : "IF_SMEQ16"; }
    }
    
    class IF_NOT extends BranchBytecode
    {
        public IF_NOT(int identifier) { super(identifier); }
        public IF_NOT(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.IF_NOT : Instruction.IF_NOT16); }
        @Override protected int stack(boolean pars) { return pars ? 1 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "IF_NOT" : "IF_NOT16"; }
    }
    
    class IF_DEF extends BranchBytecode
    {
        public IF_DEF(int identifier) { super(identifier); }
        public IF_DEF(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.IF_DEF : Instruction.IF_DEF16); }
        @Override protected int stack(boolean pars) { return pars ? 1 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "IF_DEF" : "IF_DEF16"; }
    }
    
    class IF_UNDEF extends BranchBytecode
    {
        public IF_UNDEF(int identifier) { super(identifier); }
        public IF_UNDEF(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.IF_UNDEF : Instruction.IF_UNDEF16); }
        @Override protected int stack(boolean pars) { return pars ? 1 : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "IF_UNDEF" : "IF_UNDEF16"; }
    }
    
    class LOCAL_CALL extends ByteWordRefBytecode
    {
        public LOCAL_CALL(int argsCount, int funcIdx) { super(argsCount, funcIdx); }
        public LOCAL_CALL(byte b0, byte b1, byte b2) { super(b0, b1, b2); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.LOCAL_CALL : Instruction.LOCAL_CALL16); }
        @Override protected int stack(boolean pars) { return pars ? b0.getValue() : 1; }
        @Override protected String getName(boolean is16) { return is16 ? "LOCAL_CALL" : "LOCAL_CALL16"; }
    }
    
    class LOCAL_VCALL extends ByteWordRefBytecode
    {
        public LOCAL_VCALL(int argsCount, int funcIdx) { super(argsCount, funcIdx); }
        public LOCAL_VCALL(byte b0, byte b1, byte b2) { super(b0, b1, b2); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.LOCAL_VCALL : Instruction.LOCAL_VCALL16); }
        @Override protected int stack(boolean pars) { return pars ? b0.getValue() : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "LOCAL_VCALL" : "LOCAL_VCALL16"; }
    }
    
    class CALL extends OneByteBytecode
    {
        public CALL(int argsCount) { super(argsCount); }
        public CALL(byte b0) { super(b0); }
        
        @Override protected byte inst() { return (byte) Instruction.CALL; }
        @Override protected int stack(boolean pars) { return pars ? 1 + b0.getValue() : 1; }
        @Override public String getName() { return "CALL"; }
    }
    
    class VCALL extends OneByteBytecode
    {
        public VCALL(int argsCount) { super(argsCount); }
        public VCALL(byte b0) { super(b0); }
        
        @Override protected byte inst() { return (byte) Instruction.VCALL; }
        @Override protected int stack(boolean pars) { return pars ? 1 + b0.getValue() : 0; }
        @Override public String getName() { return "VCALL"; }
    }
    
    class INVOKE extends ByteWordRefBytecode
    {
        public INVOKE(int argsCount, int identifier) { super(argsCount, identifier); }
        public INVOKE(byte b0, byte b1, byte b2) { super(b0, b1, b2); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.INVOKE : Instruction.INVOKE16); }
        @Override protected int stack(boolean pars) { return pars ? 1 + b0.getValue() : 1; }
        @Override protected String getName(boolean is16) { return is16 ? "INVOKE" : "INVOKE16"; }
    }
    
    class VINVOKE extends ByteWordRefBytecode
    {
        public VINVOKE(int argsCount, int identifier) { super(argsCount, identifier); }
        public VINVOKE(byte b0, byte b1, byte b2) { super(b0, b1, b2); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.VINVOKE : Instruction.VINVOKE16); }
        @Override protected int stack(boolean pars) { return pars ? 1 + b0.getValue() : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "VINVOKE" : "VINVOKE16"; }
    }
    
    class LIBE_LOAD extends OneWordRefBytecode
    {
        public LIBE_LOAD(int libelement) { super(libelement); }
        public LIBE_LOAD(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.LIBE_LOAD : Instruction.LIBE_LOAD16); }
        @Override protected int stack(boolean pars) { return pars ? 0 : 1; }
        @Override protected String getName(boolean is16) { return is16 ? "LIBE_LOAD" : "LIBE_LOAD16"; }
    }
    
    class LIBE_GET extends OneWordRefBytecode
    {
        public LIBE_GET(int libelement) { super(libelement); }
        public LIBE_GET(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.LIBE_GET : Instruction.LIBE_GET16); }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override protected String getName(boolean is16) { return is16 ? "LIBE_GET" : "LIBE_GET16"; }
    }
    
    class LIBE_GET_I extends WordRefByteBytecode
    {
        public LIBE_GET_I(int libelement, int value) { super(libelement, value); }
        public LIBE_GET_I(byte b0, byte b1, byte b2) { super(b0, b1, b2); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.LIBE_GET_I : Instruction.LIBE_GET_I16); }
        @Override protected int stack(boolean pars) { return pars ? 0 : 1; }
        @Override protected String getName(boolean is16) { return is16 ? "LIBE_GET_I" : "LIBE_GET_I16"; }
    }
    
    class LIBE_P_GET extends NoParsBytecode
    {
        private final ReferenceValue libelement, property;
        
        public LIBE_P_GET(int libelement, int propertyIdentifier)
        {
            this.libelement = new ReferenceValue(libelement);
            this.property = new ReferenceValue(propertyIdentifier);
        }
        public LIBE_P_GET(byte b0, byte b1, byte b2, byte b3)
        {
            this.libelement = new ReferenceValue(b0, b1);
            this.property = new ReferenceValue(b2, b3);
        }
        
        @Override public int getExtraBytesCount() { return libelement.getByteCount() + property.getByteCount(); }
        @Override public int getExtraByte(int index)
        {
            switch(index)
            {
                default:
                case 0: return libelement.getByte0();
                case 1: return libelement.require2Bytes() ? libelement.getByte1() : property.getByte0();
                case 2: return libelement.require2Bytes() ? property.getByte0() : property.getByte1();
                case 3: return property.getByte1();
            }
        }
        @Override void buildDescription(ValueBuilder builder) { builder.append(libelement).append(property); }
        
        private int state()
        {
            var lib = libelement.require2Bytes();
            var pro = property.require2Bytes();
            return lib ? pro ? 3 : 1 : pro ? 2 : 0;
        }
        
        @Override protected byte inst()
        {
            switch(state())
            {
                default:
                case 0: return (byte) Instruction.LIBE_P_GET;
                case 1: return (byte) Instruction.LIBE_P16_GET;
                case 2: return (byte) Instruction.LIBE_P_GET16;
                case 3: return (byte) Instruction.LIBE_P16_GET16;
            }
        }
        @Override protected int stack(boolean pars) { return pars ? 0 : 1; }
        @Override public String getName()
        {
            switch(state())
            {
                default:
                case 0: return "LIBE_P_GET";
                case 1: return "LIBE_P16_GET";
                case 2: return "LIBE_P_GET16";
                case 3: return "LIBE_P16_GET16";
            }
        }
    }
    
    class LIBE_CALL extends ByteWordRefBytecode
    {
        public LIBE_CALL(int argsCount, int libelement) { super(argsCount, libelement); }
        public LIBE_CALL(byte b0, byte b1, byte b2) { super(b0, b1, b2); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.LIBE_CALL : Instruction.LIBE_CALL16); }
        @Override protected int stack(boolean pars) { return pars ? b0.getValue() : 1; }
        @Override protected String getName(boolean is16) { return is16 ? "LIBE_CALL" : "LIBE_CALL16"; }
    }
    
    class LIBE_VCALL extends ByteWordRefBytecode
    {
        public LIBE_VCALL(int argsCount, int libelement) { super(argsCount, libelement); }
        public LIBE_VCALL(byte b0, byte b1, byte b2) { super(b0, b1, b2); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.LIBE_VCALL : Instruction.LIBE_VCALL16); }
        @Override protected int stack(boolean pars) { return pars ? b0.getValue() : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "LIBE_VCALL" : "LIBE_VCALL16"; }
    }
    
    class LIBE_NEW extends ByteWordRefBytecode
    {
        public LIBE_NEW(int argsCount, int libelement) { super(argsCount, libelement); }
        public LIBE_NEW(byte b0, byte b1, byte b2) { super(b0, b1, b2); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.LIBE_NEW : Instruction.LIBE_NEW16); }
        @Override protected int stack(boolean pars) { return pars ? b0.getValue() : 1; }
        @Override protected String getName(boolean is16) { return is16 ? "LIBE_NEW" : "LIBE_NEW16"; }
    }
    
    class LIBE_VNEW extends ByteWordRefBytecode
    {
        public LIBE_VNEW(int argsCount, int libelement) { super(argsCount, libelement); }
        public LIBE_VNEW(byte b0, byte b1, byte b2) { super(b0, b1, b2); }
        
        @Override protected byte inst(boolean is16) { return (byte) (is16 ? Instruction.LIBE_VNEW : Instruction.LIBE_VNEW16); }
        @Override protected int stack(boolean pars) { return pars ? b0.getValue() : 0; }
        @Override protected String getName(boolean is16) { return is16 ? "LIBE_VNEW" : "LIBE_VNEW16"; }
    }
    
    class ARGS_TO_ARRAY extends TwoByteBytecode
    {
        public ARGS_TO_ARRAY(int varIndex, int offsetIndex) { super(varIndex, offsetIndex); }
        public ARGS_TO_ARRAY(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst() { return (byte) Instruction.ARGS_TO_ARRAY; }
        @Override protected int stack(boolean pars) { return pars ? 0 : 0; }
        @Override public String getName() { return "ARGS_TO_ARRAY"; }
    }
    
    class ARG_TO_VAR extends TwoByteBytecode
    {
        public ARG_TO_VAR(int varIndex, int argIndex) { super(varIndex, argIndex); }
        public ARG_TO_VAR(byte b0, byte b1) { super(b0, b1); }
        
        @Override protected byte inst() { return (byte) Instruction.ARG_TO_VAR; }
        @Override protected int stack(boolean pars) { return pars ? 0 : 1; }
        @Override public String getName() { return "ARG_TO_VAR"; }
    }
    
    class NEW extends OneByteBytecode
    {
        public NEW(int argsCount) { super(argsCount); }
        public NEW(byte b0) { super(b0); }
        
        @Override protected byte inst() { return (byte) Instruction.NEW; }
        @Override protected int stack(boolean pars) { return pars ? b0.getValue() : 1; }
        @Override public String getName() { return "NEW"; }
    }
    
    class VNEW extends OneByteBytecode
    {
        public VNEW(int argsCount) { super(argsCount); }
        public VNEW(byte b0) { super(b0); }
        
        @Override protected byte inst() { return (byte) Instruction.VNEW; }
        @Override protected int stack(boolean pars) { return pars ? b0.getValue() : 0; }
        @Override public String getName() { return "VNEW"; }
    }
    
    class SELF extends NoParsBytecode
    {
        @Override protected byte inst() { return (byte) Instruction.SELF; }
        @Override protected int stack(boolean pars) { return pars ? 0 : 1; }
        @Override public String getName() { return "SELF"; }
    }
    
    class CLASS extends NoParsBytecode
    {
        @Override protected byte inst() { return (byte) Instruction.CLASS; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "CLASS"; }
    }
    
    class BASE extends NoParsBytecode
    {
        @Override protected byte inst() { return (byte) Instruction.BASE; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 1; }
        @Override public String getName() { return "BASE"; }
    }
    
    class YIELD extends NoParsBytecode
    {
        @Override protected byte inst() { return (byte) Instruction.YIELD; }
        @Override protected int stack(boolean pars) { return pars ? 1 : 0; }
        @Override public String getName() { return "YIELD"; }
    }
    
    class YIELD_T extends NoParsBytecode
    {
        private final GSLDataType type;
        
        public YIELD_T(GSLDataType type) { this.type = Objects.requireNonNull(type); }
        public YIELD_T(int type) { this(i2t(type)); }
        public YIELD_T(byte type) { this(b2t(type)); }
        
        @Override protected byte inst()
        {
            switch(type)
            {
                default:
                case NULL: return (byte) Instruction.YIELD_NULL;
                case INTEGER: return (byte) Instruction.YIELD_INT;
                case FLOAT: return (byte) Instruction.YIELD_FLOAT;
                case BOOLEAN: return (byte) Instruction.YIELD_BOOL;
                case STRING: return (byte) Instruction.YIELD_STRING;
                case CONST_TUPLE: return (byte) Instruction.YIELD_CTUPLE;
                case CONST_MAP: return (byte) Instruction.YIELD_CMAP;
                case FUNCTION: return (byte) Instruction.YIELD_FUNCTION;
                case LIST: return (byte) Instruction.YIELD_LIST;
                case TUPLE: return (byte) Instruction.YIELD_TUPLE;
                case MAP: return (byte) Instruction.YIELD_MAP;
                case STRUCT: return (byte) Instruction.YIELD_STRUCT;
                case BLUEPRINT: return (byte) Instruction.YIELD_BPRINT;
                case OBJECT: return (byte) Instruction.YIELD_OBJECT;
                case ITERATOR: return (byte) Instruction.YIELD_ITERATOR;
                case RAW_BYTES: return (byte) Instruction.YIELD_BYTES;
                case NATIVE: return (byte) Instruction.YIELD_NATIVE;
            }
        }
        @Override protected int stack(boolean pars) { return pars ? (type == GSLDataType.NULL ? 0 : 1) : 0; }
        @Override public String getName()
        {
            switch(type)
            {
                default:
                case NULL: return "YIELD_NULL";
                case INTEGER: return "YIELD_INT";
                case FLOAT: return "YIELD_FLOAT";
                case BOOLEAN: return "YIELD_BOOL";
                case STRING: return "YIELD_STRING";
                case CONST_TUPLE: return "YIELD_CTUPLE";
                case CONST_MAP: return "YIELD_CMAP";
                case FUNCTION: return "YIELD_FUNCTION";
                case LIST: return "YIELD_LIST";
                case TUPLE: return "YIELD_TUPLE";
                case MAP: return "YIELD_MAP";
                case STRUCT: return "YIELD_STRUCT";
                case BLUEPRINT: return "YIELD_BPRINT";
                case OBJECT: return "YIELD_OBJECT";
                case ITERATOR: return "YIELD_ITERATOR";
                case RAW_BYTES: return "YIELD_BYTES";
                case NATIVE: return "YIELD_NATIVE";
            }
        }
    }
}
