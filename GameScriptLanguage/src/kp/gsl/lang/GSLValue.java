/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kp.gsl.lib.Def;

/**
 *
 * @author Asus
 */
public abstract class GSLValue extends GSLVarargs
{
    public abstract GSLDataType getGSLDataType();
    
    public final boolean isNull() { return getGSLDataType() == GSLDataType.NULL; }
    public final boolean isInteger() { return getGSLDataType() == GSLDataType.INTEGER; }
    public final boolean isFloat() { return getGSLDataType() == GSLDataType.FLOAT; }
    public final boolean isNumber() { return getGSLDataType().isNumber(); }
    public final boolean isBoolean() { return getGSLDataType() == GSLDataType.BOOLEAN; }
    public final boolean isString() { return getGSLDataType() == GSLDataType.STRING; }
    public final boolean isConstTuple() { return getGSLDataType() == GSLDataType.CONST_TUPLE; }
    public final boolean isConstMap() { return getGSLDataType() == GSLDataType.CONST_MAP; }
    public final boolean isFunction() { return getGSLDataType() == GSLDataType.FUNCTION; }
    public final boolean isList() { return getGSLDataType() == GSLDataType.LIST; }
    public final boolean isTuple() { return getGSLDataType() == GSLDataType.TUPLE; }
    public final boolean isObject() { return getGSLDataType() == GSLDataType.OBJECT; }
    public final boolean isStruct() { return getGSLDataType() == GSLDataType.STRUCT; }
    public final boolean isBlueprint() { return getGSLDataType() == GSLDataType.BLUEPRINT; }
    public final boolean isIterator() { return getGSLDataType() == GSLDataType.ITERATOR; }
    public final boolean isRawBytes() { return getGSLDataType() == GSLDataType.RAW_BYTES; }
    public final boolean isNative() { return getGSLDataType() == GSLDataType.NATIVE; }
    
    public abstract boolean isMutable();
    
    public abstract int intValue();
    public abstract long longValue();
    public abstract float floatValue();
    public abstract double doubleValue();
    public abstract char charValue();
    public abstract boolean boolValue();
    
