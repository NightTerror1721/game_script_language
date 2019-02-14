/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.bytecode;

import static kp.gsl.script.bytecode.ByteOps.i2b;

/**
 *
 * @author Asus
 */
public abstract class Bytecode
{
    public Bytecode() {}
    
    public final int getBytesCount() { return getExtraBytesCount() + 1; }
    
    public final void build(byte[] code, int offset)
    {
        code[offset] = i2b(getInstruction());
        var len = getExtraBytesCount();
        if(len < 1)
            return;
        for(var i = 0; i < len; i++)
            code[offset + i] = (byte) (getExtraByte(i) & 0xff);
    }
    
    public final String getDescription()
    {
        var sb = new StringBuilder(getName());
        var len = getExtraBytesCount();
        if(len >= 1)
        {
            var builder = new ValueBuilder();
            buildDescription(builder);
            sb.append(builder);
        }
        return sb.toString();
    }
    
    public boolean isBranch() { return false; }
    public void setTargetBranchPosition(int position) { throw new IllegalStateException(); }
    public void setTargetBranchPosition(BranchTarget target) { throw new IllegalStateException(); }
    
    public abstract int getInstruction();
    
    public abstract String getName();
    
    public abstract int getStackParametersCount();
    public abstract int getStackResultsCount();
    
    public abstract int getExtraBytesCount();
    public abstract int getExtraByte(int index);
    
    abstract void buildDescription(ValueBuilder builder);
}
