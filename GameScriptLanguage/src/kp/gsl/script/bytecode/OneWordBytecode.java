/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.bytecode;

import java.util.Objects;

/**
 *
 * @author Asus
 */
public abstract class OneWordBytecode<WV extends WordValue> extends NoParsBytecode
{
    protected final WV word;
    
    protected OneWordBytecode(WV word)
    {
        this.word = Objects.requireNonNull(word);
    }
        
    @Override public int getExtraBytesCount() { return word.getByteCount(); }
    @Override public int getExtraByte(int index)
    {
        return index == 0 ? word.getByte0() : word.getByte1();
    }
    @Override void buildDescription(ValueBuilder builder)
    {
        builder.append(word);
    }
    
    @Override public final String getName() { return getName(word.require2Bytes()); }
    
    @Override public final byte inst() { return inst(word.require2Bytes()); }
    
    protected abstract byte inst(boolean is16);
    protected abstract String getName(boolean is16);
}
