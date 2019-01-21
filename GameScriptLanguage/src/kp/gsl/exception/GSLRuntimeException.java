/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.exception;

/**
 *
 * @author Asus
 */
public class GSLRuntimeException extends RuntimeException
{
    public GSLRuntimeException() { super(); }
    public GSLRuntimeException(String msg) { super(msg); }
    public GSLRuntimeException(Throwable cause) { super(cause); }
    public GSLRuntimeException(String msg, Throwable cause) { super(msg, cause); }
}
