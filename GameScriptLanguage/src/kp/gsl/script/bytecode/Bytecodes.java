/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.bytecode;

import kp.gsl.lang.GSLDataType;
import kp.gsl.script.bytecode.BytecodeImpls.ADD;
import kp.gsl.script.bytecode.BytecodeImpls.ARGS_TO_ARRAY;
import kp.gsl.script.bytecode.BytecodeImpls.ARG_TO_VAR;
import kp.gsl.script.bytecode.BytecodeImpls.BASE;
import kp.gsl.script.bytecode.BytecodeImpls.BLUEPRINT_NEW;
import kp.gsl.script.bytecode.BytecodeImpls.BW_AND;
import kp.gsl.script.bytecode.BytecodeImpls.BW_NOT;
import kp.gsl.script.bytecode.BytecodeImpls.BW_OR;
import kp.gsl.script.bytecode.BytecodeImpls.BW_SFH_L;
import kp.gsl.script.bytecode.BytecodeImpls.BW_SFH_R;
import kp.gsl.script.bytecode.BytecodeImpls.BW_XOR;
import kp.gsl.script.bytecode.BytecodeImpls.BYTES_NEW;
import kp.gsl.script.bytecode.BytecodeImpls.CALL;
import kp.gsl.script.bytecode.BytecodeImpls.CAST_T;
import kp.gsl.script.bytecode.BytecodeImpls.CLASS;
import kp.gsl.script.bytecode.BytecodeImpls.CONCAT;
import kp.gsl.script.bytecode.BytecodeImpls.DECREASE;
import kp.gsl.script.bytecode.BytecodeImpls.DIVIDE;
import kp.gsl.script.bytecode.BytecodeImpls.DUP;
import kp.gsl.script.bytecode.BytecodeImpls.DUP2;
import kp.gsl.script.bytecode.BytecodeImpls.EQ;
import kp.gsl.script.bytecode.BytecodeImpls.GET;
import kp.gsl.script.bytecode.BytecodeImpls.GET_I;
import kp.gsl.script.bytecode.BytecodeImpls.GOTO;
import kp.gsl.script.bytecode.BytecodeImpls.GR;
import kp.gsl.script.bytecode.BytecodeImpls.GREQ;
import kp.gsl.script.bytecode.BytecodeImpls.IF;
import kp.gsl.script.bytecode.BytecodeImpls.IF_DEF;
import kp.gsl.script.bytecode.BytecodeImpls.IF_EQ;
import kp.gsl.script.bytecode.BytecodeImpls.IF_GR;
import kp.gsl.script.bytecode.BytecodeImpls.IF_GREQ;
import kp.gsl.script.bytecode.BytecodeImpls.IF_NOEQ;
import kp.gsl.script.bytecode.BytecodeImpls.IF_NOT;
import kp.gsl.script.bytecode.BytecodeImpls.IF_SM;
import kp.gsl.script.bytecode.BytecodeImpls.IF_SMEQ;
import kp.gsl.script.bytecode.BytecodeImpls.IF_TNEQ;
import kp.gsl.script.bytecode.BytecodeImpls.IF_T_EQ;
import kp.gsl.script.bytecode.BytecodeImpls.IF_UNDEF;
import kp.gsl.script.bytecode.BytecodeImpls.INCREASE;
import kp.gsl.script.bytecode.BytecodeImpls.INVOKE;
import kp.gsl.script.bytecode.BytecodeImpls.IS_DEF;
import kp.gsl.script.bytecode.BytecodeImpls.IS_UNDEF;
import kp.gsl.script.bytecode.BytecodeImpls.ITERATOR;
import kp.gsl.script.bytecode.BytecodeImpls.ITERATOR_NEW;
import kp.gsl.script.bytecode.BytecodeImpls.LENGTH;
import kp.gsl.script.bytecode.BytecodeImpls.LIBE_CALL;
import kp.gsl.script.bytecode.BytecodeImpls.LIBE_GET;
import kp.gsl.script.bytecode.BytecodeImpls.LIBE_GET_I;
import kp.gsl.script.bytecode.BytecodeImpls.LIBE_LOAD;
import kp.gsl.script.bytecode.BytecodeImpls.LIBE_NEW;
import kp.gsl.script.bytecode.BytecodeImpls.LIBE_P_GET;
import kp.gsl.script.bytecode.BytecodeImpls.LIBE_VCALL;
import kp.gsl.script.bytecode.BytecodeImpls.LIBE_VNEW;
import kp.gsl.script.bytecode.BytecodeImpls.LIST_NEW;
import kp.gsl.script.bytecode.BytecodeImpls.LOAD_CLOSURE;
import kp.gsl.script.bytecode.BytecodeImpls.LOAD_CONST;
import kp.gsl.script.bytecode.BytecodeImpls.LOAD_FUNCTION;
import kp.gsl.script.bytecode.BytecodeImpls.LOAD_GLOBAL;
import kp.gsl.script.bytecode.BytecodeImpls.LOAD_NULL;
import kp.gsl.script.bytecode.BytecodeImpls.LOAD_REF;
import kp.gsl.script.bytecode.BytecodeImpls.LOAD_VAR;
import kp.gsl.script.bytecode.BytecodeImpls.LOCAL_CALL;
import kp.gsl.script.bytecode.BytecodeImpls.LOCAL_VCALL;
import kp.gsl.script.bytecode.BytecodeImpls.MAP_NEW;
import kp.gsl.script.bytecode.BytecodeImpls.MINUS;
import kp.gsl.script.bytecode.BytecodeImpls.MULTIPLY;
import kp.gsl.script.bytecode.BytecodeImpls.NEGATIVE;
import kp.gsl.script.bytecode.BytecodeImpls.NEW;
import kp.gsl.script.bytecode.BytecodeImpls.NOEQ;
import kp.gsl.script.bytecode.BytecodeImpls.NOOP;
import kp.gsl.script.bytecode.BytecodeImpls.NOT;
import kp.gsl.script.bytecode.BytecodeImpls.OBJECT_NEW;
import kp.gsl.script.bytecode.BytecodeImpls.PLUS;
import kp.gsl.script.bytecode.BytecodeImpls.POP;
import kp.gsl.script.bytecode.BytecodeImpls.PROPERTY_GET;
import kp.gsl.script.bytecode.BytecodeImpls.PROPERTY_SET;
import kp.gsl.script.bytecode.BytecodeImpls.REF_NEW;
import kp.gsl.script.bytecode.BytecodeImpls.REMAINDER;
import kp.gsl.script.bytecode.BytecodeImpls.RETURN;
import kp.gsl.script.bytecode.BytecodeImpls.RETURN_T;
import kp.gsl.script.bytecode.BytecodeImpls.SELF;
import kp.gsl.script.bytecode.BytecodeImpls.SET;
import kp.gsl.script.bytecode.BytecodeImpls.SET_I;
import kp.gsl.script.bytecode.BytecodeImpls.SM;
import kp.gsl.script.bytecode.BytecodeImpls.SMEQ;
import kp.gsl.script.bytecode.BytecodeImpls.STORE_GLOBAL;
import kp.gsl.script.bytecode.BytecodeImpls.STORE_REF;
import kp.gsl.script.bytecode.BytecodeImpls.STORE_VAR;
import kp.gsl.script.bytecode.BytecodeImpls.STRUCT_NEW;
import kp.gsl.script.bytecode.BytecodeImpls.ST_VAR_T;
import kp.gsl.script.bytecode.BytecodeImpls.SWAP;
import kp.gsl.script.bytecode.BytecodeImpls.SWAP2;
import kp.gsl.script.bytecode.BytecodeImpls.TUPLE_NEW;
import kp.gsl.script.bytecode.BytecodeImpls.TYPEID;
import kp.gsl.script.bytecode.BytecodeImpls.T_EQ;
import kp.gsl.script.bytecode.BytecodeImpls.T_NOEQ;
import kp.gsl.script.bytecode.BytecodeImpls.VCALL;
import kp.gsl.script.bytecode.BytecodeImpls.VINVOKE;
import kp.gsl.script.bytecode.BytecodeImpls.VNEW;
import kp.gsl.script.bytecode.BytecodeImpls.YIELD;
import kp.gsl.script.bytecode.BytecodeImpls.YIELD_T;


