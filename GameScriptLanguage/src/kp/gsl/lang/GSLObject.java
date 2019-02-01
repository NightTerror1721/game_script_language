/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.LinkedHashMap;
import java.util.Map;
import kp.gsl.lang.AbstractObject.Property;

/**
 *
 * @author Asus
 */
public final class GSLObject extends AbstractObject<Map<String, Property>>
{
    public GSLObject(Map<String, Property> props, GSLValue parent)
    {
        super(props, parent);
    }
    public GSLObject(Map<String, Property> props) { super(props, null); }
    public GSLObject(GSLValue parent) { super(new LinkedHashMap<>(), parent); }
    public GSLObject() { super(new LinkedHashMap<>(), null); }

    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.OBJECT; }
    
    @Override
    public final GSLObject cast() { return this; }
    
    @Override public final GSLStruct     operatorCastStruct() { return Utils.structOf(this); }
    @Override public final GSLBlueprint  operatorCastBlueprint() { return Utils.blueprintOf(this); }
    @Override public final GSLObject     operatorCastObject() { return this; }
}
