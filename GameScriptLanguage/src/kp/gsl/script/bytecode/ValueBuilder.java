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
final class ValueBuilder
{
    private final StringBuilder builder = new StringBuilder();
    
    public final ValueBuilder append(byte value) { return add(value & 0xff); }
    public final ValueBuilder append(ByteValue value) { return add(value); }
    public final ValueBuilder append(WordValue value) { return add(value); }
    public final ValueBuilder append(ReferenceValue value) { return add(value); }
    public final ValueBuilder append(PositionValue value) { return add(value); }
    
    private ValueBuilder add(Object obj)
    {
        if(builder.length() > 0)
            builder.append(' ');
        builder.append(obj);
        return this;
    }
    
    @Override
    public final String toString() { return builder.toString(); }
}
