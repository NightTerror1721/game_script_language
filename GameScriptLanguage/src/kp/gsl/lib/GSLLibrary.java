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
public interface GSLLibrary
{
    String getLibraryName();
    GSLLibraryElement getLibraryElement(String name);
    boolean hasLibraryElement(String name);
    
    static void linkToLib(GSLLibraryElement element, GSLLibrary lib)
    {
        if(element.lib != null)
            throw new IllegalArgumentException("Element is already linked with other library.");
        element.lib = lib;
    }
    
    static void unlinkToLib(GSLLibraryElement element)
    {
        if(element.lib == null)
            throw new IllegalArgumentException("Element not linked with any library.");
        element.lib = null;
    }
}
