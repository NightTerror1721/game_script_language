/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.bytecode;

import static kp.gsl.script.bytecode.ByteOps.b2i;
import static kp.gsl.script.bytecode.ByteOps.b2s;
import static kp.gsl.script.bytecode.ByteOps.bs2i;
import static kp.gsl.script.bytecode.ByteOps.bs2s;
import static kp.gsl.script.bytecode.ByteOps.i2b;
import static kp.gsl.script.bytecode.ByteOps.isw;

/**
 *
 * @author Asus
 */
class WordValue
{
    final byte b0, b1;
    
    public WordValue(int value)
    {
        this.b0 = i2b(value, 0);
        this.b1 = i2b(value, 1);
    }
    public WordValue(byte b0, byte b1)
    {
        this.b0 = b0;
        this.b1 = b1;
    }
    
    public final int getValue() { return bs2i(b0, b1); }
    
    public final int getByte0() { return b2i(b0); }
    public final int getByte1() { return b2i(b1); }
    
    public boolean require2Bytes() { return isw(getValue()); }
    
    @Override
    public String toString() { return require2Bytes() ? bs2s(b0, b1) : b2s(b0); }
    
    public int getByteCount() { return parameters(getValue()); }
    
    public static final int parameters(int value) { return isw(value) ? 2 : 1; }
}
