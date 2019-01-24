/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kp.gsl.exception.UnsupportedOperatorException;
import kp.gsl.lib.Def;

/**
 *
 * @author Asus
 */
public final class GSLTuple extends GSLImmutableValue
{
    private final GSLValue[] tuple;
    
    public GSLTuple(GSLValue[] array)
    {
        this.tuple = new GSLValue[array.length];
        System.arraycopy(array, 0, tuple, 0, array.length);
    }
    GSLTuple(GSLValue[] array, boolean dummy) { this.tuple = array; }
    
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.TUPLE; }

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
    public final boolean boolValue() { return tuple.length > 0; }

    @Override
    public final String toString() { return Arrays.toString(tuple); }

    @Override
    public final GSLValue[] toArray()
    {
        var copy = new GSLValue[tuple.length];
        System.arraycopy(tuple, 0, copy, 0, copy.length);
        return copy;
    }

    @Override
    public final List<GSLValue> toList() { return List.of(tuple); }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return Utils.collectionToMap(toList()); }
    
    @Override
    public final Stream<GSLValue> stream() { return Stream.of(tuple); }
    
    @Override
    public final GSLTuple cast() { return this; }

    @Override public final GSLValue operatorEquals(GSLValue value) { return equals(value) ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return equals(value) ? FALSE : TRUE; }
    @Override public final GSLValue operatorGreater(GSLValue value) { throw new UnsupportedOperatorException(this, ">"); }
    @Override public final GSLValue operatorSmaller(GSLValue value) { throw new UnsupportedOperatorException(this, "<"); }
    @Override public final GSLValue operatorGreaterEquals(GSLValue value) { throw new UnsupportedOperatorException(this, ">="); }
    @Override public final GSLValue operatorSmallerEquals(GSLValue value) { throw new UnsupportedOperatorException(this, "<="); }
    @Override public final GSLValue operatorNegate() { return boolValue() ? FALSE : TRUE; }
    @Override public final int      operatorLength() { return tuple.length; }

    @Override public final GSLValue operatorPlus(GSLValue value)
    {
        return new GSLTuple(Stream.concat(stream(), value.stream())
                .toArray(GSLValue[]::new), true);
    }
    @Override public final GSLValue operatorMinus(GSLValue value)
    {
        var list = value.toList();
        return new GSLTuple(stream().filter(list::contains).toArray(GSLValue[]::new), true);
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
        return (v = tuple[index.intValue()]) == null ? NULL : v;
    }
    @Override public final GSLValue operatorGet(int index)
    {
        GSLValue v;
        return (v = tuple[index]) == null ? NULL : v;
    }

    @Override
    public GSLValue operatorGetProperty(String name)
    {
        switch(name)
        {
            default: return NULL;
            case "contains": return CONTAINS;
            case "get": return GET;
            case "indexOf": return INDEX_OF;
            case "isEmpty": return EMPTY;
            case "lastIndexOf": return LAST_INDEX_OF;
            case "size": return SIZE;
            case "sort": return SORT;
            case "subTuple": return SUB;
        }
    }

    @Override
    public final GSLValue operatorCall(GSLValue self, GSLVarargs args) { throw new UnsupportedOperatorException(this, "()"); }
    
    @Override
    public final GSLValue operatorNew(GSLVarargs args) { throw new UnsupportedOperatorException(this, "new"); }

    @Override
    public final GSLValue operatorIterator()
    {
        return Def.<GSLValue>iterator(new Iterator<>()
        {
            private int it;
            @Override public final boolean hasNext() { return it < tuple.length; }
            @Override public final GSLValue next() { return tuple[it++]; }

        });
    }
    @Override public final boolean hasNext() { throw new UnsupportedOperatorException(this, "hasNext"); }
    @Override public final GSLValue next() { throw new UnsupportedOperatorException(this, "next"); }

    @Override public final boolean equals(Object o)
    {
        return o instanceof GSLTuple &&
                Arrays.equals(tuple, ((GSLTuple) o).tuple);
    }
    @Override public final int hashCode() { return Arrays.hashCode(tuple); }
    
    private static final class Builder implements ImmutableContainerBuilder<GSLValue, GSLTuple>
    {
        private final LinkedList<GSLValue> values = new LinkedList<>();
        
        @Override
        public final ImmutableContainerBuilder<GSLValue, GSLTuple> addElement(GSLValue element)
        {
            if(element == null)
                throw new NullPointerException();
            values.add(element);
            return this;
        }

        @Override
        public final GSLTuple build() { return new GSLTuple(values.toArray(GSLValue[]::new), true); }
        
    }
    public static final ImmutableContainerBuilder<GSLValue, GSLTuple> builder() { return new Builder(); }
    
    private static final GSLValue CONTAINS = Def.<GSLTuple>boolMethod((self, args) -> List.of(self.tuple).contains(args.arg0()));
    
    private static final GSLValue GET = Def.<GSLTuple>method((self, args) -> {
        GSLValue v;
        return (v = self.tuple[args.arg0().intValue()]) == null ? args.arg1() : v;
    });
    
    private static final GSLValue INDEX_OF = Def.<GSLTuple>intMethod((self, args) -> self.toList().indexOf(args.arg0()));
    
    private static final GSLValue EMPTY = Def.<GSLTuple>boolMethod((self, args) -> self.tuple.length < 1);
    
    private static final GSLValue LAST_INDEX_OF = Def.<GSLTuple>intMethod((self, args) -> self.toList().lastIndexOf(args.arg0()));
    
    private static final GSLValue SIZE = Def.<GSLTuple>intMethod((self, args) -> self.tuple.length);
    
    private static final GSLValue SORT = Def.<GSLTuple>method((self, args) -> {
        var other = new LinkedList<>(List.of(self.tuple));
        other.sort(Utils.defaultComparator(args.arg0()));
        return new GSLTuple(other.toArray(GSLValue[]::new), true);
    });
    
    private static final GSLValue SUB = Def.<GSLTuple>method((self, args) -> {
        if(args.numberOfArguments() > 1)
            return new GSLTuple(Arrays.copyOfRange(self.tuple, args.arg0().intValue(), args.arg1().intValue()), true);
        return new GSLTuple(Arrays.copyOfRange(self.tuple, args.arg0().intValue(), self.tuple.length), true);
    });
}
