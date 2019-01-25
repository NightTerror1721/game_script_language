/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kp.gsl.exception.NotPointerException;
import kp.gsl.exception.UnsupportedOperatorException;

/**
 *
 * @author Asus
 */
public class GSLNative extends GSLValue
{
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.NATIVE; }

    @Override
    public final boolean isMutable() { return true; }

    @Override
    public int intValue() { return superHashCode(); }

    @Override
    public long longValue() { return superHashCode(); }

    @Override
    public float floatValue() { return superHashCode(); }

    @Override
    public double doubleValue() { return superHashCode(); }

    @Override
    public char charValue() { return (char) superHashCode(); }

    @Override
    public boolean boolValue() { return true; }

    @Override
    public String toString() { return "native::" + superHashCode(); }

    @Override
    public final GSLValue[] toArray() { return Utils.arrayOf(this); }

    @Override
    public final List<GSLValue> toList() { return Utils.listOf(this); }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return Utils.mapOf(this); }
    
    @Override
    public final Stream<GSLValue> stream() { return Stream.of(this); }

    @Override public final GSLValue operatorEquals(GSLValue value) { return equals(value) ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return equals(value) ? FALSE : TRUE; }
    @Override public GSLValue operatorGreater(GSLValue value) { throw new UnsupportedOperatorException(this, ">"); }
    @Override public GSLValue operatorSmaller(GSLValue value) { throw new UnsupportedOperatorException(this, "<"); }
    @Override public GSLValue operatorGreaterEquals(GSLValue value) { throw new UnsupportedOperatorException(this, ">="); }
    @Override public GSLValue operatorSmallerEquals(GSLValue value) { throw new UnsupportedOperatorException(this, "<="); }
    @Override public GSLValue operatorNegate() { return TRUE; }
    @Override public int      operatorLength() { return 1; }

    @Override public GSLValue operatorPlus(GSLValue value) { throw new UnsupportedOperatorException(this, "+"); }
    @Override public GSLValue operatorMinus(GSLValue value) { throw new UnsupportedOperatorException(this, "-"); }
    @Override public GSLValue operatorMultiply(GSLValue value) { throw new UnsupportedOperatorException(this, "*"); }
    @Override public GSLValue operatorDivide(GSLValue value) { throw new UnsupportedOperatorException(this, "/"); }
    @Override public GSLValue operatorRemainder(GSLValue value) { throw new UnsupportedOperatorException(this, "%"); }
    @Override public GSLValue operatorIncrease() { throw new UnsupportedOperatorException(this, "++"); }
    @Override public GSLValue operatorDecrease() { throw new UnsupportedOperatorException(this, "--"); }
    @Override public GSLValue operatorNegative() { throw new UnsupportedOperatorException(this, "-()"); }

    @Override public GSLValue operatorBitwiseShiftLeft(GSLValue value) { throw new UnsupportedOperatorException(this, "<<"); }
    @Override public GSLValue operatorBitwiseShiftRight(GSLValue value) { throw new UnsupportedOperatorException(this, ">>"); }
    @Override public GSLValue operatorBitwiseAnd(GSLValue value) { throw new UnsupportedOperatorException(this, "&"); }
    @Override public GSLValue operatorBitwiseOr(GSLValue value) { throw new UnsupportedOperatorException(this, "|"); }
    @Override public GSLValue operatorBitwiseXor(GSLValue value) { throw new UnsupportedOperatorException(this, "^"); }
    @Override public GSLValue operatorBitwiseNot() { throw new UnsupportedOperatorException(this, "~"); }

    @Override public GSLValue operatorGet(GSLValue index) { throw new UnsupportedOperatorException(this, "[]"); }
    @Override public void operatorSet(GSLValue index, GSLValue value) { throw new UnsupportedOperatorException(this, "[]="); }

    @Override
    public GSLValue operatorGetProperty(String name) { throw new UnsupportedOperatorException(this, "."); }
    @Override
    public void operatorSetProperty(String name, GSLValue value) { throw new UnsupportedOperatorException(this, ".="); }
    @Override
    public void operatorDelProperty(String name) { throw new UnsupportedOperatorException(this, "delete"); }

    @Override
    public GSLValue operatorCall(GSLValue self, GSLVarargs args) { throw new UnsupportedOperatorException(this, "()"); }
    
    @Override
    public GSLValue operatorNew(GSLVarargs args) { throw new UnsupportedOperatorException(this, "new"); }
    
    @Override public final GSLValue operatorReferenceGet() { throw new NotPointerException(this); }
    @Override public final void     operatorReferenceSet(GSLValue value) { throw new NotPointerException(this); }

    @Override
    public GSLValue operatorIterator() { return Utils.oneIter(this); }
    @Override public boolean hasNext() { throw new UnsupportedOperatorException(this, "hasNext"); }
    @Override public GSLValue next() { throw new UnsupportedOperatorException(this, "next"); }

    @Override public boolean equals(Object o) { return this == o; }
    @Override public int hashCode() { return superHashCode(); }
    
}
