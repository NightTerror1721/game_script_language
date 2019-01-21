/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.List;
import java.util.Map;
import kp.gsl.exception.UnsupportedOperatorException;

/**
 *
 * @author Asus
 */
public final class GSLString extends GSLImmutableValue
{
    final String string;
    
    public GSLString(String string)
    {
        if(string == null)
            throw new NullPointerException();
        this.string  = string;
    }
    
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.STRING; }

    @Override
    public final int intValue() { return string.hashCode(); }

    @Override
    public final long longValue() { return string.hashCode(); }

    @Override
    public final float floatValue() { return string.hashCode(); }

    @Override
    public final double doubleValue() { return string.hashCode(); }
    
    @Override
    public final char charValue() { return (char) string.hashCode(); }

    @Override
    public final boolean boolValue() { return !string.isEmpty(); }

    @Override
    public final String toString() { return string; }

    @Override
    public final GSLValue[] toArray() { return Utils.arrayOf(this); }

    @Override
    public final List<GSLValue> toList() { return Utils.listOf(this); }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return Utils.mapOf(this); }
    
    @Override
    public final GSLString cast() { return this; }

    @Override public final GSLValue operatorEquals(GSLValue value) { return string.equals(value.toString()) ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return string.equals(value.toString()) ? FALSE : TRUE; }
    @Override public final GSLValue operatorGreater(GSLValue value) { return string.compareTo(value.toString()) > 0 ? TRUE : FALSE; }
    @Override public final GSLValue operatorSmaller(GSLValue value) { return string.compareTo(value.toString()) < 0 ? TRUE : FALSE; }
    @Override public final GSLValue operatorGreaterEquals(GSLValue value) { return string.compareTo(value.toString()) >= 0 ? TRUE : FALSE; }
    @Override public final GSLValue operatorSmallerEquals(GSLValue value) { return string.compareTo(value.toString()) <= 0 ? TRUE : FALSE; }
    @Override public final GSLValue operatorNegate() { return string.isEmpty() ? TRUE : FALSE; }
    @Override public final int      operatorLength() { return string.length(); }

    @Override public final GSLValue operatorPlus(GSLValue value) { throw new UnsupportedOperatorException(this, "+"); }
    @Override public final GSLValue operatorMinus(GSLValue value) { throw new UnsupportedOperatorException(this, "-"); }
    @Override public final GSLValue operatorMultiply(GSLValue value) { throw new UnsupportedOperatorException(this, "*"); }
    @Override public final GSLValue operatorDivide(GSLValue value) { throw new UnsupportedOperatorException(this, "/"); }
    @Override public final GSLValue operatorRemainder(GSLValue value) { throw new UnsupportedOperatorException(this, "%"); }
    @Override public final GSLValue operatorIncrease() { throw new UnsupportedOperatorException(this, "++"); }
    @Override public final GSLValue operatorDecrease() { throw new UnsupportedOperatorException(this, "--"); }
    @Override public final GSLValue operatorNegative() { throw new UnsupportedOperatorException(this, "-()"); }

    @Override public final GSLValue operatorBitwiseShiftLeft(GSLValue value) { throw new UnsupportedOperatorException(this, ">>"); }
    @Override public final GSLValue operatorBitwiseShiftRight(GSLValue value) { throw new UnsupportedOperatorException(this, "<<"); }
    @Override public final GSLValue operatorBitwiseAnd(GSLValue value) { throw new UnsupportedOperatorException(this, "&"); }
    @Override public final GSLValue operatorBitwiseOr(GSLValue value) { throw new UnsupportedOperatorException(this, "|"); }
    @Override public final GSLValue operatorBitwiseXor(GSLValue value) { throw new UnsupportedOperatorException(this, "^"); }
    @Override public final GSLValue operatorBitwiseNot() { throw new UnsupportedOperatorException(this, "~"); }

    @Override public final GSLValue operatorGet(GSLValue index)
    {
        return new GSLInteger(string.charAt(index.intValue()));
    }
    @Override public final GSLValue operatorGet(int index)
    {
        return new GSLInteger(string.charAt(index));
    }

    @Override
    public GSLValue operatorGetProperty(String name)
    {
        switch(name)
        {
            default: return NULL;
        }
    }

    @Override
    public final GSLValue operatorCall(GSLValue self, GSLValue[] args) { throw new UnsupportedOperatorException(this, "()"); }
    
    @Override
    public final GSLValue operatorNew(GSLValue[] args) { throw new UnsupportedOperatorException(this, "new"); }

    @Override
    public final GSLValue operatorIterator() { return Utils.oneIter(this); }
    @Override public final boolean hasNext() { throw new UnsupportedOperatorException(this, "hasNext"); }
    @Override public final GSLValue next() { throw new UnsupportedOperatorException(this, "next"); }

    @Override public final boolean equals(Object o) { return this == o; }
    @Override public final int hashCode() { return superHashCode(); }
}
