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
public final class GSLBoolean extends GSLImmutableValue
{
    static final GSLBoolean TRUE_INSTANCE = new GSLBoolean(true);
    static final GSLBoolean FALSE_INSTANCE = new GSLBoolean(false);
    
    private final boolean bool;
    
    private GSLBoolean(boolean bool) { this.bool = bool; }
    
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.BOOLEAN; }

    @Override
    public final int intValue() { return bool ? 1 : 0; }

    @Override
    public final long longValue() { return bool ? 1 : 0; }

    @Override
    public final float floatValue() { return bool ? 1 : 0; }

    @Override
    public final double doubleValue() { return bool ? 1 : 0; }
    
    @Override
    public final char charValue() { return (char) (bool ? 1 : 0); }

    @Override
    public final boolean boolValue() { return bool; }

    @Override
    public final String toString() { return bool ? "true" : "false"; }

    @Override
    public final GSLValue[] toArray() { return Utils.arrayOf(this); }

    @Override
    public final List<GSLValue> toList() { return Utils.listOf(this); }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return Utils.mapOf(this); }
    
    @Override
    public final Stream<GSLValue> stream() { return Stream.of(this); }
    
    @Override
    public final GSLBoolean cast() { return this; }

    @Override public final GSLValue operatorEquals(GSLValue value) { return bool == value.boolValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return bool != value.boolValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorGreater(GSLValue value) { return doubleValue() > value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorSmaller(GSLValue value) { return doubleValue() < value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorGreaterEquals(GSLValue value) { return doubleValue() >= value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorSmallerEquals(GSLValue value) { return doubleValue() <= value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLValue operatorNegate() { return bool ? FALSE : TRUE; }
    @Override public final int      operatorLength() { return 1; }

    @Override public final GSLValue operatorPlus(GSLValue value) { return new GSLFloat(doubleValue() + value.doubleValue()); }
    @Override public final GSLValue operatorMinus(GSLValue value) { return new GSLFloat(doubleValue() - value.doubleValue()); }
    @Override public final GSLValue operatorMultiply(GSLValue value) { return new GSLFloat(doubleValue() * value.doubleValue()); }
    @Override public final GSLValue operatorDivide(GSLValue value) { return new GSLFloat(doubleValue() / value.doubleValue()); }
    @Override public final GSLValue operatorRemainder(GSLValue value) { return new GSLInteger(longValue() % value.longValue()); }
    @Override public final GSLValue operatorIncrease() { return new GSLInteger(longValue() + 1); }
    @Override public final GSLValue operatorDecrease() { return new GSLInteger(longValue() - 1); }
    @Override public final GSLValue operatorNegative() { return new GSLInteger(-longValue()); }

    @Override public final GSLValue operatorBitwiseShiftLeft(GSLValue value) { return new GSLInteger(longValue() >> value.longValue()); }
    @Override public final GSLValue operatorBitwiseShiftRight(GSLValue value) { return new GSLInteger(longValue() << value.longValue()); }
    @Override public final GSLValue operatorBitwiseAnd(GSLValue value) { return new GSLInteger(longValue() & value.longValue()); }
    @Override public final GSLValue operatorBitwiseOr(GSLValue value) { return new GSLInteger(longValue() | value.longValue()); }
    @Override public final GSLValue operatorBitwiseXor(GSLValue value) { return new GSLInteger(longValue() ^ value.longValue()); }
    @Override public final GSLValue operatorBitwiseNot() { return new GSLInteger(~longValue()); }

    @Override public final GSLValue operatorGet(GSLValue index) { throw new UnsupportedOperatorException(this, "[]"); }

    @Override
    public GSLValue operatorGetProperty(String name) { throw new UnsupportedOperatorException(this, "."); }

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
        if(o instanceof GSLBoolean)
            return bool == ((GSLBoolean) o).bool;
        if(o instanceof GSLFloat)
            return doubleValue() == ((GSLFloat) o).number;
        if(o instanceof GSLInteger)
            return doubleValue() == ((GSLInteger) o).number;
        
        return false;
    }
    @Override public final int hashCode() { return Boolean.hashCode(bool); }
}
