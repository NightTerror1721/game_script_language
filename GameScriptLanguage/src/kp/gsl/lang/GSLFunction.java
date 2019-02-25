/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kp.gsl.exception.GSLRuntimeException;
import kp.gsl.exception.UnsupportedOperatorException;
import kp.gsl.lib.Def;

/**
 *
 * @author Asus
 */
public abstract class GSLFunction extends GSLImmutableValue
{
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.FUNCTION; }

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
    public final boolean boolValue() { return true; }

    @Override
    public final String toString() { return "function::" + superHashCode(); }

    @Override
    public final GSLValue[] toArray() { return Utils.arrayOf(this); }

    @Override
    public final List<GSLValue> toList() { return Utils.listOf(this); }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return Utils.mapOf(this); }
    
    @Override
    public final Stream<GSLValue> stream() { return Stream.of(this); }
    
    @Override
    public final GSLFunction cast() { return this; }
    
    
    @Override public final GSLInteger    operatorCastInteger() { return new GSLInteger(hashCode()); }
    @Override public final GSLFloat      operatorCastFloat() { return new GSLFloat(hashCode()); }
    @Override public final GSLBoolean    operatorCastBoolean() { return boolValue() ? TRUE : FALSE; }
    @Override public final GSLString     operatorCastString() { return new GSLString(toString()); }
    @Override public final GSLConstTuple operatorCastConstTuple() { return new GSLConstTuple(Utils.constArrayOf(this)); }
    @Override public final GSLConstMap   operatorCastConstMap() { return new GSLConstMap(Utils.constMapOf(this)); }
    @Override public final GSLFunction   operatorCastFunction() { return this; }
    @Override public final GSLList       operatorCastList() { return new GSLList(Utils.listOf(this)); }
    @Override public final GSLTuple      operatorCastTuple() { return new GSLTuple(Utils.arrayOf(this)); }
    @Override public final GSLMap        operatorCastMap() { return new GSLMap(Utils.mapOf(this)); }
    @Override public final GSLStruct     operatorCastStruct() { return Utils.structOf(this); }
    @Override public final GSLBlueprint  operatorCastBlueprint() { return Utils.blueprintOf(this); }
    @Override public final GSLObject     operatorCastObject() { return Utils.objectOf(this); }
    @Override public final GSLIterator   operatorCastIterator() { return Utils.oneIter(this); }
    @Override public final GSLRawBytes   operatorCastRawBytes() { throw new GSLRuntimeException("Cannot cast function to bytes"); }
    

    @Override public final GSLImmutableValue operatorEquals(GSLValue value) { return this == value ? TRUE : FALSE; }
    @Override public final GSLImmutableValue operatorNotEquals(GSLValue value) { return this != value ? TRUE : FALSE; }
    @Override public final GSLImmutableValue operatorGreater(GSLValue value) { throw new UnsupportedOperatorException(this, ">"); }
    @Override public final GSLImmutableValue operatorSmaller(GSLValue value) { throw new UnsupportedOperatorException(this, "<"); }
    @Override public final GSLImmutableValue operatorGreaterEquals(GSLValue value) { throw new UnsupportedOperatorException(this, ">="); }
    @Override public final GSLImmutableValue operatorSmallerEquals(GSLValue value) { throw new UnsupportedOperatorException(this, "<="); }
    @Override public final GSLImmutableValue operatorNegate() { return FALSE; }
    @Override public final int      operatorLength() { return 1; }

    @Override public final GSLImmutableValue operatorPlus(GSLValue value) { throw new UnsupportedOperatorException(this, "+"); }
    @Override public final GSLImmutableValue operatorMinus(GSLValue value) { throw new UnsupportedOperatorException(this, "-"); }
    @Override public final GSLImmutableValue operatorMultiply(GSLValue value) { throw new UnsupportedOperatorException(this, "*"); }
    @Override public final GSLImmutableValue operatorDivide(GSLValue value) { throw new UnsupportedOperatorException(this, "/"); }
    @Override public final GSLImmutableValue operatorRemainder(GSLValue value) { throw new UnsupportedOperatorException(this, "%"); }
    @Override public final GSLImmutableValue operatorIncrease() { throw new UnsupportedOperatorException(this, "++"); }
    @Override public final GSLImmutableValue operatorDecrease() { throw new UnsupportedOperatorException(this, "--"); }
    @Override public final GSLImmutableValue operatorNegative() { throw new UnsupportedOperatorException(this, "-()"); }

    @Override public final GSLImmutableValue operatorBitwiseShiftLeft(GSLValue value) { throw new UnsupportedOperatorException(this, ">>"); }
    @Override public final GSLImmutableValue operatorBitwiseShiftRight(GSLValue value) { throw new UnsupportedOperatorException(this, "<<"); }
    @Override public final GSLImmutableValue operatorBitwiseAnd(GSLValue value) { throw new UnsupportedOperatorException(this, "&"); }
    @Override public final GSLImmutableValue operatorBitwiseOr(GSLValue value) { throw new UnsupportedOperatorException(this, "|"); }
    @Override public final GSLImmutableValue operatorBitwiseXor(GSLValue value) { throw new UnsupportedOperatorException(this, "^"); }
    @Override public final GSLImmutableValue operatorBitwiseNot() { throw new UnsupportedOperatorException(this, "~"); }

    @Override public final GSLImmutableValue operatorGet(GSLValue index) { throw new UnsupportedOperatorException(this, "[x]"); }
    @Override public final GSLImmutableValue operatorPeek() { throw new UnsupportedOperatorException(this, "[]"); }

    @Override
    public GSLImmutableValue operatorGetProperty(String name)
    {
        switch(name)
        {
            default: return NULL;
            case "call": return CALL;
            case "apply": return APPLY;
        }
    }

    @Override
    public abstract GSLValue operatorCall(GSLValue self, GSLVarargs args);
    
    @Override
    public final GSLValue operatorNew(GSLVarargs args) { throw new UnsupportedOperatorException(this, "new"); }

    @Override
    public final GSLValue operatorIterator() { return Utils.oneIter(this); }
    @Override public final boolean hasNext() { throw new UnsupportedOperatorException(this, "hasNext"); }
    @Override public final GSLValue next() { throw new UnsupportedOperatorException(this, "next"); }

    @Override public final boolean equals(Object o) { return this == o; }
    @Override public final int hashCode() { return superHashCode(); }
    
    
    private static final GSLFunction CALL = Def.<GSLFunction>method((self, args) -> {
        if(args.numberOfArguments() < 1)
            throw new GSLRuntimeException("Expected minimun 1 argument");
        return self.operatorCall(args.arg0(), subVarargs(args, 1));
    });
    
    private static final GSLFunction APPLY = Def.<GSLFunction>method((self, args) -> {
        if(args.numberOfArguments() < 1)
            throw new GSLRuntimeException("Expected minimun 1 argument");
        return self.operatorCall(args.arg0(), subVarargs(args, 1));
    });
}
