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
final class ReferenceValue extends WordValue
{
    public ReferenceValue(int value) { super(value); }
    public ReferenceValue(byte b0, byte b1) { super(b0, b1); }
    
    @Override
    public final String toString()
    {
        if(b1 == 0)
            return "#" + (b0 & 0xff);
        return "#" + ((b0 & 0xff) | ((b1 & 0xff) << 8));
    }
}
