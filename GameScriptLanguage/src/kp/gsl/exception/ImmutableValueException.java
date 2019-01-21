/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.exception;

import kp.gsl.lang.GSLImmutableValue;

/**
 *
 * @author Asus
 */
public class ImmutableValueException extends GSLRuntimeException
{
    public ImmutableValueException(GSLImmutableValue value)
    {
        super("Immutable value " + value + " with type " + value.getGSLDataType() + " cannot be modified.");
    }
}
