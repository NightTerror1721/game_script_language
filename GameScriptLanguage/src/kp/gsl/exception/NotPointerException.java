/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.exception;

import kp.gsl.lang.GSLValue;

/**
 *
 * @author Asus
 */
public class NotPointerException extends GSLRuntimeException
{
    public NotPointerException(GSLValue value)
    {
        super("Immutable value " + value + " with type " + value.getGSLDataType() + " is not a valid pointer.");
    }
}
