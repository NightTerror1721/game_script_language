/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import kp.gsl.exception.ImmutableValueException;
import kp.gsl.exception.NotPointerException;

/**
 *
 * @author Asus
 */
public abstract class GSLImmutableValue extends GSLValue
{
    @Override public final boolean isMutable() { return true; }
    
    @Override public final void operatorSet(GSLValue index, GSLValue value) { throw new ImmutableValueException(this); }
    @Override public final void operatorSet(int index, GSLValue value) { throw new ImmutableValueException(this); }
    
    @Override public final void operatorSetProperty(String name, GSLValue value) { throw new ImmutableValueException(this); }
    @Override public final void operatorDelProperty(String name) { throw new ImmutableValueException(this); }
    
    @Override public final GSLValue operatorReferenceGet() { throw new NotPointerException(this); }
    @Override public final void     operatorReferenceSet(GSLValue value) { throw new NotPointerException(this); }
}
