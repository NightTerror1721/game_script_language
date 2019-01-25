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
public final class GSLFloat extends GSLImmutableValue
{
    final double number;
    
    public GSLFloat(double value) { this.number = value; }
    
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.FLOAT; }

    @Override
    public final int intValue() { return (int) number; }

    @Override
    public final long longValue() { return (long) number; }

    @Override
    public final float floatValue() { return (float) number; }

    @Override
    public final double doubleValue() { return number; }
    
    @Override
    public final char charValue() { return (char) number; }

    @Override
    public final boolean boolValue() { return number != 0d; }

    @Override
    public final String toString() { return Double.toString(number); }

    @Override
    public final GSLValue[] toArray() { return Utils.arrayOf(this); }

    @Override
    public final List<GSLValue> toList() { return Utils.listOf(this); }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return Utils.mapOf(this); }
    
    @Override
    public final Stream<GSLValue> stream() { return Stream.of(this); }
    
    @Override
    public final GSLFloat cast() { return this; }

    @Override public final GSLValue operatorEquals(GSLValue value) { return number == value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return number != value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorGreater(GSLValue value) { return number > value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorSmaller(GSLValue value) { return number < value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorGreaterEquals(GSLValue value) { return number >= value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorSmallerEquals(GSLValue value) { return number <= value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorNegate() { return number == 0d ? TRUE : FALSE; }
    @Override public final int      operatorLength() { return 1; }

    @Override public final GSLValue operatorPlus(GSLValue value) { return new GSLFloat(number + value.doubleValue()); }
    @Override public final GSLValue operatorMinus(GSLValue value) { return new GSLFloat(number - value.doubleValue()); }
    @Override public final GSLValue operatorMultiply(GSLValue value) { return new GSLFloat(number * value.doubleValue()); }
    @Override public final GSLValue operatorDivide(GSLValue value) { return new GSLFloat(number / value.doubleValue()); }
    @Override public final GSLValue operatorRemainder(GSLValue value) { return new GSLInteger((long) number % value.longValue()); }
    @Override public final GSLValue operatorIncrease() { return new GSLFloat(number + 1); }
    @Override public final GSLValue operatorDecrease() { return new GSLFloat(number - 1); }
    @Override public final GSLValue operatorNegative() { return new GSLFloat(-number); }

    @Override public final GSLValue operatorBitwiseShiftLeft(GSLValue value) { return new GSLInteger((long) number >> value.longValue()); }
    @Override public final GSLValue operatorBitwiseShiftRight(GSLValue value) { return new GSLInteger((long) number << value.longValue()); }
    @Override public final GSLValue operatorBitwiseAnd(GSLValue value) { return new GSLInteger((long) number & value.longValue()); }
    @Override public final GSLValue operatorBitwiseOr(GSLValue value) { return new GSLInteger((long) number | value.longValue()); }
    @Override public final GSLValue operatorBitwiseXor(GSLValue value) { return new GSLInteger((long) number ^ value.longValue()); }
    @Override public final GSLValue operatorBitwiseNot() { return new GSLInteger(~((long) number)); }

    @Override public final GSLValue operatorGet(GSLValue index) { throw new UnsupportedOperatorException(this, "[]"); }

    @Override
    public GSLValue operatorGetProperty(String name)
    {
        switch(name)
        {
            default: return NULL;
            case "int": case "integer": return new GSLInteger((long) number);
            case "float": return this;
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
        if(o instanceof GSLFloat)
            return number == ((GSLFloat) o).number;
        if(o instanceof GSLInteger)
            return number == ((GSLInteger) o).number;
        if(o instanceof GSLBoolean)
            return number == ((GSLBoolean) o).doubleValue();
        return false;
    }
    @Override public final int hashCode() { return Double.hashCode(number); }
}
