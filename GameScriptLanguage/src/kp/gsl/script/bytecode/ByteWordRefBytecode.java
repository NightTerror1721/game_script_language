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
public abstract class ByteWordRefBytecode extends OneWordBytecode<ReferenceValue>
{
    protected final ByteValue b0;
    
    public ByteWordRefBytecode(int value, int reference)
    {
        super(new ReferenceValue(reference));
        this.b0 = new ByteValue(value);
    }
    public ByteWordRefBytecode(byte b0, byte b1, byte b2)
    {
        super(new ReferenceValue(b1, b2));
        this.b0 = new ByteValue(b0);
    }
    
    @Override public int getExtraBytesCount() { return word.getByteCount() + 1; }
    @Override public int getExtraByte(int index)
    {
        switch(index)
        {
            default:
            case 0: return b0.getByte0();
            case 1: return word.getByte0();
            case 2: return word.getByte1();
        }
    }
    @Override void buildDescription(ValueBuilder builder)
    {
        builder.append(b0).append(word);
    }
    
}
