/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.LinkedList;
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
public final class GSLList extends GSLValue
{
    final List<GSLValue> list;
    
    public GSLList(List<GSLValue> list)
    {
        if(list == null)
            throw new NullPointerException();
        this.list = list;
    }
    public GSLList() { this.list = new LinkedList<>(); }
    
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.LIST; }

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
    public final boolean boolValue() { return !list.isEmpty(); }

    @Override
    public final String toString() { return list.toString(); }

    @Override
    public final GSLValue[] toArray() { return list.toArray(GSLValue[]::new); }

    @Override
    public final List<GSLValue> toList() { return list; }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return Utils.collectionToMap(list); }
    
    @Override
    public final Stream<GSLValue> stream() { return list.stream(); }
    
    @Override
    public final GSLList cast() { return this; }
    
    
    @Override public final GSLInteger    operatorCastInteger() { return new GSLInteger(hashCode()); }
    @Override public final GSLFloat      operatorCastFloat() { return new GSLFloat(hashCode()); }
    @Override public final GSLBoolean    operatorCastBoolean() { return boolValue() ? TRUE : FALSE; }
    @Override public final GSLString     operatorCastString() { return new GSLString(toString()); }
    @Override public final GSLConstTuple operatorCastConstTuple() { return new GSLConstTuple(Utils.toImmutableFromList(list)); }
    @Override public final GSLConstMap   operatorCastConstMap() { return new GSLConstMap(Utils.listToConstMap(list)); }
    @Override public final GSLFunction   operatorCastFunction() { return Utils.autoGetter(this); }
    @Override public final GSLList       operatorCastList() { return this; }
    @Override public final GSLTuple      operatorCastTuple() { return new GSLTuple(list.toArray(GSLValue[]::new)); }
    @Override public final GSLMap        operatorCastMap() { return new GSLMap(Utils.collectionToMap(list)); }
    @Override public final GSLStruct     operatorCastStruct() { return Utils.structOf(this); }
    @Override public final GSLBlueprint  operatorCastBlueprint() { return Utils.blueprintOf(this); }
    @Override public final GSLObject     operatorCastObject() { return Utils.objectOf(this); }
    @Override public final GSLIterator   operatorCastIterator() { return Utils.oneIter(this); }
    @Override public final GSLRawBytes   operatorCastRawBytes() { return Utils.listToBytes(list); }
    
    
    @Override
    public final boolean isMutable() { return true; }

    @Override public final GSLValue operatorEquals(GSLValue value) { return equals(value) ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return equals(value)  ? FALSE : TRUE; }
    @Override public final GSLValue operatorGreater(GSLValue value) { throw new UnsupportedOperatorException(this, ">"); }
    @Override public final GSLValue operatorSmaller(GSLValue value) { throw new UnsupportedOperatorException(this, "<"); }
    @Override public final GSLValue operatorGreaterEquals(GSLValue value) { throw new UnsupportedOperatorException(this, ">="); }
    @Override public final GSLValue operatorSmallerEquals(GSLValue value) { throw new UnsupportedOperatorException(this, "<="); }
    @Override public final GSLValue operatorNegate() { return list.isEmpty() ? TRUE : FALSE; }
    @Override public final int      operatorLength() { return list.size(); }

    @Override public final GSLValue operatorPlus(GSLValue value)
    {
        return new GSLList(Stream.concat(stream(), value.stream()).collect(Collectors.toList()));
    }
    @Override public final GSLValue operatorMinus(GSLValue value)
    {
        var other = value.toList();
        return new GSLList(stream().filter(v -> !other.contains(v)).collect(Collectors.toList()));
    }
    @Override public final GSLValue operatorMultiply(GSLValue value) { throw new UnsupportedOperatorException(this, "*"); }
    @Override public final GSLValue operatorDivide(GSLValue value) { throw new UnsupportedOperatorException(this, "/"); }
    @Override public final GSLValue operatorRemainder(GSLValue value) { throw new UnsupportedOperatorException(this, "%"); }
    @Override public final GSLValue operatorIncrease() { throw new UnsupportedOperatorException(this, "++"); }
    @Override public final GSLValue operatorDecrease() { throw new UnsupportedOperatorException(this, "--"); }
    @Override public final GSLValue operatorNegative() { throw new UnsupportedOperatorException(this, "-()"); }

    @Override public final GSLValue operatorBitwiseShiftLeft(GSLValue value)
    {
        list.add(value);
        return this;
    }
    @Override public final GSLValue operatorBitwiseShiftRight(GSLValue value)
    {
        list.remove(value);
        return this;
    }
    @Override public final GSLValue operatorBitwiseAnd(GSLValue value) { throw new UnsupportedOperatorException(this, "&"); }
    @Override public final GSLValue operatorBitwiseOr(GSLValue value) { throw new UnsupportedOperatorException(this, "|"); }
    @Override public final GSLValue operatorBitwiseXor(GSLValue value) { throw new UnsupportedOperatorException(this, "^"); }
    @Override public final GSLValue operatorBitwiseNot() { throw new UnsupportedOperatorException(this, "~"); }

    @Override public final GSLValue operatorGet(GSLValue index)
    {
        return list.get(index.intValue());
    }
    @Override public final GSLValue operatorGet(int index)
    {
        return list.get(index);
    }
    @Override public final void operatorSet(GSLValue index, GSLValue value)
    {
        list.set(index.intValue(), value);
    }
    @Override public final void operatorSet(int index, GSLValue value)
    {
        list.set(index, value);
    }

    @Override
    public GSLValue operatorGetProperty(String name)
    {
        switch(name)
        {
            default: return NULL;
            case "add": return ADD;
            case "addAll": return ADD_ALL;
            case "clear": return CLEAR;
            case "contains": return CONTAINS;
            case "isEmpry": return EMPTY;
            case "get": return GET;
            case "indexOf": return INDEX_OF;
            case "lastIndexOf": return LAST_INDEX_OF;
            case "remove": return REMOVE;
            case "removeIdx": return REMOVE_IDX;
            case "set": return SET;
            case "size": return SIZE;
            case "sort": return SORT;
            case "subList": return SUB;
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
    public final GSLValue operatorIterator() { return Def.iterator(list.iterator()); }
    @Override public final boolean hasNext() { throw new UnsupportedOperatorException(this, "hasNext"); }
    @Override public final GSLValue next() { throw new UnsupportedOperatorException(this, "next"); }

    @Override public final boolean equals(Object o)
    {
        return o instanceof GSLList &&
                list.equals(((GSLList) o).list);
    }
    @Override public final int hashCode() { return list.hashCode(); }
    
    
    
    private static final GSLValue ADD = Def.<GSLList>boolMethod((self, args) -> {
        if(args.numberOfArguments() > 1)
        {
            self.list.add(args.arg0().intValue(), args.arg1());
            return true;
        }
        return self.list.add(args.arg0());
    });
    private static final GSLValue ADD_ALL = Def.<GSLList>boolMethod((self, args) -> {
        if(args.numberOfArguments() > 1)
            return self.list.addAll(args.arg0().intValue(), args.arg1().toList());
        return self.list.addAll(args.arg0().toList());
    });
    
    private static final GSLValue CLEAR = Def.<GSLList>voidMethod((self, args) -> self.list.clear());
    private static final GSLValue CONTAINS = Def.<GSLList>boolMethod((self, args) -> self.list.contains(args.arg0()));
    
    private static final GSLValue GET = Def.<GSLList>method((self, args) -> {
        GSLValue v;
        return (v = self.list.get(args.arg0().intValue())) == null ? args.arg1() : v;
    });
    
    private static final GSLValue INDEX_OF = Def.<GSLList>intMethod((self, args) -> self.list.indexOf(args.arg0()));
    
    private static final GSLValue EMPTY = Def.<GSLList>boolMethod((self, args) -> self.list.isEmpty());
    
    private static final GSLValue LAST_INDEX_OF = Def.<GSLList>intMethod((self, args) -> self.list.lastIndexOf(args.arg0()));
    
    private static final GSLValue SIZE = Def.<GSLList>intMethod((self, args) -> self.list.size());
    
    private static final GSLValue REMOVE = Def.<GSLList>boolMethod((self, args) -> self.list.remove(args.arg0()));
    private static final GSLValue REMOVE_IDX = Def.<GSLList>method((self, args) -> {
        GSLValue v;
        return (v = self.list.remove(args.arg0().intValue())) == null ? NULL : v;
    });
    
    private static final GSLValue SET = Def.<GSLList>method((self, args) -> self.list.set(args.arg0().intValue(), args.arg1()));
    
    private static final GSLValue SORT = Def.<GSLList>method((self, args) -> { self.list.sort(Utils.defaultComparator(args.arg0())); return self; });
    
    private static final GSLValue SUB = Def.<GSLList>method((self, args) -> {
        if(args.numberOfArguments() > 1)
            return new GSLList(self.list.subList(args.arg0().intValue(), args.arg1().intValue()));
        return new GSLList(self.list.subList(args.arg0().intValue(), self.list.size()));
    });
}
