/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lib;

import java.util.HashMap;

/**
 *
 * @author Asus
 */
public class AbstractGSLLibrary implements GSLLibrary
{
    protected final String name;
    protected final HashMap<String, GSLLibraryElement> elements = new HashMap<>();
    
    public AbstractGSLLibrary(String name)
    {
        if(name == null)
            throw new NullPointerException();
        if(name.isBlank())
            throw new IllegalArgumentException("Library name cannot be empty");
        this.name = name;
    }

    @Override
    public String getLibraryName() { return name; }

    @Override
    public GSLLibraryElement getLibraryElement(String name) { return elements.get(name); }

    @Override
    public boolean hasLibraryElement(String name) { return elements.containsKey(name); }
}
