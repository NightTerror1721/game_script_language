/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.compiler.namespace;

/**
 *
 * @author Asus
 */
public class GlobalVariable extends NamespaceIdentifier
{
    public GlobalVariable(String name)
    {
        super(name);
    }
    
    @Override public boolean isGlobalVariable() { return true; }
}
