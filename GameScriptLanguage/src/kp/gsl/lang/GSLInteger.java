/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kp.gsl.exception.UnsupportedOperatorException;

/**
 *
 * @author Asus
 */
public final class GSLInteger extends GSLImmutableValue
{
    final long number;
    
    public GSLInteger(long value) { this.number = value; }
    
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.INTEGER; }

    @Override
    public final int intValue() { return (int) number; }

    @Override
    public final long longValue() { return number; }

    @Override
    public final float floatValue() { return number; }

    @Override
    public final double doubleValue() { return number; }
    
    @Override
    public final char charValue() { return (char) number; }

    @Override
    public final boolean boolValue() { return number != 0; }

    @Override
    public final String toString() { return Long.toString(number); }

    @Override
    public final GSLValue[] toArray() { return Utils.arrayOf(this); }

    @Override
    public final List<GSLValue> toList() { return Utils.listOf(this); }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return Utils.mapOf(this); }
    
    @Override
    public final Stream<GSLValue> stream() { return Stream.of(this); }
    
    @Override
    public final GSLInteger cast() { return this; }
    
    
    @Override public final GSLInteger    operatorCastInteger() { return this; }
    @Override public final GSLFloat      operatorCastFloat() { return new GSLFloat(number); }
    @Override public final GSLBoolean    operatorCastBoolean() { return number == 0 ? FALSE : TRUE; }
    @Override public final GSLString     operatorCastString() { return new GSLString(Long.toString(number)); }
    @Override public final GSLConstTuple operatorCastConstTuple() { return new GSLConstTuple(new GSLImmutableValue[] { this }); }
    @Override public final GSLConstMap   operatorCastConstMap() { return new GSLConstMap(Utils.constMapOf(this)); }
    @Override public final GSLFunction   operatorCastFunction() { return Utils.autoGetter(this); }
    @Override public final GSLList       operatorCastList() { return new GSLList(Utils.listOf(this)); }
    @Override public final GSLTuple      operatorCastTuple() { return new GSLTuple(new GSLValue[] { this }); }
    @Override public final GSLMap        operatorCastMap() { return new GSLMap(Utils.mapOf(this)); }
    @Override public final GSLStruct     operatorCastStruct() { return Utils.structOf(this); }
    @Override public final GSLBlueprint  operatorCastBlueprint() { return Utils.blueprintOf(this); }
    @Override public final GSLObject     operatorCastObject() { return Utils.objectOf(this); }
    @Override public final GSLIterator   operatorCastIterator() { return Utils.oneIter(this); }
    @Override public final GSLRawBytes   operatorCastRawBytes()
    {
        var bytes = new GSLRawBytes(new byte[8]);
        bytes.setLong(0, number);
        return bytes;
    }
    

    @Override public final GSLValue operatorEquals(GSLValue value) { return number == value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return number != value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorGreater(GSLValue value) { return number > value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorSmaller(GSLValue value) { return number < value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorGreaterEquals(GSLValue value) { return number >= value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorSmallerEquals(GSLValue value) { return number <= value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorNegate() { return number == 0 ? TRUE : FALSE; }
    @Override public final int      operatorLength() { return 1; }

    @Override public final GSLValue operatorPlus(GSLValue value) { return new GSLFloat(number + value.doubleValue()); }
    @Override public final GSLValue operatorMinus(GSLValue value) { return new GSLFloat(number - value.doubleValue()); }
    @Override public final GSLValue operatorMultiply(GSLValue value) { return new GSLFloat(number * value.doubleValue()); }
    @Override public final GSLValue operatorDivide(GSLValue value) { return new GSLFloat(number / value.doubleValue()); }
    @Override public final GSLValue operatorRemainder(GSLValue value) { return new GSLInteger(number % value.longValue()); }
    @Override public final GSLValue operatorIncrease() { return new GSLInteger(number + 1); }
    @Override public final GSLValue operatorDecrease() { return new GSLInteger(number - 1); }
    @Override public final GSLValue operatorNegative() { return new GSLInteger(-number); }

    @Override public final GSLValue operatorBitwiseShiftLeft(GSLValue value) { return new GSLInteger(number >> value.longValue()); }
    @Override public final GSLValue operatorBitwiseShiftRight(GSLValue value) { return new GSLInteger(number << value.longValue()); }
    @Override public final GSLValue operatorBitwiseAnd(GSLValue value) { return new GSLInteger(number & value.longValue()); }
    @Override public final GSLValue operatorBitwiseOr(GSLValue value) { return new GSLInteger(number | value.longValue()); }
    @Override public final GSLValue operatorBitwiseXor(GSLValue value) { return new GSLInteger(number ^ value.longValue()); }
    @Override public final GSLValue operatorBitwiseNot() { return new GSLInteger(~number); }

    @Override public final GSLValue operatorGet(GSLValue index) { throw new UnsupportedOperatorException(this, "[]"); }

    @Override
    public GSLValue operatorGetProperty(String name)
    {
        switch(name)
        {
            default: return NULL;
            case "int": case "integer": return this;
            case "float": return new GSLFloat(number);
        }
    }

    @Override
    public final GSLValue operatorCall(GSLValue self, GSLVarargs args) { throw new UnsupportedOperatorException(this, "()"); }
    
    @Override
    public final GSLValue operatorNew(GSLVarargs args) { throw new UnsupportedOperatorException(this, "new"); }

    @Override
    public final GSLValue operatorIterator() { return Utils.oneIter(this); }
    @Override public final boolean hasNext() { throw new UnsupportedOperatorException(this, "hasNext"); }
    @Override public final GSLValue next() { throw new UnsupportedOperatorException(this, "next"); }

    @Override public final boolean equals(Object o)
    {
        if(o instanceof GSLInteger)
            return number == ((GSLInteger) o).number;
        if(o instanceof GSLFloat)
            return number == ((GSLFloat) o).number;
        if(o instanceof GSLBoolean)
            return number == ((GSLBoolean) o).doubleValue();
        return false;
    }
    @Override public final int hashCode() { return Long.hashCode(number); }
}