/**
 *
 * @author Asus
 */
public interface Bytecodes
{
    Bytecode NOOP = new NOOP();
    
    static Bytecode loadConst(int constIdx) { return new LOAD_CONST(constIdx); }
    static Bytecode loadVar(int varIdx) { return new LOAD_VAR(varIdx); }
    static Bytecode loadFunction(int funcIdx) { return new LOAD_FUNCTION(funcIdx); }
    static Bytecode loadClosure(int parsCount, int funcIdx) { return new LOAD_CLOSURE(parsCount, funcIdx); }
    static Bytecode loadGlobal(int identifier) { return new LOAD_GLOBAL(identifier); }
    Bytecode LOAD_NULL = new LOAD_NULL();
    
    static Bytecode storeGlobal(int identifier) { return new STORE_GLOBAL(identifier); }
    static Bytecode storeVar(int varIdx) { return storeVar(varIdx, null); }
    static Bytecode storeVar(int varIdx, GSLDataType type) { return type == null ? new STORE_VAR(varIdx) : new ST_VAR_T(varIdx, type); }
    
    Bytecode LIST_NEW = new LIST_NEW();
    Bytecode TUPLE_NEW = new TUPLE_NEW();
    Bytecode MAP_NEW = new MAP_NEW();
    Bytecode STRUCT_NEW = new STRUCT_NEW();
    Bytecode BLUEPRINT_NEW = new BLUEPRINT_NEW();
    Bytecode OBJECT_NEW = new OBJECT_NEW(0);
    Bytecode OBJECT_NEW_PARENT = new OBJECT_NEW(1);
    Bytecode ITERATOR_NEW = new ITERATOR_NEW();
    Bytecode BYTES_NEW = new BYTES_NEW();
    
