/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.compiler;

import kp.gsl.exception.CompilerError;
import kp.gsl.script.ScriptConstants;
import kp.gsl.script.bytecode.Bytecode;

/**
 *
 * @author Asus
 */
public final class RuntimeStack
{
    private int stackLen, stackUsed;
    private int allocatedVars;
    
    private void push(int amount) throws CompilerError
    {
        if(amount == 0)
            return;
        amount = amount < 0 ? -amount : amount;
        if(stackUsed + amount > stackLen)
            stackLen = stackUsed = stackUsed + amount;
        else stackUsed += amount;
        if(stackLen > ScriptConstants.MAX_STACK_LENGTH)
            throw new CompilerError("Max stack length exceded");
    }
    //public final void push() throws CompilerError { push(1); }
    
    private void pop(int amount) throws CompilerError
    {
        if(amount == 0)
            return;
        amount = amount < 0 ? -amount : amount;
        stackUsed -= amount;
        if(stackUsed < 0)
            throw new CompilerError("Stack under zero not valid.");
    }
    public final void pop() throws CompilerError { pop(1); }
    
    public final void modify(Bytecode b) throws CompilerError
    {
        var pars = b.getStackParametersCount();
        var ress = b.getStackResultsCount();
        if(pars > 0)
            pop(pars);
        if(ress > 0)
            push(ress);
    }
    public final void modifyInverse(Bytecode b) throws CompilerError
    {
        var pars = b.getStackParametersCount();
        var ress = b.getStackResultsCount();
        if(pars > 0)
            push(pars);
        if(ress > 0)
            pop(ress);
    }
    
    public final int allocateVariable(int lastIndex) throws CompilerError
    {
        if(lastIndex + 1 < allocatedVars)
            return lastIndex + 1;
        allocatedVars++;
        if(allocatedVars > ScriptConstants.MAX_VARS)
            throw new CompilerError("Max local variables exceded");
        return allocatedVars - 1;
    }
    
    
    public final int getVariableCount() { return allocatedVars; }
    public final int getMaxStackLength() { return stackLen; }
    public final boolean isStackEmpty() { return stackUsed <= 0; }
}
