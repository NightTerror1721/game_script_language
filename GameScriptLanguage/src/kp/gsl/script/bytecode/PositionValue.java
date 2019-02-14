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
public final class PositionValue extends WordValue implements BranchTarget
{
    public PositionValue(int value) { super(value); }
    public PositionValue(byte b0, byte b1) { super(b0, b1); }
    
    @Override
    public final String toString()
    {
        if(b1 == 0)
            return "0x" + (b0 & 0xff);
        return "0x" + ((b0 & 0xff) | ((b1 & 0xff) << 8));
    }
    
    @Override
    public final boolean require2Bytes() { return true; }
    
    @Override
    public final int getByteCount() { return 2; }

    @Override
    public final PositionValue getPosition() { return this; }
}
