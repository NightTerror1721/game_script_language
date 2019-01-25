/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import kp.gsl.exception.NotPointerException;
import kp.gsl.exception.UnsupportedOperatorException;
import kp.gsl.lib.Def;

/**
 *
 * @author Asus
 */
public abstract class GSLIterator extends GSLValue implements Iterator<GSLValue>
{
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.ITERATOR; }
    
    @Override
    public final boolean isMutable() { return false; }

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
    public final boolean boolValue() { return hasNext(); }

    @Override
    public final String toString() { return "iterator::" + superHashCode(); }

    @Override
    public final GSLValue[] toArray() { return stream().toArray(GSLValue[]::new); }

    @Override
    public final List<GSLValue> toList() { return stream().collect(Collectors.toList()); }

    @Override
    public final Map<GSLValue, GSLValue> toMap()
    {
        return Utils.collectionToMap(toList());
    }
    
    @Override
    public final Stream<GSLValue> stream()
    {
        
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED), false);
    }
    
    @Override
    public final GSLIterator cast() { return this; }

    @Override public final GSLValue operatorEquals(GSLValue value) { return this == value ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return this != value ? TRUE : FALSE; }
    @Override public final GSLValue operatorGreater(GSLValue value) { throw new UnsupportedOperatorException(this, ">"); }
    @Override public final GSLValue operatorSmaller(GSLValue value) { throw new UnsupportedOperatorException(this, "<"); }
    @Override public final GSLValue operatorGreaterEquals(GSLValue value) { throw new UnsupportedOperatorException(this, ">="); }
    @Override public final GSLValue operatorSmallerEquals(GSLValue value) { throw new UnsupportedOperatorException(this, "<="); }
    @Override public final GSLValue operatorNegate() { return hasNext() ? FALSE : TRUE; }
    @Override public final int      operatorLength() { return 1; }

    @Override public final GSLValue operatorPlus(GSLValue value) { throw new UnsupportedOperatorException(this, "+"); }
    @Override public final GSLValue operatorMinus(GSLValue value) { throw new UnsupportedOperatorException(this, "-"); }
    @Override public final GSLValue operatorMultiply(GSLValue value) { throw new UnsupportedOperatorException(this, "*"); }
    @Override public final GSLValue operatorDivide(GSLValue value) { throw new UnsupportedOperatorException(this, "/"); }
    @Override public final GSLValue operatorRemainder(GSLValue value) { throw new UnsupportedOperatorException(this, "%"); }
    @Override public final GSLValue operatorIncrease() { return next(); }
    @Override public final GSLValue operatorDecrease() { throw new UnsupportedOperatorException(this, "--"); }
    @Override public final GSLValue operatorNegative() { throw new UnsupportedOperatorException(this, "-()"); }

    @Override public final GSLValue operatorBitwiseShiftLeft(GSLValue value) { throw new UnsupportedOperatorException(this, ">>"); }
    @Override public final GSLValue operatorBitwiseShiftRight(GSLValue value) { throw new UnsupportedOperatorException(this, "<<"); }
    @Override public final GSLValue operatorBitwiseAnd(GSLValue value) { throw new UnsupportedOperatorException(this, "&"); }
    @Override public final GSLValue operatorBitwiseOr(GSLValue value) { throw new UnsupportedOperatorException(this, "|"); }
    @Override public final GSLValue operatorBitwiseXor(GSLValue value) { throw new UnsupportedOperatorException(this, "^"); }
    @Override public final GSLValue operatorBitwiseNot() { throw new UnsupportedOperatorException(this, "~"); }

    @Override public final GSLValue operatorGet(GSLValue index) { throw new UnsupportedOperatorException(this, "[]"); }
    @Override public final void     operatorSet(GSLValue index, GSLValue value) { throw new UnsupportedOperatorException(this, "[]="); }

    @Override
    public GSLValue operatorGetProperty(String name)
    {
        switch(name)
        {
            default: return NULL;
            case "hasNext": return HAS_NEXT;
            case "next": return NEXT;
        }
    }
    @Override public final void operatorSetProperty(String name, GSLValue value) { throw new UnsupportedOperatorException(this, ".="); }
    @Override public final void operatorDelProperty(String name) { throw new UnsupportedOperatorException(this, "delete"); }

    @Override
    public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
    {
        return null;
    }
    
    @Override
    public final GSLValue operatorNew(GSLVarargs args) { throw new UnsupportedOperatorException(this, "new"); }
    
    @Override public final GSLValue operatorReferenceGet() { throw new NotPointerException(this); }
    @Override public final void     operatorReferenceSet(GSLValue value) { throw new NotPointerException(this); }

    @Override public final GSLValue operatorIterator() { return this; }

    @Override public final boolean equals(Object o) { return this == o; }
    @Override public final int hashCode() { return superHashCode(); }
    
    
    private static final GSLValue HAS_NEXT = Def.<GSLIterator>method((self, args) -> {
        return self.hasNext() ? TRUE : FALSE;
    });
    private static final GSLValue NEXT = Def.<GSLIterator>method((self, args) -> {
        return self.next();
    });
}
