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
    
    
    @Override public final GSLInteger    operatorCastInteger() { return new GSLInteger(hashCode()); }
    @Override public final GSLFloat      operatorCastFloat() { return new GSLFloat(hashCode()); }
    @Override public final GSLBoolean    operatorCastBoolean() { return boolValue() ? TRUE : FALSE; }
    @Override public final GSLString     operatorCastString() { return new GSLString(toString()); }
    @Override public final GSLConstTuple operatorCastConstTuple() { return new GSLConstTuple(Utils.constMapToConstArray(table)); }
    @Override public final GSLConstMap   operatorCastConstMap() { return this; }
    @Override public final GSLFunction   operatorCastFunction() { return Utils.autoGetter(this); }
    @Override public final GSLList       operatorCastList() { return new GSLList(Utils.constMapToList(table)); }
    @Override public final GSLTuple      operatorCastTuple() { return new GSLTuple(Utils.constMapToArray(table)); }
    @Override public final GSLMap        operatorCastMap() { return new GSLMap(Utils.constMapToMap(table)); }
    @Override public final GSLStruct     operatorCastStruct() { return Utils.structOf(this); }
    @Override public final GSLBlueprint  operatorCastBlueprint() { return Utils.blueprintOf(this); }
    @Override public final GSLObject     operatorCastObject() { return Utils.objectOf(this); }
    @Override public final GSLIterator   operatorCastIterator() { return Utils.oneIter(this); }
    @Override public final GSLRawBytes   operatorCastRawBytes() { return Utils.mapToBytes(table); }
    

    @Override public final GSLImmutableValue operatorEquals(GSLValue value) { return equals(value) ? TRUE : FALSE; }
    @Override public final GSLImmutableValue operatorNotEquals(GSLValue value) { return equals(value) ? FALSE : TRUE; }
    @Override public final GSLImmutableValue operatorGreater(GSLValue value) { throw new UnsupportedOperatorException(this, ">"); }
    @Override public final GSLImmutableValue operatorSmaller(GSLValue value) { throw new UnsupportedOperatorException(this, "<"); }
    @Override public final GSLImmutableValue operatorGreaterEquals(GSLValue value) { throw new UnsupportedOperatorException(this, ">="); }
    @Override public final GSLImmutableValue operatorSmallerEquals(GSLValue value) { throw new UnsupportedOperatorException(this, "<="); }
    @Override public final GSLImmutableValue operatorNegate() { return boolValue() ? FALSE : TRUE; }
    @Override public final int      operatorLength() { return table.size(); }

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

    @Override public final GSLImmutableValue operatorGet(GSLValue index) { return table.getOrDefault(index, NULL); }
    @Override public final GSLImmutableValue operatorGet(int index) { return table.getOrDefault(new GSLInteger(index), NULL); }
    @Override public final GSLImmutableValue operatorPeek() { throw new UnsupportedOperatorException(this, "[]"); }

    @Override
    public GSLImmutableValue operatorGetProperty(String name)
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
    
    
    private static final GSLImmutableValue KEYS = Def.<GSLConstMap>method((self, args) -> {
        return new GSLTuple(self.table.keySet().toArray(GSLValue[]::new));
    });
    
    private static final GSLImmutableValue VALUES = Def.<GSLConstMap>method((self, args) -> {
        return new GSLTuple(self.table.values().toArray(GSLValue[]::new));
    });
    
    private static final GSLImmutableValue ENTRIES = Def.<GSLConstMap>method((self, args) -> {
        return new GSLTuple(self.stream().toArray(GSLValue[]::new));
    });
    
    private static final GSLImmutableValue CONTAINS = Def.<GSLConstMap>boolMethod((self, args) -> {
        return args.arg0().isMutable() ? false : self.table.containsKey((GSLImmutableValue) args.arg0());
    });
    
    private static final GSLImmutableValue CONTAINS_V = Def.<GSLConstMap>boolMethod((self, args) -> args.arg0().isMutable() ? false : self.table.containsValue((GSLImmutableValue) args.arg0()));
    
    private static final GSLImmutableValue GET = Def.<GSLConstMap>method((self, args) -> {
        GSLImmutableValue v;
        if(args.arg0().isMutable())
            return NULL;
        return (v = self.table.get((GSLImmutableValue) args.arg0())) == null ? args.arg1() : v;
    });
    
    private static final GSLImmutableValue EMPTY = Def.<GSLConstMap>boolMethod((self, args) -> self.table.isEmpty());
    
    private static final GSLImmutableValue SIZE = Def.<GSLConstMap>intMethod((self, args) -> self.table.size());
}
