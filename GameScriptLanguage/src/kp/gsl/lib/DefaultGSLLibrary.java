/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lib;

/**
 *
 * @author Asus
 */
public class DefaultGSLLibrary extends AbstractGSLLibrary
{
    public DefaultGSLLibrary(String name) { super(name); }
    
    public DefaultGSLLibrary registerLibraryElement(GSLLibraryElement element)
    {
        GSLLibrary.linkToLib(element, this);
        elements.put(name, element);
        return this;
    }
    
    public final GSLLibraryElement unregisterLibraryElement(String name)
    {
        var element = elements.remove(name);
        if(element != null)
            GSLLibrary.unlinkToLib(element);
        return element;
    }
}
