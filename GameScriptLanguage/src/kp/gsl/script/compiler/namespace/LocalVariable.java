/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.compiler.namespace;

import java.util.Objects;
import kp.gsl.script.compiler.parser.DataType;

/**
 *
 * @author Asus
 */
public class LocalVariable extends NamespaceIdentifier
{
    private final DataType type;
    private final int index;
    
    public LocalVariable(String name, DataType type, int index)
    {
        super(name);
        this.type = Objects.requireNonNull(type);
        this.index = index;
    }
    
    @Override public boolean isLocalVariable() { return true; }
    
    @Override public int getIndex() { return index; }
    @Override public DataType getDataType() { return type; }
    @Override public boolean needInherition() { return true; }
}
