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
public class GSLException extends Exception
{
    public GSLException() { super(); }
    public GSLException(String msg) { super(msg); }
    public GSLException(Throwable cause) { super(cause); }
    public GSLException(String msg, Throwable cause) { super(msg, cause); }
}
