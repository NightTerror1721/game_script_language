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
public abstract class NoParsBytecode extends Bytecode
{
    @Override public final int getInstruction() { return inst() & 0xff; }
    @Override public int getStackParametersCount() { return Math.abs(stack(true)); }
    @Override public int getStackResultsCount() { return Math.abs(stack(false)); }
    @Override public int getExtraBytesCount() { return 0; }
    @Override public int getExtraByte(int index) { throw new IllegalStateException(); }
    @Override void buildDescription(ValueBuilder builder) {}
    
    protected abstract byte inst();
    protected abstract int stack(boolean pars);
}
