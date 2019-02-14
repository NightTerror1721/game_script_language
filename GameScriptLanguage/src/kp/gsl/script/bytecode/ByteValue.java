/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.bytecode;

import static kp.gsl.script.bytecode.ByteOps.b2i;
import static kp.gsl.script.bytecode.ByteOps.b2s;
import static kp.gsl.script.bytecode.ByteOps.i2b;

/**
 *
 * @author Asus
 */
final class ByteValue
{
    private final byte b0;
    
    public ByteValue(int value) { this.b0 = i2b(value); }
    public ByteValue(byte value) { this.b0 = value; }
    
    public final int getValue() { return b2i(b0); }
    
    public final int getByte0() { return b2i(b0); }
    
    @Override
    public String toString() { return b2s(b0); }
}
