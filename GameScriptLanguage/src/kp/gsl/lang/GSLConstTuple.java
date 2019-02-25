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
public final class GSLConstTuple extends GSLImmutableValue
{
    private final GSLImmutableValue[] tuple;
    
    public GSLConstTuple(GSLImmutableValue[] array)
    {
        if(array == null)
            throw new NullPointerException();
        this.tuple = array;
    }
    
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.CONST_TUPLE; }

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
    public final GSLConstTuple cast() { return this; }
    
    
    @Override public final GSLInteger    operatorCastInteger() { return new GSLInteger(hashCode()); }
    @Override public final GSLFloat      operatorCastFloat() { return new GSLFloat(hashCode()); }
    @Override public final GSLBoolean    operatorCastBoolean() { return boolValue() ? TRUE : FALSE; }
    @Override public final GSLString     operatorCastString() { return new GSLString(toString()); }
    @Override public final GSLConstTuple operatorCastConstTuple() { return this; }
    @Override public final GSLConstMap   operatorCastConstMap() { return new GSLConstMap(Utils.constArrayToConstMap(tuple)); }
    @Override public final GSLFunction   operatorCastFunction() { return Utils.autoGetter(this); }
    @Override public final GSLList       operatorCastList() { return new GSLList(Utils.arrayToList(tuple)); }
    @Override public final GSLTuple      operatorCastTuple() { return new GSLTuple(tuple); }
    @Override public final GSLMap        operatorCastMap() { return new GSLMap(Utils.arrayToMap(tuple)); }
    @Override public final GSLStruct     operatorCastStruct() { return Utils.structOf(this); }
    @Override public final GSLBlueprint  operatorCastBlueprint() { return Utils.blueprintOf(this); }
    @Override public final GSLObject     operatorCastObject() { return Utils.objectOf(this); }
    @Override public final GSLIterator   operatorCastIterator() { return Utils.oneIter(this); }
    @Override public final GSLRawBytes   operatorCastRawBytes() { return Utils.arrayToBytes(tuple); }
    

    @Override public final GSLImmutableValue operatorEquals(GSLValue value) { return equals(value) ? TRUE : FALSE; }
    @Override public final GSLImmutableValue operatorNotEquals(GSLValue value) { return equals(value) ? FALSE : TRUE; }
    @Override public final GSLImmutableValue operatorGreater(GSLValue value) { throw new UnsupportedOperatorException(this, ">"); }
    @Override public final GSLImmutableValue operatorSmaller(GSLValue value) { throw new UnsupportedOperatorException(this, "<"); }
    @Override public final GSLImmutableValue operatorGreaterEquals(GSLValue value) { throw new UnsupportedOperatorException(this, ">="); }
    @Override public final GSLImmutableValue operatorSmallerEquals(GSLValue value) { throw new UnsupportedOperatorException(this, "<="); }
    @Override public final GSLImmutableValue operatorNegate() { return boolValue() ? FALSE : TRUE; }
    @Override public final int      operatorLength() { return tuple.length; }

    @Override public final GSLImmutableValue operatorPlus(GSLValue value)
    {
        return new GSLConstTuple(Stream.concat(stream(), value.stream().map(v -> (GSLImmutableValue) v))
                .toArray(GSLImmutableValue[]::new));
    }
    @Override public final GSLImmutableValue operatorMinus(GSLValue value)
    {
        var list = value.toList();
        return new GSLConstTuple(stream().filter(list::contains).toArray(GSLImmutableValue[]::new));
    }
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

    @Override public final GSLImmutableValue operatorGet(GSLValue index)
    {
        GSLImmutableValue v;
        return (v = tuple[index.intValue()]) == null ? NULL : v;
    }
    @Override public final GSLImmutableValue operatorGet(int index)
    {
        GSLImmutableValue v;
        return (v = tuple[index]) == null ? NULL : v;
    }
    @Override public final GSLImmutableValue operatorPeek()
    {
        GSLImmutableValue v;
        return (v = tuple[tuple.length - 1]) == null ? NULL : v;
    }

    @Override
    public GSLImmutableValue operatorGetProperty(String name)
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
        return o instanceof GSLConstTuple &&
                Arrays.equals(tuple, ((GSLConstTuple) o).tuple);
    }
    @Override public final int hashCode() { return Arrays.hashCode(tuple); }
    
    
    private static final GSLImmutableValue CONTAINS = Def.<GSLConstTuple>boolMethod((self, args) -> List.of(self.tuple).contains(args.arg0()));
    
    private static final GSLImmutableValue GET = Def.<GSLConstTuple>method((self, args) -> {
        GSLValue v;
        return (v = self.tuple[args.arg0().intValue()]) == null ? args.arg1() : v;
    });
    
    private static final GSLImmutableValue INDEX_OF = Def.<GSLConstTuple>intMethod((self, args) -> self.toList().indexOf(args.arg0()));
    
    private static final GSLImmutableValue EMPTY = Def.<GSLConstTuple>boolMethod((self, args) -> self.tuple.length < 1);
    
    private static final GSLImmutableValue LAST_INDEX_OF = Def.<GSLConstTuple>intMethod((self, args) -> self.toList().lastIndexOf(args.arg0()));
    
    private static final GSLImmutableValue SIZE = Def.<GSLConstTuple>intMethod((self, args) -> self.tuple.length);
    
    private static final GSLImmutableValue SORT = Def.<GSLConstTuple>method((self, args) -> {
        var other = new LinkedList<>(List.of(self.tuple));
        other.sort(Utils.defaultComparator(args.arg0()));
        return new GSLTuple(other.toArray(GSLValue[]::new));
    });
    
    private static final GSLImmutableValue SUB = Def.<GSLConstTuple>method((self, args) -> {
        if(args.numberOfArguments() > 1)
            return new GSLTuple(Arrays.copyOfRange(self.tuple, args.arg0().intValue(), args.arg1().intValue()));
        return new GSLTuple(Arrays.copyOfRange(self.tuple, args.arg0().intValue(), self.tuple.length));
    });
}
