/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.HashMap;
import java.util.Map;
import kp.gsl.exception.UnsupportedOperatorException;
import kp.gsl.lang.AbstractObject.Property;

/**
 *
 * @author Asus
 */
public final class GSLBlueprint extends AbstractObject<Map<String, Property>>
{
    public GSLBlueprint(Map<String, Property> props, GSLValue parent)
    {
        super(props, parent);
    }
    public GSLBlueprint(Map<String, Property> props)
    {
        super(props, null);
    }
    public GSLBlueprint(AbstractObject obj, GSLValue parent)
    {
        super(new HashMap<>(obj.props), parent);
    }
    public GSLBlueprint(AbstractObject obj)
    {
        super(new HashMap<>(obj.props), obj.parent);
    }
    
    public static final GSLBlueprint assimilate(AbstractObject obj, GSLValue parent)
    {
        return new GSLBlueprint(obj.props, parent);
    }
    
    public static final GSLBlueprint assimilate(AbstractObject obj)
    {
        return new GSLBlueprint(obj.props);
    }

    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.BLUEPRINT; }
    
    @Override
    public final GSLBlueprint cast() { return this; }
    
    
    @Override public final GSLStruct     operatorCastStruct() { return Utils.structOf(this); }
    @Override public final GSLBlueprint  operatorCastBlueprint() { return this; }
    @Override public final GSLObject     operatorCastObject() { return Utils.objectOf(this); }
    
    
    @Override
    public final void operatorSetProperty(String name, GSLValue value) { throw new UnsupportedOperatorException(this, ".="); }
    
    @Override
    public final void operatorDelProperty(String name) { throw new UnsupportedOperatorException(this, "delete"); }
}
