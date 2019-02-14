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
public abstract class TwoByteBytecode extends NoParsBytecode
{
    protected final ByteValue b0, b1;
    
    public TwoByteBytecode(int value0, int value1) { b0 = new ByteValue(value0); b1 = new ByteValue(value1); }
    public TwoByteBytecode(byte value0, byte value1) { b0 = new ByteValue(value0); b1 = new ByteValue(value1); }
    
    @Override public int getExtraBytesCount() { return 2; }
    @Override public int getExtraByte(int index) { return index == 0 ? b0.getByte0() : b1.getByte0(); }
    
    @Override void buildDescription(ValueBuilder builder) { builder.append(b0).append(b1); }
}
