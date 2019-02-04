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
    
    
    @Override public final GSLInteger    operatorCastInteger() { return new GSLInteger(longValue()); }
    @Override public final GSLFloat      operatorCastFloat() { return new GSLFloat(doubleValue()); }
    @Override public final GSLBoolean    operatorCastBoolean() { return boolValue() ? TRUE : FALSE; }
    @Override public final GSLString     operatorCastString() { return new GSLString(toString()); }
    @Override public final GSLConstTuple operatorCastConstTuple() { return new GSLConstTuple(Utils.toImmutable(toArray())); }
    @Override public final GSLConstMap   operatorCastConstMap() { return new GSLConstMap(Utils.toImmutable(toMap())); }
    @Override public final GSLFunction   operatorCastFunction() { return Utils.autoGetter(this); }
    @Override public final GSLList       operatorCastList() { return new GSLList(toList()); }
    @Override public final GSLTuple      operatorCastTuple() { return new GSLTuple(toArray()); }
    @Override public final GSLMap        operatorCastMap() { return new GSLMap(toMap()); }
    @Override public final GSLStruct     operatorCastStruct() { return Utils.structOf(this); }
    @Override public final GSLBlueprint  operatorCastBlueprint() { return Utils.blueprintOf(this); }
    @Override public final GSLObject     operatorCastObject() { return Utils.objectOf(this); }
    @Override public final GSLIterator   operatorCastIterator() { return Utils.oneIter(this); }
    @Override public GSLRawBytes   operatorCastRawBytes() { throw new UnsupportedOperatorException(this, "(bytes)"); }
    

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
    @Override public void operatorSet(GSLValue index, GSLValue value) { throw new UnsupportedOperatorException(this, "[x]="); }
    @Override public void operatorAdd(GSLValue value) { throw new UnsupportedOperatorException(this, "[]="); }

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
