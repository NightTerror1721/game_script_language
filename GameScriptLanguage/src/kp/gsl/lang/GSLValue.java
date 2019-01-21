/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Asus
 */
public abstract class GSLValue
{
    public abstract GSLDataType getGSLDataType();
    
    public final boolean isNull() { return getGSLDataType() == GSLDataType.NULL; }
    public final boolean isInteger() { return getGSLDataType() == GSLDataType.INTEGER; }
    public final boolean isFloat() { return getGSLDataType() == GSLDataType.FLOAT; }
    public final boolean isBoolean() { return getGSLDataType() == GSLDataType.BOOLEAN; }
    public final boolean isString() { return getGSLDataType() == GSLDataType.STRING; }
    public final boolean isVector() { return getGSLDataType() == GSLDataType.VECTOR; }
    public final boolean isTable() { return getGSLDataType() == GSLDataType.TABLE; }
    public final boolean isFunction() { return getGSLDataType() == GSLDataType.FUNCTION; }
    public final boolean isList() { return getGSLDataType() == GSLDataType.LIST; }
    public final boolean isTuple() { return getGSLDataType() == GSLDataType.TUPLE; }
    public final boolean isObject() { return getGSLDataType() == GSLDataType.OBJECT; }
    public final boolean isStruct() { return getGSLDataType() == GSLDataType.STRUCT; }
    public final boolean isDictionary() { return getGSLDataType() == GSLDataType.DICTIONARY; }
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
    public <V extends GSLValue> V cast() { return (V) this; }
    
    
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
    
    
    /* Object operators */
    public abstract GSLValue operatorGetProperty(String name);
    public abstract void     operatorSetProperty(String name, GSLValue value);
    public abstract GSLValue operatorCall(GSLValue self, GSLValue[] args);
    public final    GSLValue operatorCall(GSLValue self) { return operatorCall(self, SGSConstants.EMPTY_ARGS); }
    public abstract GSLValue operatorNew(GSLValue[] args);
    public final    GSLValue operatorNew() { return operatorNew(SGSConstants.EMPTY_ARGS); }
    
    
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
    
    
    
    /* Immutable Constant Values */
    public static final GSLImmutableValue ZERO = new GSLInteger(0);
    public static final GSLImmutableValue ONE = new GSLInteger(1);
    public static final GSLImmutableValue MINUSONE = new GSLInteger(-1);
    public static final GSLImmutableValue TRUE = GSLBoolean.TRUE_INSTANCE;
    public static final GSLImmutableValue FALSE = GSLBoolean.FALSE_INSTANCE;
    public static final GSLImmutableValue NULL = GSLNull.INSTANCE;
}
