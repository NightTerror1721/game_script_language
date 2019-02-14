/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.bytecode;

/**
 *
 * @author Asus
 */
public abstract class BranchBytecode extends NoParsBytecode
{
    protected BranchTarget target;
    
    public BranchBytecode(int position) { this.target = new PositionValue(position); }
    public BranchBytecode(byte b0, byte b1) { this.target = new PositionValue(b0, b1); }
    public BranchBytecode(BranchTarget target) { this.target = target; }
    
    @Override public int getExtraBytesCount() { return 2; }
    @Override public int getExtraByte(int index)
    {
        var word = target.getPosition();
        return index == 0 ? word.getByte0() : word.getByte1();
    }
    @Override void buildDescription(ValueBuilder builder)
    {
        builder.append(target.getPosition());
    }
    
    @Override public final String getName() { return getName(target.getPosition().require2Bytes()); }
    
    @Override public final byte inst() { return inst(target.getPosition().require2Bytes()); }
    
    protected abstract byte inst(boolean is16);
    protected abstract String getName(boolean is16);
    
    @Override
    public final boolean isBranch() { return true; }
    
    @Override
    public final void setTargetBranchPosition(int position) { this.target = new PositionValue(position); }
    
    @Override
    public final void setTargetBranchPosition(BranchTarget target) { this.target = target; }
}
