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
public class CompilerError extends GSLException
{
    public CompilerError(String errorMessage) { super(errorMessage); }
    public CompilerError(String errorMessage, Throwable cause) { super(errorMessage, cause); }
}
