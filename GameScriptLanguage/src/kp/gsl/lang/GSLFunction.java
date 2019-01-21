/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.List;
import java.util.Map;
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
    public final GSLFunction cast() { return this; }

    @Override public final GSLValue operatorEquals(GSLValue value) { return this == value ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return this != value ? TRUE : FALSE; }
    @Override public final GSLValue operatorGreater(GSLValue value) { throw new UnsupportedOperatorException(this, ">"); }
    @Override public final GSLValue operatorSmaller(GSLValue value) { throw new UnsupportedOperatorException(this, "<"); }
    @Override public final GSLValue operatorGreaterEquals(GSLValue value) { throw new UnsupportedOperatorException(this, ">="); }
    @Override public final GSLValue operatorSmallerEquals(GSLValue value) { throw new UnsupportedOperatorException(this, "<="); }
    @Override public final GSLValue operatorNegate() { return FALSE; }
    @Override public final int      operatorLength() { return 1; }

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

    @Override public final GSLValue operatorGet(GSLValue index) { throw new UnsupportedOperatorException(this, "[]"); }

    @Override
    public GSLValue operatorGetProperty(String name)
    {
        switch(name)
        {
            default: return NULL;
            case "call": return CALL;
            case "apply": return APPLY;
        }
    }

    @Override
    public abstract GSLValue operatorCall(GSLValue self, GSLValue[] args);
    
    @Override
    public final GSLValue operatorNew(GSLValue[] args) { throw new UnsupportedOperatorException(this, "new"); }

    @Override
    public final GSLValue operatorIterator() { return Utils.oneIter(this); }
    @Override public final boolean hasNext() { throw new UnsupportedOperatorException(this, "hasNext"); }
    @Override public final GSLValue next() { throw new UnsupportedOperatorException(this, "next"); }

    @Override public final boolean equals(Object o) { return this == o; }
    @Override public final int hashCode() { return superHashCode(); }
    
    
    private static final GSLFunction CALL = Def.<GSLFunction>method((self, args) -> {
        if(args.length < 1)
            throw new GSLRuntimeException("Expected minimun 1 argument");
        var nargs = new GSLValue[args.length - 1];
        System.arraycopy(args, 1, nargs, 0, nargs.length);
        return self.operatorCall(args[0], nargs);
    });
    
    private static final GSLFunction APPLY = Def.<GSLFunction>method((self, args) -> {
        if(args.length < 1)
            throw new GSLRuntimeException("Expected minimun 1 argument");
        return self.operatorCall(args[0], args[1].toArray());
    });
}
