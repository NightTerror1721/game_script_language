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
public abstract class OneByteBytecode extends NoParsBytecode
{
    protected final ByteValue b0;
    
    public OneByteBytecode(int value) { b0 = new ByteValue(value); }
    public OneByteBytecode(byte value) { b0 = new ByteValue(value); }
    
    @Override public int getExtraBytesCount() { return 1; }
    @Override public int getExtraByte(int index) { return b0.getByte0(); }
    
    @Override void buildDescription(ValueBuilder builder) { builder.append(b0); }
}
