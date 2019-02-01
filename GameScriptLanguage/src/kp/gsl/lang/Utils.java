/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import kp.gsl.exception.GSLRuntimeException;
import kp.gsl.lang.AbstractObject.Property;

/**
 *
 * @author Asus
 */
final class Utils
{
    private Utils() {}
    
    public static final GSLValue[] emptyArray() { return new GSLValue[] {}; }
    public static final List<GSLValue> emptyList() { return List.of(); }
    public static final Map<GSLValue, GSLValue> emptyMap() { return Map.of(); }
    
    public static final GSLValue[] arrayOf(GSLValue value) { return new GSLValue[] { value }; }
    public static final List<GSLValue> listOf(GSLValue value) { return List.of(value); }
    public static final Map<GSLValue, GSLValue> mapOf(GSLValue value)
    {
        var map = new HashMap<GSLValue, GSLValue>();
        map.put(new GSLString("scalar"), value);
        return map;
    }
    public static final GSLImmutableValue[] constArrayOf(GSLImmutableValue value) { return new GSLImmutableValue[] { value }; }
    public static final Map<GSLImmutableValue, GSLImmutableValue> constMapOf(GSLImmutableValue value)
    {
        var map = new HashMap<GSLImmutableValue, GSLImmutableValue>();
        map.put(new GSLString("scalar"), value);
        return map;
    }
    public static final GSLStruct structOf(GSLValue value)
    {
        var struct = value.toMap().entrySet().stream()
                .collect(linkedHashMapCollector(e -> e.getKey().toString(), e -> new Property(e.getKey().toString(), true, e.getValue())));
        return new GSLStruct(struct);
    }
    public static final GSLStruct structOf(AbstractObject obj) { return new GSLStruct(obj); }
    public static final GSLBlueprint blueprintOf(GSLValue value)
    {
        var bp = value.toMap().entrySet().stream()
                .collect(linkedHashMapCollector(e -> e.getKey().toString(), e -> new Property(e.getKey().toString(), true, e.getValue())));
        return new GSLBlueprint(bp);
    }
    public static final GSLBlueprint blueprintOf(AbstractObject obj) { return new GSLBlueprint(obj); }
    public static final GSLObject objectOf(GSLValue value)
    {
        var obj = value.toMap().entrySet().stream()
                .collect(linkedHashMapCollector(e -> e.getKey().toString(), e -> new Property(e.getKey().toString(), true, e.getValue())));
        return new GSLObject(obj);
    }
    public static final GSLObject objectOf(AbstractObject obj) { return new GSLObject(obj); }
    
    public static final GSLImmutableValue[] toImmutable(GSLValue[] values)
    {
        return Arrays.stream(values).map(v -> (GSLImmutableValue) v).toArray(GSLImmutableValue[]::new);
    }
    public static final List<GSLImmutableValue> toImmutable(List<GSLValue> values)
    {
        return values.stream().map(v -> (GSLImmutableValue) v).collect(Collectors.toList());
    }
    public static final Map<GSLImmutableValue, GSLImmutableValue> toImmutable(Map<GSLValue, GSLValue> values)
    {
        return values.entrySet().stream().collect(Collectors.toMap(e -> (GSLImmutableValue) e.getKey(), e -> (GSLImmutableValue) e.getValue()));
    }
    public static final Map<GSLImmutableValue, GSLImmutableValue> objToImmutable(Map<String, Property> obj)
    {
        return obj.entrySet().stream().collect(Collectors.toMap(e -> new GSLString(e.getKey()), e -> (GSLImmutableValue) e.getValue().value));
    }
    public static final GSLImmutableValue[] toImmutableFromList(List<GSLValue> values)
    {
        return values.stream().map(v -> (GSLImmutableValue) v).toArray(GSLImmutableValue[]::new);
    }
    
    public static final <V extends GSLValue> GSLIterator oneIter(final GSLValue value)
    {
        return new GSLIterator()
        {
            private boolean used;
            
            @Override
            public boolean hasNext() { return !used; }

            @Override
            public GSLValue next()
            {
                if(used)
                    return NULL;
                used = true;
                return value;
            }
        };
    }
    