    @Override
    public abstract String toString();
    public abstract GSLValue[] toArray();
    public abstract List<GSLValue> toList();
    public abstract Map<GSLValue, GSLValue> toMap();
    public abstract Stream<GSLValue> stream();
    public <V extends GSLValue> V cast() { return (V) this; }
    
    
    /* Operator Casts */
    public abstract GSLInteger    operatorCastInteger();
    public abstract GSLFloat      operatorCastFloat();
    public abstract GSLBoolean    operatorCastBoolean();
    public abstract GSLString     operatorCastString();
    public abstract GSLConstTuple operatorCastConstTuple();
    public abstract GSLConstMap   operatorCastConstMap();
    public abstract GSLFunction   operatorCastFunction();
    public abstract GSLList       operatorCastList();
    public abstract GSLTuple      operatorCastTuple();
    public abstract GSLMap        operatorCastMap();
    public abstract GSLStruct     operatorCastStruct();
    public abstract GSLBlueprint  operatorCastBlueprint();
    public abstract GSLObject     operatorCastObject();
    public abstract GSLIterator   operatorCastIterator();
    public abstract GSLRawBytes   operatorCastRawBytes();
    public final    GSLNative     operatorCastNative() { return cast(); }
    
    
    /* Common operators */
    public abstract GSLValue operatorEquals(GSLValue value);
    public abstract GSLValue operatorNotEquals(GSLValue value);
    public abstract GSLValue operatorGreater(GSLValue value);
    public abstract GSLValue operatorSmaller(GSLValue value);
    public abstract GSLValue operatorGreaterEquals(GSLValue value);
    public abstract GSLValue operatorSmallerEquals(GSLValue value);
    public abstract GSLValue operatorNegate();
    public abstract int      operatorLength();
    
    
    /* Math operators */
    public abstract GSLValue operatorPlus(GSLValue value);
    public abstract GSLValue operatorMinus(GSLValue value);
    public abstract GSLValue operatorMultiply(GSLValue value);
    public abstract GSLValue operatorDivide(GSLValue value);
    public abstract GSLValue operatorRemainder(GSLValue value);
    public abstract GSLValue operatorIncrease();
    public abstract GSLValue operatorDecrease();
    public abstract GSLValue operatorNegative();
    
    
    /* Bit operators */
    public abstract GSLValue operatorBitwiseShiftLeft(GSLValue value);
    public abstract GSLValue operatorBitwiseShiftRight(GSLValue value);
    public abstract GSLValue operatorBitwiseAnd(GSLValue value);
    public abstract GSLValue operatorBitwiseOr(GSLValue value);
    public abstract GSLValue operatorBitwiseXor(GSLValue value);
    public abstract GSLValue operatorBitwiseNot();
    
    
    /* Array operators */
    public abstract GSLValue operatorGet(GSLValue index);
    public abstract void     operatorSet(GSLValue index, GSLValue value);
    public          GSLValue operatorGet(int index) { return operatorGet(new GSLInteger(index)); }
    public          void     operatorSet(int index, GSLValue value) { operatorSet(new GSLInteger(index), value); }
    public abstract void     operatorAdd(GSLValue value);
    
    
    /* Object operators */
    public abstract GSLValue operatorGetProperty(String name);
    public abstract void     operatorSetProperty(String name, GSLValue value);
    public abstract void     operatorDelProperty(String name);
    public abstract GSLValue operatorCall(GSLValue self, GSLVarargs args);
    public final    GSLValue operatorCall(GSLValue self) { return operatorCall(self, NO_ARGS); }
    public final    GSLValue operatorCall(GSLValue self, GSLValue arg0) { return operatorCall(self, (GSLVarargs) arg0); }
    public final    GSLValue operatorCall(GSLValue self, GSLValue arg0, GSLValue arg1) { return operatorCall(self, new Pair(arg0, arg1)); }
    public final    GSLValue operatorCall(GSLValue self, GSLValue arg0, GSLValue arg1, GSLValue arg2) { return operatorCall(self, new Array(arg0, arg1, arg2)); }
    public final    GSLValue operatorCall(GSLValue self, GSLValue arg0, GSLValue arg1, GSLValue arg2, GSLValue arg3) { return operatorCall(self, new Array(arg0, arg1, arg2, arg3)); }
    public abstract GSLValue operatorNew(GSLVarargs args);
    public final    GSLValue operatorNew() { return operatorNew(NO_ARGS); }
    public final    GSLValue operatorNew(GSLValue self, GSLValue arg0) { return operatorNew((GSLVarargs) arg0); }
    public final    GSLValue operatorNew(GSLValue self, GSLValue arg0, GSLValue arg1) { return operatorNew(new Pair(arg0, arg1)); }
    public final    GSLValue operatorNew(GSLValue self, GSLValue arg0, GSLValue arg1, GSLValue arg2) { return operatorNew(new Array(arg0, arg1, arg2)); }
    public final    GSLValue operatorNew(GSLValue self, GSLValue arg0, GSLValue arg1, GSLValue arg2, GSLValue arg3) { return operatorNew(new Array(arg0, arg1, arg2, arg3)); }
    
    
    /* Pointer operators */
    public abstract GSLValue operatorReferenceGet();
    public abstract void     operatorReferenceSet(GSLValue value);
    
    
    /* Iterator operators */
    public abstract GSLValue operatorIterator();
    public abstract boolean  hasNext();
    public abstract GSLValue next();
    
    
    
    /* Java Functions */
    @Override public abstract boolean equals(Object o);
    @Override public abstract int hashCode();
    
    protected final int superHashCode() { return super.hashCode(); }
    