    Bytecode CAST_INT = new CAST_T(GSLDataType.INTEGER);
    Bytecode CAST_FLOAT = new CAST_T(GSLDataType.FLOAT);
    Bytecode CAST_BOOL = new CAST_T(GSLDataType.BOOLEAN);
    Bytecode CAST_STRING = new CAST_T(GSLDataType.STRING);
    Bytecode CAST_CTUPLE = new CAST_T(GSLDataType.CONST_TUPLE);
    Bytecode CAST_CMAP = new CAST_T(GSLDataType.CONST_MAP);
    Bytecode CAST_FUNCTION = new CAST_T(GSLDataType.FUNCTION);
    Bytecode CAST_LIST = new CAST_T(GSLDataType.LIST);
    Bytecode CAST_TUPLE = new CAST_T(GSLDataType.TUPLE);
    Bytecode CAST_MAP = new CAST_T(GSLDataType.MAP);
    Bytecode CAST_STRUCT = new CAST_T(GSLDataType.STRUCT);
    Bytecode CAST_BPRINT = new CAST_T(GSLDataType.BLUEPRINT);
    Bytecode CAST_OBJECT = new CAST_T(GSLDataType.OBJECT);
    Bytecode CAST_ITERATOR = new CAST_T(GSLDataType.ITERATOR);
    Bytecode CAST_BYTES = new CAST_T(GSLDataType.RAW_BYTES);
    Bytecode CAST_NATIVE = new CAST_T(GSLDataType.NATIVE);
    static Bytecode cast(GSLDataType type)
    {
        switch(type)
        {
            case INTEGER: return CAST_INT;
            case FLOAT: return CAST_FLOAT;
            case BOOLEAN: return CAST_BOOL;
            case STRING: return CAST_STRING;
            case CONST_TUPLE: return CAST_CTUPLE;
            case CONST_MAP: return CAST_CMAP;
            case FUNCTION: return CAST_FUNCTION;
            case LIST: return CAST_LIST;
            case TUPLE: return CAST_TUPLE;
            case MAP: return CAST_MAP;
            case STRUCT: return CAST_STRUCT;
            case BLUEPRINT: return CAST_BPRINT;
            case OBJECT: return CAST_OBJECT;
            case ITERATOR: return CAST_ITERATOR;
            case RAW_BYTES: return CAST_BYTES;
            case NATIVE: return CAST_NATIVE;
            default:
                throw new IllegalArgumentException();
        }
    }
    
    static Bytecode refNew(int varIdx) { return new REF_NEW(varIdx); }
    Bytecode LOAD_REF = new LOAD_REF();
    Bytecode STORE_REF = new STORE_REF();
    
    Bytecode POP = new POP();
    Bytecode SWAP = new SWAP();
    Bytecode SWAP2 = new SWAP2();
    Bytecode DUP = new DUP();
    Bytecode DUP2 = new DUP2();
    
    static Bytecode goTo(int instIdx) { return new GOTO(instIdx); }
    static Bytecode goTo() { return new GOTO(0); }
    
    Bytecode RETURN = new RETURN();
    static Bytecode Return(GSLDataType type) { return type == null ? RETURN : new RETURN_T(type); }
    
    Bytecode PLUS = new PLUS();
    Bytecode MINUS = new MINUS();
    Bytecode MULTIPLY = new MULTIPLY();
    Bytecode DIVIDE = new DIVIDE();
    Bytecode REMAINDER = new REMAINDER();
    Bytecode NEGATIVE = new NEGATIVE();
    Bytecode INCREASE = new INCREASE();
    Bytecode DECREASE = new DECREASE();
    