    public static final Map<GSLValue, GSLValue> arrayToMap(GSLValue[] array)
    {
        if(array == null || array.length < 1)
            return new LinkedHashMap<>();
        var map = new LinkedHashMap<GSLValue, GSLValue>(array.length);
        for(var i = 0; i < array.length; i++)
            map.put(new GSLInteger(i), array[i]);
        return map;
    }
    public static final Map<GSLImmutableValue, GSLImmutableValue> arrayToConstMap(GSLValue[] array)
    {
        if(array == null || array.length < 1)
            return new LinkedHashMap<>();
        var map = new LinkedHashMap<GSLImmutableValue, GSLImmutableValue>(array.length);
        for(var i = 0; i < array.length; i++)
            map.put(new GSLInteger(i), (GSLImmutableValue) array[i]);
        return map;
    }
    
    public static final Map<GSLValue, GSLValue> iteratorToMap(Iterator<GSLValue> it)
    {
        var map = new LinkedHashMap<GSLValue, GSLValue>();
        var count = 0;
        while(it.hasNext())
            map.put(new GSLInteger(count++), it.next());
        return map;
    }
    public static final Map<GSLImmutableValue, GSLImmutableValue> iteratorToConstMap(Iterator<GSLValue> it)
    {
        var map = new LinkedHashMap<GSLImmutableValue, GSLImmutableValue>();
        var count = 0;
        while(it.hasNext())
            map.put(new GSLInteger(count++), (GSLImmutableValue) it.next());
        return map;
    }
    
    public static final Map<GSLValue, GSLValue> collectionToMap(Collection<GSLValue> c)
    {
        var idx = 0;
        var map = new LinkedHashMap<GSLValue, GSLValue>(c.size());
        for(var v : c)
            map.put(new GSLInteger(idx++), v);
        return map;
    }
    
    public static final Map<GSLImmutableValue, GSLImmutableValue> listToConstMap(List<GSLValue> c)
    {
        var idx = 0;
        var map = new LinkedHashMap<GSLImmutableValue, GSLImmutableValue>(c.size());
        for(var v : c)
            map.put(new GSLInteger(idx++), (GSLImmutableValue) v);
        return map;
    }
    
    public static final Map<GSLImmutableValue, GSLImmutableValue> constArrayToConstMap(GSLImmutableValue[] c)
    {
        var idx = 0;
        var map = new LinkedHashMap<GSLImmutableValue, GSLImmutableValue>(c.length);
        for(var v : c)
            map.put(new GSLInteger(idx++), v);
        return map;
    }
    
    public static final Map<GSLValue, GSLValue> constMapToMap(Map<GSLImmutableValue, GSLImmutableValue> map)
    {
        return new HashMap<>(map);
    }
    
    public static final GSLValue[] mapToArray(Map<GSLValue, GSLValue> map)
    {
        return map.entrySet().stream().map(e -> new GSLTuple(new GSLValue[] { e.getKey(), e.getValue() })).toArray(GSLValue[]::new);
    }
    public static final GSLImmutableValue[] mapToConstArray(Map<GSLValue, GSLValue> map)
    {
        return map.entrySet().stream()
                .map(e -> new GSLConstTuple(new GSLImmutableValue[] { (GSLImmutableValue) e.getKey(), (GSLImmutableValue) e.getValue() }))
                .toArray(GSLImmutableValue[]::new);
    }
    public static final GSLValue[] constMapToArray(Map<GSLImmutableValue, GSLImmutableValue> map)
    {
        return map.entrySet().stream().map(e -> new GSLTuple(new GSLValue[] { e.getKey(), e.getValue() })).toArray(GSLValue[]::new);
    }
    public static final GSLImmutableValue[] constMapToConstArray(Map<GSLImmutableValue, GSLImmutableValue> map)
    {
        return map.entrySet().stream()
                .map(e -> new GSLConstTuple(new GSLImmutableValue[] { e.getKey(), e.getValue() }))
                .toArray(GSLImmutableValue[]::new);
    }
    