    public final GSLValue operatorTypedEquals(GSLValue value)
    {
        if(value.getGSLDataType() != value.getGSLDataType())
            return FALSE;
        return operatorEquals(value);
    }
    public final GSLValue operatorTypedNotEquals(GSLValue value)
    {
        if(value.getGSLDataType() != value.getGSLDataType())
            return TRUE;
        return operatorNotEquals(value);
    }
    
    
    @Override public final int numberOfArguments() { return 1; }
    @Override public final GSLValue arg0() { return this; }
    @Override public final GSLValue arg1() { return NULL; }
    @Override public final GSLValue arg(int index) { return index == 0 ? this : NULL; }
    
    
    
    /* Immutable Constant Values */
    public static final GSLInteger ZERO = new GSLInteger(0);
    public static final GSLInteger ONE = new GSLInteger(1);
    public static final GSLInteger MINUSONE = new GSLInteger(-1);
    public static final GSLBoolean TRUE = GSLBoolean.TRUE_INSTANCE;
    public static final GSLBoolean FALSE = GSLBoolean.FALSE_INSTANCE;
    public static final GSLString EMPTY_STRING = new GSLString("");
    public static final GSLConstTuple EMPTY_TUPLE = new GSLConstTuple(new GSLImmutableValue[] {});
    public static final GSLConstMap EMPTY_MAP = new GSLConstMap(Collections.emptyMap());
    public static final GSLFunction EMPTY_FUNCTION = Def.voidFunction((args) -> {});
    public static final GSLNull NULL = GSLNull.INSTANCE;
    
    
    /* ValueOf */
    
    public static final GSLValue valueOf(GSLValue value) { return value; }
    
    public static final GSLValue valueOf(byte value) { return new GSLInteger(value); }
    public static final GSLValue valueOf(short value) { return new GSLInteger(value); }
    public static final GSLValue valueOf(int value) { return new GSLInteger(value); }
    public static final GSLValue valueOf(long value) { return new GSLInteger(value); }
    public static final GSLValue valueOf(float value) { return new GSLFloat(value); }
    public static final GSLValue valueOf(double value) { return new GSLFloat(value); }
    public static final GSLValue valueOf(char value) { return new GSLInteger(value); }
    public static final GSLValue valueOf(boolean value) { return value ? TRUE : FALSE; }
    
    public static final GSLValue valueOf(Byte value) { return new GSLInteger(value); }
    public static final GSLValue valueOf(Short value) { return new GSLInteger(value); }
    public static final GSLValue valueOf(Integer value) { return new GSLInteger(value); }
    public static final GSLValue valueOf(Long value) { return new GSLInteger(value); }
    public static final GSLValue valueOf(Float value) { return new GSLFloat(value); }
    public static final GSLValue valueOf(Double value) { return new GSLFloat(value); }
    public static final GSLValue valueOf(Character value) { return new GSLInteger(value); }
    public static final GSLValue valueOf(Boolean value) { return value ? TRUE : FALSE; }
    
    public static final GSLValue valueOf(String value) { return new GSLString(value); }
    
    
    public static final GSLValue valueOf(GSLValue[] value) { return new GSLTuple(value); }
    
    public static final GSLValue valueOf(byte[] value) { return new GSLRawBytes(value); }
    public static final GSLValue valueOf(short[] value)
    {
        var array = new GSLValue[value.length];
        for(var i = 0; i < array.length; i++)
            array[i] = valueOf(value[i]);
        return valueOf(array);
    }
    public static final GSLValue valueOf(int[] value) { return valueOf(Arrays.stream(value).mapToObj(GSLValue::valueOf).toArray(GSLValue[]::new)); }
    public static final GSLValue valueOf(long[] value) { return valueOf(Arrays.stream(value).mapToObj(GSLValue::valueOf).toArray(GSLValue[]::new)); }
    public static final GSLValue valueOf(float[] value)
    {
        var array = new GSLValue[value.length];
        for(var i = 0; i < array.length; i++)
            array[i] = valueOf(value[i]);
        return valueOf(array);
    }
    public static final GSLValue valueOf(double[] value) { return valueOf(Arrays.stream(value).mapToObj(GSLValue::valueOf).toArray(GSLValue[]::new)); }
    public static final GSLValue valueOf(char[] value)
    {
        var array = new GSLValue[value.length];
        for(var i = 0; i < array.length; i++)
            array[i] = valueOf(value[i]);
        return valueOf(array);
    }
    public static final GSLValue valueOf(boolean[] value)
    {
        var array = new GSLValue[value.length];
        for(var i = 0; i < array.length; i++)
            array[i] = valueOf(value[i]);
        return valueOf(array);
    }
    
