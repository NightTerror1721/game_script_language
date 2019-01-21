/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    
    public static final Map<GSLValue, GSLValue> collectionToMap(Collection<GSLValue> c)
    {
        var idx = 0;
        var map = new LinkedHashMap<GSLValue, GSLValue>(c.size());
        for(var v : c)
            map.put(new GSLInteger(idx++), v);
        return map;
    }
    
    public static final GSLValue[] mapToArray(Map<GSLValue, GSLValue> map)
    {
        
    }
}
