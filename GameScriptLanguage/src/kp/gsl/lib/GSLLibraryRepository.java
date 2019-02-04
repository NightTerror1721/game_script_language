/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lib;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Asus
 */
public class GSLLibraryRepository implements Iterable<GSLLibrary>
{
    private final HashMap<String, GSLLibrary> libs = new HashMap<>();
    
    public final void registerLibrary(GSLLibrary lib)
    {
        if(libs.containsKey(lib.getLibraryName()))
            throw new IllegalArgumentException("Library " + lib.getLibraryName() + " has already exists");
        libs.put(lib.getLibraryName(), lib);
    }
    
    public final GSLLibrary getLibrary(String name) { return libs.get(name); }
    
    public final boolean hasLibrary(String name) { return libs.containsKey(name); }
    
    public final GSLLibrary removeLibrary(String name) { return libs.remove(name); }

    @Override
    public final Iterator<GSLLibrary> iterator() { return libs.values().iterator(); }
}