    Bytecode BW_SFH_L = new BW_SFH_L();
    Bytecode BW_SFH_R = new BW_SFH_R();
    Bytecode BW_AND = new BW_AND();
    Bytecode BW_OR = new BW_OR();
    Bytecode BW_XOR = new BW_XOR();
    Bytecode BW_NOT = new BW_NOT();
    
    Bytecode EQ = new EQ();
    Bytecode NOEQ = new NOEQ();
    Bytecode T_EQ = new T_EQ();
    Bytecode T_NOEQ = new T_NOEQ();
    Bytecode GR = new GR();
    Bytecode SM = new SM();
    Bytecode GREQ = new GREQ();
    Bytecode SMEQ = new SMEQ();
    Bytecode IS_DEF = new IS_DEF();
    Bytecode IS_UNDEF = new IS_UNDEF();
    Bytecode NOT = new NOT();
    Bytecode CONCAT = new CONCAT();
    Bytecode LENGTH = new LENGTH();
    Bytecode TYPEID = new TYPEID();
    Bytecode ITERATOR = new ITERATOR();
    
    Bytecode GET = new GET();
    static Bytecode get(int localValue) { return new GET_I(localValue); }
    Bytecode SET = new SET();
    static Bytecode set(int localValue) { return new SET_I(localValue); }
    Bytecode ADD = new ADD();
    
    static Bytecode propertyGet(int identifier) { return new PROPERTY_GET(identifier); }
    static Bytecode propertySet(int identifier) { return new PROPERTY_SET(identifier); }
    
    static Bytecode If(int instIdx) { return new IF(instIdx); }
    
    static Bytecode ifEq(int instIdx) { return new IF_EQ(instIdx); }
    static Bytecode ifNoeq(int instIdx) { return new IF_NOEQ(instIdx); }
    static Bytecode ifTEq(int instIdx) { return new IF_T_EQ(instIdx); }
    static Bytecode ifTNeq(int instIdx) { return new IF_TNEQ(instIdx); }
    static Bytecode ifGr(int instIdx) { return new IF_GR(instIdx); }
    static Bytecode ifSm(int instIdx) { return new IF_SM(instIdx); }
    static Bytecode ifGreq(int instIdx) { return new IF_GREQ(instIdx); }
    static Bytecode ifSmeq(int instIdx) { return new IF_SMEQ(instIdx); }
    static Bytecode ifNot(int instIdx) { return new IF_NOT(instIdx); }
    
    static Bytecode ifDef(int instIdx) { return new IF_DEF(instIdx); }
    static Bytecode ifUndef(int instIdx) { return new IF_UNDEF(instIdx); }
    
    static Bytecode localCall(boolean isVoid, int argsCount, int funcIdx) { return isVoid ? new LOCAL_VCALL(argsCount, funcIdx) : new LOCAL_CALL(argsCount, funcIdx); }
    
    static Bytecode call(boolean isVoid, int argsCount) { return isVoid ? new VCALL(argsCount) : new CALL(argsCount); }
    
    static Bytecode invoke(boolean isVoid, int argsCount, int identifier) { return isVoid ? new VINVOKE(argsCount, identifier) : new INVOKE(argsCount, identifier); }
    
    static Bytecode libeLoad(int libelement) { return new LIBE_LOAD(libelement); }
    static Bytecode libeGet(int libelement) { return new LIBE_GET(libelement); }
    static Bytecode libeGet(int libelement, int localValue) { return new LIBE_GET_I(libelement, localValue); }
    static Bytecode libePGet(int libelement, int propertyIdentifier) { return new LIBE_P_GET(libelement, propertyIdentifier); }
    static Bytecode libeCall(boolean isVoid, int argsCount, int libelement) { return isVoid ? new LIBE_VCALL(argsCount, libelement) : new LIBE_CALL(argsCount, libelement); }
    static Bytecode libeNew(boolean isVoid, int argsCount, int libelement) { return isVoid ? new LIBE_VNEW(argsCount, libelement) : new LIBE_NEW(argsCount, libelement); }
    
    static Bytecode argsToArray(int varIdx, int offsetIdx) { return new ARGS_TO_ARRAY(varIdx, offsetIdx); }
    static Bytecode argToVar(int varIdx, int argIdx) { return new ARG_TO_VAR(varIdx, argIdx); }
    
    static Bytecode New(boolean isVoid, int argsCount) { return isVoid ? new VNEW(argsCount) : new NEW(argsCount); }
    Bytecode SELF = new SELF();
    Bytecode CLASS = new CLASS();
    Bytecode BASE = new BASE();
    
    Bytecode YIELD = new YIELD();
    static Bytecode yield(GSLDataType type) { return type == null ? YIELD : new YIELD_T(type); }
}
