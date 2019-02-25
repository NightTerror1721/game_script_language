/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.compiler.namespace;

import kp.gsl.lib.GSLLibraryElement;
import kp.gsl.script.compiler.parser.DataType;

/**
 *
 * @author Asus
 */
public abstract class NamespaceIdentifier
{
    private final String name;
    
    protected NamespaceIdentifier(String name)
    {
        this.name = name;
    }
    
    public boolean isLocalVariable() { return false; }
    public boolean isGlobalVariable() { return false; }
    public boolean isFunction() { return false; }
    public boolean isConstant() { return false; }
    public boolean isArgument() { return false; }
    public boolean isLibraryElement() { return false; }
    public boolean isLibraryContainer() { return false; }
    
    public final String getName() { return name; }
    public int getIndex() { return -1; }
    public DataType getDataType() { return DataType.ANY; }
    public boolean isReassignable() { return true; }
    public boolean needInherition() { return false; }
    public GSLLibraryElement getLibraryElement() { return null; }
}
