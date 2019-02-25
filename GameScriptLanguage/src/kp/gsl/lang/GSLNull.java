/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kp.gsl.exception.UnsupportedOperatorException;
import kp.gsl.lib.Def;

/**
 *
 * @author Asus
 */
public final class GSLNull extends GSLImmutableValue
{
    static final GSLNull INSTANCE = new GSLNull();
    private static final GSLFloat F_ZERO = new GSLFloat(0f);
    
    private GSLNull() {}
    
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.NULL; }

    @Override
    public final int intValue() { return superHashCode(); }

    @Override
    public final long longValue() { return superHashCode(); }

    @Override
    public final float floatValue() { return superHashCode(); }

    @Override
    public final double doubleValue() { return superHashCode(); }
    
    @Override
    public final char charValue() { return (char) superHashCode(); }

    @Override
    public final boolean boolValue() { return false; }

    @Override
    public final String toString() { return "null"; }

    @Override
    public final GSLValue[] toArray() { return Utils.arrayOf(this); }

    @Override
    public final List<GSLValue> toList() { return Utils.listOf(this); }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return Utils.mapOf(this); }
    
    @Override
    public final Stream<GSLValue> stream() { return Stream.of(this); }
    
    @Override
    public final GSLNull cast() { return this; }
    
    
    @Override public final GSLInteger    operatorCastInteger() { return ZERO; }
    @Override public final GSLFloat      operatorCastFloat() { return F_ZERO; }
    @Override public final GSLBoolean    operatorCastBoolean() { return FALSE; }
    @Override public final GSLString     operatorCastString() { return EMPTY_STRING; }
    @Override public final GSLConstTuple operatorCastConstTuple() { return EMPTY_TUPLE; }
    @Override public final GSLConstMap   operatorCastConstMap() { return EMPTY_MAP; }
    @Override public final GSLFunction   operatorCastFunction() { return EMPTY_FUNCTION; }
    @Override public final GSLList       operatorCastList() { return new GSLList(); }
    @Override public final GSLTuple      operatorCastTuple() { return new GSLTuple(new GSLValue[] {}); }
    @Override public final GSLMap        operatorCastMap() { return new GSLMap(); }
    @Override public final GSLStruct     operatorCastStruct() { return new GSLStruct(); }
    @Override public final GSLBlueprint  operatorCastBlueprint() { return new GSLBlueprint(Collections.emptyMap()); }
    @Override public final GSLObject     operatorCastObject() { return new GSLObject(); }
    @Override public final GSLIterator   operatorCastIterator()
    {
        return Def.<GSLValue>iterator(new Iterator()
        {
            @Override public final boolean hasNext() { return false; }
            @Override public final GSLValue next() { return NULL; }
        });
    }
    @Override public final GSLRawBytes   operatorCastRawBytes() { return new GSLRawBytes(new byte[] { 0 }); }
    

    @Override public final GSLImmutableValue operatorEquals(GSLValue value) { return this == value ? TRUE : FALSE; }
    @Override public final GSLImmutableValue operatorNotEquals(GSLValue value) { return this != value ? TRUE : FALSE; }
    @Override public final GSLImmutableValue operatorGreater(GSLValue value) { throw new UnsupportedOperatorException(this, ">"); }
    @Override public final GSLImmutableValue operatorSmaller(GSLValue value) { throw new UnsupportedOperatorException(this, "<"); }
    @Override public final GSLImmutableValue operatorGreaterEquals(GSLValue value) { throw new UnsupportedOperatorException(this, ">="); }
    @Override public final GSLImmutableValue operatorSmallerEquals(GSLValue value) { throw new UnsupportedOperatorException(this, "<="); }
    @Override public final GSLImmutableValue operatorNegate() { return TRUE; }
    @Override public final int      operatorLength() { return 1; }

    @Override public final GSLImmutableValue operatorPlus(GSLValue value) { throw new UnsupportedOperatorException(this, "+"); }
    @Override public final GSLImmutableValue operatorMinus(GSLValue value) { throw new UnsupportedOperatorException(this, "-"); }
    @Override public final GSLImmutableValue operatorMultiply(GSLValue value) { throw new UnsupportedOperatorException(this, "*"); }
    @Override public final GSLImmutableValue operatorDivide(GSLValue value) { throw new UnsupportedOperatorException(this, "/"); }
    @Override public final GSLImmutableValue operatorRemainder(GSLValue value) { throw new UnsupportedOperatorException(this, "%"); }
    @Override public final GSLImmutableValue operatorIncrease() { throw new UnsupportedOperatorException(this, "++"); }
    @Override public final GSLImmutableValue operatorDecrease() { throw new UnsupportedOperatorException(this, "--"); }
    @Override public final GSLImmutableValue operatorNegative() { throw new UnsupportedOperatorException(this, "-()"); }

    @Override public final GSLImmutableValue operatorBitwiseShiftLeft(GSLValue value) { throw new UnsupportedOperatorException(this, "<<"); }
    @Override public final GSLImmutableValue operatorBitwiseShiftRight(GSLValue value) { throw new UnsupportedOperatorException(this, ">>"); }
    @Override public final GSLImmutableValue operatorBitwiseAnd(GSLValue value) { throw new UnsupportedOperatorException(this, "&"); }
    @Override public final GSLImmutableValue operatorBitwiseOr(GSLValue value) { throw new UnsupportedOperatorException(this, "|"); }
    @Override public final GSLImmutableValue operatorBitwiseXor(GSLValue value) { throw new UnsupportedOperatorException(this, "^"); }
    @Override public final GSLImmutableValue operatorBitwiseNot() { throw new UnsupportedOperatorException(this, "~"); }

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

    @Override public final boolean equals(Object o) { return this == o; }
    @Override public final int hashCode() { return superHashCode(); }
}
