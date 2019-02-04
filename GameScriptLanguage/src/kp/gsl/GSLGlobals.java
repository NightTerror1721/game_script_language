/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl;

import kp.gsl.lang.GSLValue;

/**
 *
 * @author Asus
 */
public interface GSLGlobals
{
    GSLValue getGlobalValue(String name);
    GSLValue setGlobalValue(String name, GSLValue value);
    
    default boolean hasGlobalValue(String name) { return getGlobalValue(name) != GSLValue.NULL; }
}
