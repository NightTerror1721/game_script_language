/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.bytecode;

import static kp.gsl.script.bytecode.ByteOps.B_ZERO;
import static kp.gsl.script.bytecode.ByteOps.i2b;

/**
 *
 * @author Asus
 */
public abstract class OneByteRefBytecode extends NoParsBytecode
{
    private final ReferenceValue ref;
    
    public OneByteRefBytecode(int ref_or_byte)
    {
        this.ref = new ReferenceValue(i2b(ref_or_byte), B_ZERO);
    }
        
    @Override public int getExtraBytesCount() { return ref.getByteCount(); }
    @Override public int getExtraByte(int index) { return ref.getByte0(); }
    @Override void buildDescription(ValueBuilder builder)
    {
        builder.append(ref);
    }
}