    public static final GSLValue valueOf(Byte[] value) { return valueOf(Arrays.stream(value).map(GSLValue::valueOf).toArray(GSLValue[]::new)); }
    public static final GSLValue valueOf(Short[] value) { return valueOf(Arrays.stream(value).map(GSLValue::valueOf).toArray(GSLValue[]::new)); }
    public static final GSLValue valueOf(Integer[] value) { return valueOf(Arrays.stream(value).map(GSLValue::valueOf).toArray(GSLValue[]::new)); }
    public static final GSLValue valueOf(Long[] value) { return valueOf(Arrays.stream(value).map(GSLValue::valueOf).toArray(GSLValue[]::new)); }
    public static final GSLValue valueOf(Float[] value) { return valueOf(Arrays.stream(value).map(GSLValue::valueOf).toArray(GSLValue[]::new)); }
    public static final GSLValue valueOf(Double[] value) { return valueOf(Arrays.stream(value).map(GSLValue::valueOf).toArray(GSLValue[]::new)); }
    public static final GSLValue valueOf(Character[] value) { return valueOf(Arrays.stream(value).map(GSLValue::valueOf).toArray(GSLValue[]::new)); }
    public static final GSLValue valueOf(Boolean[] value) { return valueOf(Arrays.stream(value).map(GSLValue::valueOf).toArray(GSLValue[]::new)); }
    
    public static final GSLValue valueOf(String[] value) { return valueOf(Arrays.stream(value).map(GSLValue::valueOf).toArray(GSLValue[]::new)); }
    
    public static final GSLValue valueOf(GSLValue[][] value) { return valueOf(Arrays.stream(value).map(GSLValue::valueOf).toArray(GSLValue[]::new)); }
    
    
    public static final GSLValue valueOf(List<GSLValue> value) { return new GSLList(value); }
    public static final GSLValue valueOf(List<GSLValue> value, boolean isTuple) { return isTuple ? new GSLTuple(value.toArray(GSLValue[]::new)) : new GSLList(value); }
    
    
    public static final GSLValue valueOf(Map<GSLValue, GSLValue> value) { return new GSLMap(value); }
    
    
    public static final <T extends GSLValue> GSLValue valueOf(Iterator<T> value) { return Def.iterator(value); }
    
    
    /* Special functions */
    
    public static final Object toJavaValue(GSLValue value)
    {
        if(value instanceof GSLReference)
            return toJavaValue(value.operatorReferenceGet());
        
        switch(value.getGSLDataType())
        {
            case NULL: return null;
            case INTEGER: return value.<GSLInteger>cast().number;
            case FLOAT: return value.<GSLFloat>cast().number;
            case BOOLEAN: return value.<GSLBoolean>cast().bool;
            case STRING: return value.<GSLString>cast().string;
            case CONST_TUPLE: return value.toArray();
            case CONST_MAP: return value.toMap();
            case FUNCTION: return value;
            case LIST: return value.<GSLList>cast().list;
            case TUPLE: return value.<GSLTuple>cast().tuple;
            case MAP: return value.<GSLMap>cast().map;
            case STRUCT: return value;
            case BLUEPRINT: return value;
            case OBJECT: return value;
            case ITERATOR: return value.operatorIterator();
            case RAW_BYTES: return value.<GSLRawBytes>cast().bytes;
            case NATIVE: return value;
            default: return value;
        }
    }
}
