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
    
    final boolean bool;
    
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
    
    
    @Override public final GSLInteger    operatorCastInteger() { return new GSLInteger(bool ? 1 : 0); }
    @Override public final GSLFloat      operatorCastFloat() { return new GSLFloat(bool ? 1 : 0); }
    @Override public final GSLBoolean    operatorCastBoolean() { return this; }
    @Override public final GSLString     operatorCastString() { return new GSLString(bool ? "1" : ""); }
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
    @Override public final GSLRawBytes   operatorCastRawBytes() { return new GSLRawBytes(new byte[] { (byte) (bool ? 1 : 0) }); }
    

    @Override public final GSLImmutableValue operatorEquals(GSLValue value) { return bool == value.boolValue() ? TRUE : FALSE; }
    @Override public final GSLImmutableValue operatorNotEquals(GSLValue value) { return bool != value.boolValue() ? TRUE : FALSE; }
    @Override public final GSLImmutableValue operatorGreater(GSLValue value) { return doubleValue() > value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLImmutableValue operatorSmaller(GSLValue value) { return doubleValue() < value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLImmutableValue operatorGreaterEquals(GSLValue value) { return doubleValue() >= value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLImmutableValue operatorSmallerEquals(GSLValue value) { return doubleValue() <= value.doubleValue() ? TRUE : FALSE; }
    @Override public final GSLImmutableValue operatorNegate() { return bool ? FALSE : TRUE; }
    @Override public final int      operatorLength() { return 1; }

    @Override public final GSLImmutableValue operatorPlus(GSLValue value) { return new GSLFloat(doubleValue() + value.doubleValue()); }
    @Override public final GSLImmutableValue operatorMinus(GSLValue value) { return new GSLFloat(doubleValue() - value.doubleValue()); }
    @Override public final GSLImmutableValue operatorMultiply(GSLValue value) { return new GSLFloat(doubleValue() * value.doubleValue()); }
    @Override public final GSLImmutableValue operatorDivide(GSLValue value) { return new GSLFloat(doubleValue() / value.doubleValue()); }
    @Override public final GSLImmutableValue operatorRemainder(GSLValue value) { return new GSLInteger(longValue() % value.longValue()); }
    @Override public final GSLImmutableValue operatorIncrease() { return new GSLInteger(longValue() + 1); }
    @Override public final GSLImmutableValue operatorDecrease() { return new GSLInteger(longValue() - 1); }
    @Override public final GSLImmutableValue operatorNegative() { return new GSLInteger(-longValue()); }

    @Override public final GSLImmutableValue operatorBitwiseShiftLeft(GSLValue value) { return new GSLInteger(longValue() >> value.longValue()); }
    @Override public final GSLImmutableValue operatorBitwiseShiftRight(GSLValue value) { return new GSLInteger(longValue() << value.longValue()); }
    @Override public final GSLImmutableValue operatorBitwiseAnd(GSLValue value) { return new GSLInteger(longValue() & value.longValue()); }
    @Override public final GSLImmutableValue operatorBitwiseOr(GSLValue value) { return new GSLInteger(longValue() | value.longValue()); }
    @Override public final GSLImmutableValue operatorBitwiseXor(GSLValue value) { return new GSLInteger(longValue() ^ value.longValue()); }
    @Override public final GSLImmutableValue operatorBitwiseNot() { return new GSLInteger(~longValue()); }

    @Override public final GSLImmutableValue operatorGet(GSLValue index) { throw new UnsupportedOperatorException(this, "[x]"); }
    @Override public final GSLImmutableValue operatorPeek() { throw new UnsupportedOperatorException(this, "[]"); }

    @Override
    public GSLImmutableValue operatorGetProperty(String name) { throw new UnsupportedOperatorException(this, "."); }

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
