/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kp.gsl.exception.UnsupportedOperatorException;
import kp.gsl.lib.Def;

/**
 *
 * @author Asus
 */
public final class GSLConstMap extends GSLImmutableValue
{
    private final Map<GSLImmutableValue, GSLImmutableValue> table;
    
    public GSLConstMap(Map<GSLImmutableValue, GSLImmutableValue> map)
    {
        this.table = map;
    }
    
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.CONST_MAP; }

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
    public final boolean boolValue() { return table.size() > 0; }

    @Override
    public final String toString() { return table.toString(); }

    @Override
    public final GSLValue[] toArray() { return stream().toArray(GSLValue[]::new); }

    @Override
    public final List<GSLValue> toList() { return stream().collect(Collectors.toList()); }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return new LinkedHashMap<>(table); }
    
    @Override
    public final Stream<GSLValue> stream()
    {
        return table.entrySet().stream().map(e -> new GSLTuple(new GSLValue[] { e.getKey(), e.getValue() }));
    }
    
    @Override
    public final GSLConstMap cast() { return this; }

    @Override public final GSLValue operatorEquals(GSLValue value) { return equals(value) ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return equals(value) ? FALSE : TRUE; }
    @Override public final GSLValue operatorGreater(GSLValue value) { throw new UnsupportedOperatorException(this, ">"); }
    @Override public final GSLValue operatorSmaller(GSLValue value) { throw new UnsupportedOperatorException(this, "<"); }
    @Override public final GSLValue operatorGreaterEquals(GSLValue value) { throw new UnsupportedOperatorException(this, ">="); }
    @Override public final GSLValue operatorSmallerEquals(GSLValue value) { throw new UnsupportedOperatorException(this, "<="); }
    @Override public final GSLValue operatorNegate() { return boolValue() ? FALSE : TRUE; }
    @Override public final int      operatorLength() { return table.size(); }

    @Override public final GSLValue operatorPlus(GSLValue value) { throw new UnsupportedOperatorException(this, "+"); }
    @Override public final GSLValue operatorMinus(GSLValue value) { throw new UnsupportedOperatorException(this, "-"); }
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

    @Override public final GSLValue operatorGet(GSLValue index) { return table.getOrDefault(index, NULL); }
    @Override public final GSLValue operatorGet(int index) { return table.getOrDefault(new GSLInteger(index), NULL); }

    @Override
    public GSLValue operatorGetProperty(String name)
    {
        switch(name)
        {
            default: return NULL;
            case "keys": return KEYS;
            case "values": return VALUES;
            case "entries": return ENTRIES;
            case "containsKey": return CONTAINS;
            case "containsValue": return CONTAINS_V;
            case "isEmpty": return EMPTY;
            case "get": return GET;
            case "size": return SIZE;
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
            private final Iterator<Map.Entry<GSLImmutableValue, GSLImmutableValue>> it = table.entrySet().iterator();
            private final GSLValue[] array = new GSLValue[2];
            private final GSLTuple result = new GSLTuple(array);
            
            @Override public final boolean hasNext() { return it.hasNext(); }
            @Override public final GSLValue next()
            {
                var next = it.next();
                if(next == null)
                    return NULL;
                array[0] = next.getKey();
                array[1] = next.getValue();
                return result;
            }

        });
    }
    @Override public final boolean hasNext() { throw new UnsupportedOperatorException(this, "hasNext"); }
    @Override public final GSLValue next() { throw new UnsupportedOperatorException(this, "next"); }

    @Override public final boolean equals(Object o)
    {
        return o instanceof GSLConstMap &&
                table.equals(((GSLConstMap) o).table);
    }
    @Override public final int hashCode() { return table.hashCode(); }
    
    
    private static final GSLValue KEYS = Def.<GSLConstMap>method((self, args) -> {
        return new GSLTuple(self.table.keySet().toArray(GSLValue[]::new));
    });
    
    private static final GSLValue VALUES = Def.<GSLConstMap>method((self, args) -> {
        return new GSLTuple(self.table.values().toArray(GSLValue[]::new));
    });
    
    private static final GSLValue ENTRIES = Def.<GSLConstMap>method((self, args) -> {
        return new GSLTuple(self.stream().toArray(GSLValue[]::new));
    });
    
    private static final GSLValue CONTAINS = Def.<GSLConstMap>boolMethod((self, args) -> {
        return args.arg0().isMutable() ? false : self.table.containsKey((GSLImmutableValue) args.arg0());
    });
    
    private static final GSLValue CONTAINS_V = Def.<GSLConstMap>boolMethod((self, args) -> args.arg0().isMutable() ? false : self.table.containsValue((GSLImmutableValue) args.arg0()));
    
    private static final GSLValue GET = Def.<GSLConstMap>method((self, args) -> {
        GSLImmutableValue v;
        if(args.arg0().isMutable())
            return NULL;
        return (v = self.table.get((GSLImmutableValue) args.arg0())) == null ? args.arg1() : v;
    });
    
    private static final GSLValue EMPTY = Def.<GSLConstMap>boolMethod((self, args) -> self.table.isEmpty());
    
    private static final GSLValue SIZE = Def.<GSLConstMap>intMethod((self, args) -> self.table.size());
}
