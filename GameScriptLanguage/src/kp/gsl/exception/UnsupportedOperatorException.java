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
public final class UnsupportedOperatorException extends GSLRuntimeException
{
    public UnsupportedOperatorException(GSLValue value, String operatorName)
    {
        super(value.getGSLDataType() + " not support " + operatorName + " operator.");
    }
}
