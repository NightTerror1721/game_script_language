/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kp.gsl.exception.NotPointerException;
import kp.gsl.exception.UnsupportedOperatorException;
import kp.gsl.lib.Def;

/**
 *
 * @author Asus
 */
public final class GSLMap extends GSLValue
{
    final Map<GSLValue, GSLValue> map;
    
    public GSLMap(Map<GSLValue, GSLValue> map)
    {
        if(map == null)
            throw new NullPointerException();
        this.map = map;
    }
    public GSLMap() { this.map = new HashMap<>(); }
    
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.MAP; }

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
    public final boolean boolValue() { return !map.isEmpty(); }

    @Override
    public final String toString() { return map.toString(); }

    @Override
    public final GSLValue[] toArray() { return stream().toArray(GSLValue[]::new); }

    @Override
    public final List<GSLValue> toList() { return stream().collect(Collectors.toList()); }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return map; }
    
    @Override
    public final Stream<GSLValue> stream()
    {
        return map.entrySet().stream().map(e -> new GSLTuple(new GSLValue[] { e.getKey(), e.getValue() }));
    }
    
    @Override
    public final boolean isMutable() { return true; }
    
    @Override
    public final GSLMap cast() { return this; }
    
    
    @Override public final GSLInteger    operatorCastInteger() { return new GSLInteger(hashCode()); }
    @Override public final GSLFloat      operatorCastFloat() { return new GSLFloat(hashCode()); }
    @Override public final GSLBoolean    operatorCastBoolean() { return boolValue() ? TRUE : FALSE; }
    @Override public final GSLString     operatorCastString() { return new GSLString(toString()); }
    @Override public final GSLConstTuple operatorCastConstTuple() { return new GSLConstTuple(Utils.mapToConstArray(map)); }
    @Override public final GSLConstMap   operatorCastConstMap() { return new GSLConstMap(Utils.toImmutable(map)); }
    @Override public final GSLFunction   operatorCastFunction() { return Utils.autoGetter(this); }
    @Override public final GSLList       operatorCastList() { return new GSLList(Utils.mapToList(map)); }
    @Override public final GSLTuple      operatorCastTuple() { return new GSLTuple(Utils.mapToArray(map)); }
    @Override public final GSLMap        operatorCastMap() { return this; }
    @Override public final GSLStruct     operatorCastStruct() { return Utils.structOf(this); }
    @Override public final GSLBlueprint  operatorCastBlueprint() { return Utils.blueprintOf(this); }
    @Override public final GSLObject     operatorCastObject() { return Utils.objectOf(this); }
    @Override public final GSLIterator   operatorCastIterator() { return Utils.oneIter(this); }
    @Override public final GSLRawBytes   operatorCastRawBytes() { return Utils.mapToBytes(map); }
    

    @Override public final GSLValue operatorEquals(GSLValue value) { return equals(value) ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return equals(value) ? FALSE : TRUE; }
    @Override public final GSLValue operatorGreater(GSLValue value) { throw new UnsupportedOperatorException(this, ">"); }
    @Override public final GSLValue operatorSmaller(GSLValue value) { throw new UnsupportedOperatorException(this, "<"); }
    @Override public final GSLValue operatorGreaterEquals(GSLValue value) { throw new UnsupportedOperatorException(this, ">="); }
    @Override public final GSLValue operatorSmallerEquals(GSLValue value) { throw new UnsupportedOperatorException(this, "<="); }
    @Override public final GSLValue operatorNegate() { return boolValue() ? FALSE : TRUE; }
    @Override public final int      operatorLength() { return map.size(); }

    @Override public final GSLValue operatorPlus(GSLValue value)
    {
        var nmap = new HashMap<>(map);
        nmap.putAll(value.toMap());
        return new GSLMap(nmap);
    }
    @Override public final GSLValue operatorMinus(GSLValue value)
    {
        var other = value.toMap();
        return new GSLMap(map.entrySet().stream()
                .filter(e -> !other.containsKey(e.getKey()))
                .collect(Utils.linkedHashMapCollector(e -> e.getKey(), e -> e.getValue())));
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

    @Override public final GSLValue operatorGet(GSLValue index) { return map.getOrDefault(index, NULL); }
    @Override public final GSLValue operatorGet(int index) { return map.getOrDefault(new GSLInteger(index), NULL); }
    @Override public final void operatorSet(GSLValue index, GSLValue value) { map.put(index, value); }
    @Override public final void operatorSet(int index, GSLValue value) { map.put(new GSLInteger(index), value); }
    @Override public final void operatorAdd(GSLValue value) { throw new UnsupportedOperatorException(this, "[]="); }
    @Override public final GSLValue operatorPeek() { throw new UnsupportedOperatorException(this, "[]"); }

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
            case "clear": return CLEAR;
            case "isEmpty": return EMPTY;
            case "get": return GET;
            case "put": return PUT;
            case "putAll": return PUT_ALL;
            case "remove": return REMOVE;
            case "size": return SIZE;
        }
    }
    @Override
    public void operatorSetProperty(String name, GSLValue value) { throw new UnsupportedOperatorException(this, ".="); }
    @Override
    public void operatorDelProperty(String name) { throw new UnsupportedOperatorException(this, "delete"); }

    @Override
    public final GSLValue operatorCall(GSLValue self, GSLVarargs args) { throw new UnsupportedOperatorException(this, "()"); }
    
    @Override
    public final GSLValue operatorNew(GSLVarargs args) { throw new UnsupportedOperatorException(this, "new"); }
    
    @Override public final GSLValue operatorReferenceGet() { throw new NotPointerException(this); }
    @Override public final void     operatorReferenceSet(GSLValue value) { throw new NotPointerException(this); }

    @Override
    public final GSLValue operatorIterator()
    {
        return Def.<GSLValue>iterator(new Iterator<>()
        {
            private final Iterator<Map.Entry<GSLValue, GSLValue>> it = map.entrySet().iterator();
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
                map.equals(((GSLMap) o).map);
    }
    @Override public final int hashCode() { return map.hashCode(); }
    
    
    private static final GSLValue KEYS = Def.<GSLMap>method((self, args) -> new GSLTuple(self.map.keySet().toArray(GSLValue[]::new)));
    
    private static final GSLValue VALUES = Def.<GSLMap>method((self, args) -> new GSLTuple(self.map.values().toArray(GSLValue[]::new)));
    
    private static final GSLValue ENTRIES = Def.<GSLMap>method((self, args) -> new GSLTuple(self.stream().toArray(GSLValue[]::new)));
    
    private static final GSLValue CONTAINS = Def.<GSLMap>boolMethod((self, args) -> self.map.containsKey(args.arg0()));
    
    private static final GSLValue CONTAINS_V = Def.<GSLMap>boolMethod((self, args) -> self.map.containsValue(args.arg0()));
    
    private static final GSLValue CLEAR = Def.<GSLMap>voidMethod((self, args) -> self.map.clear());
    
    private static final GSLValue GET = Def.<GSLMap>method((self, args) -> self.map.getOrDefault(args.arg0(), args.arg1()));
    
    private static final GSLValue EMPTY = Def.<GSLMap>boolMethod((self, args) -> self.map.isEmpty());
    
    private static final GSLValue SIZE = Def.<GSLMap>intMethod((self, args) -> self.map.size());
    
    private static final GSLValue PUT = Def.<GSLMap>method((self, args) -> self.map.put(args.arg0(), args.arg1()));
    
    private static final GSLValue PUT_ALL = Def.<GSLMap>voidMethod((self, args) -> self.map.putAll(args.arg0().toMap()));
    
    private static final GSLValue REMOVE = Def.<GSLMap>method((self, args) -> self.map.remove(args.arg0()));
}
