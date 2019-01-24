/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kp.gsl.exception.NotPointerException;
import kp.gsl.exception.UnsupportedOperatorException;
import kp.gsl.lib.Def;

/**
 *
 * @author Asus
 */
public final class GSLVector extends GSLValue
{
    final GSLValue[] vector;
    
    public GSLVector(GSLValue[] array)
    {
        if(array == null)
            throw new NullPointerException();
        this.vector = array;
    }
    
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.VECTOR; }

    @Override
    public final int intValue() { return hashCode(); }

    @Override
    public final long longValue() { return hashCode(); }

    @Override
    public final float floatValue() { return hashCode(); }

    @Override
    public final double doubleValue() { return hashCode(); }
    
    @Override
    public final char charValue() { return (char) hashCode(); }

    @Override
    public final boolean boolValue() { return vector.length > 0; }

    @Override
    public final String toString() { return Arrays.toString(vector); }

    @Override
    public final GSLValue[] toArray() { return vector; }

    @Override
    public final List<GSLValue> toList() { return Arrays.asList(vector); }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return Utils.arrayToMap(vector); }
    
    @Override
    public final Stream<GSLValue> stream() { return Arrays.stream(vector); }
    
    @Override
    public final GSLVector cast() { return this; }
    
    @Override
    public final boolean isMutable() { return true; }

    @Override public final GSLValue operatorEquals(GSLValue value) { return equals(value) ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return equals(value)  ? FALSE : TRUE; }
    @Override public final GSLValue operatorGreater(GSLValue value) { throw new UnsupportedOperatorException(this, ">"); }
    @Override public final GSLValue operatorSmaller(GSLValue value) { throw new UnsupportedOperatorException(this, "<"); }
    @Override public final GSLValue operatorGreaterEquals(GSLValue value) { throw new UnsupportedOperatorException(this, ">="); }
    @Override public final GSLValue operatorSmallerEquals(GSLValue value) { throw new UnsupportedOperatorException(this, "<="); }
    @Override public final GSLValue operatorNegate() { return vector.length < 1 ? TRUE : FALSE; }
    @Override public final int      operatorLength() { return vector.length; }

    @Override public final GSLValue operatorPlus(GSLValue value)
    {
        return new GSLVector(Stream.concat(stream(), value.stream()).toArray(GSLValue[]::new));
    }
    @Override public final GSLValue operatorMinus(GSLValue value)
    {
        var other = value.toList();
        return new GSLVector(stream().filter(v -> !other.contains(v)).toArray(GSLValue[]::new));
    }
    @Override public final GSLValue operatorMultiply(GSLValue value) { throw new UnsupportedOperatorException(this, "*"); }
    @Override public final GSLValue operatorDivide(GSLValue value) { throw new UnsupportedOperatorException(this, "/"); }
    @Override public final GSLValue operatorRemainder(GSLValue value) { throw new UnsupportedOperatorException(this, "%"); }
    @Override public final GSLValue operatorIncrease() { throw new UnsupportedOperatorException(this, "++"); }
    @Override public final GSLValue operatorDecrease() { throw new UnsupportedOperatorException(this, "--"); }
    @Override public final GSLValue operatorNegative() { throw new UnsupportedOperatorException(this, "-()"); }

    @Override public final GSLValue operatorBitwiseShiftLeft(GSLValue value) { throw new UnsupportedOperatorException(this, "<<"); }
    @Override public final GSLValue operatorBitwiseShiftRight(GSLValue value) { throw new UnsupportedOperatorException(this, ">>"); }
    @Override public final GSLValue operatorBitwiseAnd(GSLValue value) { throw new UnsupportedOperatorException(this, "&"); }
    @Override public final GSLValue operatorBitwiseOr(GSLValue value) { throw new UnsupportedOperatorException(this, "|"); }
    @Override public final GSLValue operatorBitwiseXor(GSLValue value) { throw new UnsupportedOperatorException(this, "^"); }
    @Override public final GSLValue operatorBitwiseNot() { throw new UnsupportedOperatorException(this, "~"); }

    @Override public final GSLValue operatorGet(GSLValue index)
    {
        GSLValue v;
        return (v = vector[index.intValue()]) == null ? NULL : v;
    }
    @Override public final GSLValue operatorGet(int index)
    {
        GSLValue v;
        return (v = vector[index]) == null ? NULL : v;
    }
    @Override public final void operatorSet(GSLValue index, GSLValue value)
    {
        vector[index.intValue()] = value;
    }
    @Override public final void operatorSet(int index, GSLValue value)
    {
        vector[index] = value;
    }

    @Override
    public GSLValue operatorGetProperty(String name)
    {
        switch(name)
        {
            default: return NULL;
            case "contains": return CONTAINS;
            case "isEmpry": return EMPTY;
            case "get": return GET;
            case "indexOf": return INDEX_OF;
            case "lastIndexOf": return LAST_INDEX_OF;
            case "set": return SET;
            case "size": return SIZE;
            case "sort": return SORT;
            case "subVector": return SUB;
        }
    }
    @Override
    public void operatorSetProperty(String name, GSLValue value) { throw new UnsupportedOperatorException(this, "[]="); }

    @Override
    public final GSLValue operatorCall(GSLValue self, GSLVarargs args) { throw new UnsupportedOperatorException(this, "()"); }
    
    @Override
    public final GSLValue operatorNew(GSLVarargs args) { throw new UnsupportedOperatorException(this, "new"); }
    
    @Override public final GSLValue operatorReferenceGet() { throw new NotPointerException(this); }
    @Override public final void     operatorReferenceSet(GSLValue value) { throw new NotPointerException(this); }

    @Override
    public final GSLValue operatorIterator()
    {
        return Def.iterator(new Iterator<GSLValue>()
        {
            private int it;
            @Override public final boolean hasNext() { return it < vector.length; }
            @Override public final GSLValue next() { return vector[it++]; }
        });
    }
    @Override public final boolean hasNext() { throw new UnsupportedOperatorException(this, "hasNext"); }
    @Override public final GSLValue next() { throw new UnsupportedOperatorException(this, "next"); }

    @Override public final boolean equals(Object o)
    {
        return o instanceof GSLVector &&
                Arrays.equals(vector, ((GSLVector) o).vector);
    }
    @Override public final int hashCode() { return Arrays.hashCode(vector); }
    
    
    
    private static final GSLValue CONTAINS = Def.<GSLVector>boolMethod((self, args) -> self.toList().contains(args.arg0()));
    
    private static final GSLValue GET = Def.<GSLVector>method((self, args) -> {
        GSLValue v;
        return (v = self.vector[args.arg0().intValue()]) == null ? args.arg1() : v;
    });
    
    private static final GSLValue INDEX_OF = Def.<GSLVector>intMethod((self, args) -> self.toList().indexOf(args.arg0().intValue()));
    
    private static final GSLValue EMPTY = Def.<GSLVector>boolMethod((self, args) -> self.vector.length < 1);
    
    private static final GSLValue LAST_INDEX_OF = Def.<GSLVector>intMethod((self, args) -> self.toList().lastIndexOf(args.arg0().intValue()));
    
    private static final GSLValue SIZE = Def.<GSLVector>intMethod((self, args) -> self.vector.length);
    
    private static final GSLValue SET = Def.<GSLVector>method((self, args) -> self.vector[args.arg0().intValue()] = args.arg1());
    
    private static final GSLValue SORT = Def.<GSLVector>method((self, args) -> { Arrays.sort(self.vector, Utils.defaultComparator(args.arg0())); return self; });
    
    private static final GSLValue SUB = Def.<GSLVector>method((self, args) -> {
        if(args.numberOfArguments() > 1)
            return new GSLVector(Arrays.copyOfRange(self.vector, args.arg0().intValue(), args.arg1().intValue()));
        return new GSLVector(Arrays.copyOfRange(self.vector, args.arg0().intValue(), self.vector.length));
    });
}