    public static final List<GSLValue> mapToList(Map<GSLValue, GSLValue> map)
    {
        return map.entrySet().stream().map(e -> new GSLTuple(new GSLValue[] { e.getKey(), e.getValue() })).collect(Collectors.toList());
    }
    public static final List<GSLValue> constMapToList(Map<GSLImmutableValue, GSLImmutableValue> map)
    {
        return map.entrySet().stream().map(e -> new GSLTuple(new GSLValue[] { e.getKey(), e.getValue() })).collect(Collectors.toList());
    }
    
    public static final List<GSLValue> arrayToList(GSLValue[] array)
    {
        return Arrays.stream(array).collect(Collectors.toList());
    }
    
    public static final <T, K, U>
    Collector<T, ?, LinkedHashMap<K, U>> linkedHashMapCollector(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper)
    {
        return Collectors.toMap(keyMapper, valueMapper, (v0, v1) -> v1, LinkedHashMap::new);
    }
    
    public static final Comparator<GSLValue> defaultComparator(final GSLValue comparator)
    {
        return (v0, v1) -> comparator.operatorCall(GSLValue.NULL, new GSLVarargs.Pair(v0, v1)).intValue();
    }
    
    
    public static final GSLFunction autoGetter(final GSLValue value)
    {
        return new GSLFunction()
        {
            @Override
            public GSLValue operatorCall(GSLValue self, GSLVarargs args) { return value; }
        };
    }
    
    public static final GSLRawBytes listToBytes(List<GSLValue> list)
    {
        var lens = new GSLRawBytes(new byte[4]);
        try(var baos = new ByteArrayOutputStream())
        {
            lens.setInt(0, list.size());
            baos.write(lens.bytes);
            for(var e : list)
            {
                var bytes = e.operatorCastRawBytes();
                lens.setInt(0, bytes.bytes.length);
                baos.write(lens.bytes);
                baos.write(bytes.bytes);
            }
            return new GSLRawBytes(baos.toByteArray());
        }
        catch(IOException ex) { throw new GSLRuntimeException(ex); }
    }
    public static final GSLRawBytes arrayToBytes(GSLValue[] array) { return listToBytes(List.of(array)); }
    
    public static final GSLRawBytes mapToBytes(Map<? extends GSLValue, ? extends GSLValue> map)
    {
        var lens = new GSLRawBytes(new byte[4]);
        try(var baos = new ByteArrayOutputStream())
        {
            lens.setInt(0, map.size());
            baos.write(lens.bytes);
            for(var e : map.entrySet())
            {
                var bytes = e.getKey().operatorCastRawBytes();
                lens.setInt(0, bytes.bytes.length);
                baos.write(lens.bytes);
                baos.write(bytes.bytes);
                
                bytes = e.getValue().operatorCastRawBytes();
                lens.setInt(0, bytes.bytes.length);
                baos.write(lens.bytes);
                baos.write(bytes.bytes);
            }
            return new GSLRawBytes(baos.toByteArray());
        }
        catch(IOException ex) { throw new GSLRuntimeException(ex); }
    }
    
    public static final GSLRawBytes objToBytes(Map<String, Property> obj)
    {
        var lens = new GSLRawBytes(new byte[4]);
        try(var baos = new ByteArrayOutputStream())
        {
            lens.setInt(0, obj.size());
            baos.write(lens.bytes);
            for(var e : obj.entrySet())
            {
                var bytes = new GSLString(e.getKey()).operatorCastRawBytes();
                lens.setInt(0, bytes.bytes.length);
                baos.write(lens.bytes);
                baos.write(bytes.bytes);
                
                bytes = e.getValue().value.operatorCastRawBytes();
                lens.setInt(0, bytes.bytes.length);
                baos.write(lens.bytes);
                baos.write(bytes.bytes);
            }
            return new GSLRawBytes(baos.toByteArray());
        }
        catch(IOException ex) { throw new GSLRuntimeException(ex); }
    }
}
