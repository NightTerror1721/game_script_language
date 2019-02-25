/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import kp.gsl.exception.GSLRuntimeException;
import kp.gsl.exception.UnsupportedOperatorException;
import kp.gsl.lang.AbstractObject.Property;

/**
 *
 * @author Asus
 */
public final class GSLStruct extends AbstractObject
{
    public GSLStruct(GSLValue parent, String... fields)
    {
        super(Stream.of(fields).collect(Utils.linkedHashMapCollector(Function.identity(), Property::new)), parent);
    }
    /*public GSLStruct(GSLValue parent, Map<?, GSLValue> map)
    {
        super(map.entrySet().stream().collect(Utils.linkedHashMapCollector(
                e -> e.getKey().toString(),
                e -> new Property(e.getKey().toString(), e.getValue().boolValue()))), parent);
    }*/
    public GSLStruct(GSLValue parent, Map<String, Property> structMap)
    {
        super(structMap.values().stream().collect(Utils.linkedHashMapCollector(Property::getName, Property::copy)), parent);
    }
    public GSLStruct(GSLStruct struct) { this(struct.parent, struct.props); }
    
    public GSLStruct(String... fields) { this(null, fields); }
    //public GSLStruct(Map<?, GSLValue> map) { this(null, map); }
    public GSLStruct(LinkedHashMap<String, Property> structMap) { this(null, structMap); }
    
    private GSLStruct(AbstractObject model) { super(model.props, model.parent); }
    
    public static final GSLStruct assimilate(AbstractObject obj)
    {
        return new GSLStruct(obj);
    }
    
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.STRUCT; }
    
    @Override
    public final GSLStruct cast() { return this; }
    
    
    @Override public final GSLStruct     operatorCastStruct() { return this; }
    @Override public final GSLBlueprint  operatorCastBlueprint() { return Utils.blueprintOf(this); }
    @Override public final GSLObject     operatorCastObject() { return Utils.objectOf(this); }
    
    
    @Override
    public void operatorSetProperty(String name, GSLValue value)
    {
        Property prop = props.getOrDefault(name, null);
        if(prop == null)
            throw new GSLRuntimeException("Cannot create new properties in struct object");
        prop.setValue(value);
    }
    
    @Override
    public final void operatorDelProperty(String name) { throw new UnsupportedOperatorException(this, "delete"); }
}
