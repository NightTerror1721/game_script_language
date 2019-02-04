/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lib;

import kp.gsl.GSLGlobals;
import kp.gsl.lang.GSLDataType;
import kp.gsl.lang.GSLInteger;
import kp.gsl.lang.GSLValue;
import kp.gsl.lang.GSLVarargs;

/**
 *
 * @author Asus
 */
public abstract class GSLLibraryElement
{
    final String name;
    GSLLibrary lib;
    
    public GSLLibraryElement(String name)
    {
        if(name == null)
            throw new NullPointerException();
        if(name.isBlank())
            throw new IllegalArgumentException("Library name cannot be empty");
        this.name = name;
    }
    
    public final GSLLibrary getLibrary() { return lib; }
    public final String getElementName() { return name; }
    
    public abstract GSLDataType getType();
    public abstract GSLValue    toGSLValue(GSLGlobals globals);
    
    public abstract GSLValue operatorGet(GSLGlobals globals, GSLValue index);
    public          GSLValue operatorGet(GSLGlobals globals, int index) { return operatorGet(globals, new GSLInteger(index)); }
    
    public abstract GSLValue operatorGetProperty(GSLGlobals globals, String name);
    
    public abstract GSLValue operatorCall(GSLGlobals globals, GSLValue self, GSLVarargs args);
    public abstract GSLValue operatorNew(GSLGlobals globals, GSLVarargs args);
    
}
