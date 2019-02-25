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
    
    @Override public final void     operatorSet(GSLValue index, GSLValue value) { throw new ImmutableValueException(this); }
    @Override public final void     operatorSet(int index, GSLValue value) { throw new ImmutableValueException(this); }
    @Override public final void     operatorAdd(GSLValue value) { throw new ImmutableValueException(this); }
    
    @Override public final void operatorSetProperty(String name, GSLValue value) { throw new ImmutableValueException(this); }
    @Override public final void operatorDelProperty(String name) { throw new ImmutableValueException(this); }
    
    @Override public final GSLValue operatorReferenceGet() { throw new NotPointerException(this); }
    @Override public final void     operatorReferenceSet(GSLValue value) { throw new NotPointerException(this); }
    
    
    /* Common operators */
    @Override public abstract GSLImmutableValue operatorEquals(GSLValue value);
    @Override public abstract GSLImmutableValue operatorNotEquals(GSLValue value);
    @Override public abstract GSLImmutableValue operatorGreater(GSLValue value);
    @Override public abstract GSLImmutableValue operatorSmaller(GSLValue value);
    @Override public abstract GSLImmutableValue operatorGreaterEquals(GSLValue value);
    @Override public abstract GSLImmutableValue operatorSmallerEquals(GSLValue value);
    @Override public abstract GSLImmutableValue operatorNegate();
    
    
    /* Math operators */
    @Override public abstract GSLImmutableValue operatorPlus(GSLValue value);
    @Override public abstract GSLImmutableValue operatorMinus(GSLValue value);
    @Override public abstract GSLImmutableValue operatorMultiply(GSLValue value);
    @Override public abstract GSLImmutableValue operatorDivide(GSLValue value);
    @Override public abstract GSLImmutableValue operatorRemainder(GSLValue value);
    @Override public abstract GSLImmutableValue operatorIncrease();
    @Override public abstract GSLImmutableValue operatorDecrease();
    @Override public abstract GSLImmutableValue operatorNegative();
    
    
    /* Bit operators */
    @Override public abstract GSLImmutableValue operatorBitwiseShiftLeft(GSLValue value);
    @Override public abstract GSLImmutableValue operatorBitwiseShiftRight(GSLValue value);
    @Override public abstract GSLImmutableValue operatorBitwiseAnd(GSLValue value);
    @Override public abstract GSLImmutableValue operatorBitwiseOr(GSLValue value);
    @Override public abstract GSLImmutableValue operatorBitwiseXor(GSLValue value);
    @Override public abstract GSLImmutableValue operatorBitwiseNot();
    
    
    /* Array operators */
    @Override public abstract GSLImmutableValue operatorGet(GSLValue index);
    @Override public          GSLImmutableValue operatorGet(int index) { return operatorGet(new GSLInteger(index)); }
    @Override public abstract GSLImmutableValue operatorPeek();
    
    @Override public abstract GSLImmutableValue operatorGetProperty(String name);
}
