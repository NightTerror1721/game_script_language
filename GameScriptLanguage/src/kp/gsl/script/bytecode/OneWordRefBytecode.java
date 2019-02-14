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
public abstract class OneWordRefBytecode extends OneWordBytecode<ReferenceValue>
{
    public OneWordRefBytecode(int reference)
    {
        super(new ReferenceValue(reference));
    }
    public OneWordRefBytecode(byte b0, byte b1)
    {
        super(new ReferenceValue(b0, b1));
    }
}
