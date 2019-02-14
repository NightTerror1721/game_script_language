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
public abstract class WordRefByteBytecode extends ByteWordRefBytecode
{
    public WordRefByteBytecode(int reference, int value) { super(value, reference); }
    public WordRefByteBytecode(byte b0, byte b1, byte b2) { super(b2, b0, b1); }
    
    @Override public int getExtraByte(int index)
    {
        switch(index)
        {
            default:
            case 0: return word.getByte0();
            case 1: return word.require2Bytes() ? word.getByte1() : b0.getByte0();
            case 2: return b0.getByte0();
        }
    }
    @Override void buildDescription(ValueBuilder builder)
    {
        builder.append(word).append(b0);
    }
}
