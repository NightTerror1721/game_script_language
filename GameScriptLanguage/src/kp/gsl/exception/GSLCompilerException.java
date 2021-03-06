/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.exception;

import java.util.Iterator;
import java.util.List;
import kp.gsl.exception.ErrorList.ErrorEntry;

/**
 *
 * @author Asus
 */
public final class GSLCompilerException extends GSLException implements Iterable<ErrorEntry>
{
    private final List<ErrorEntry> errors;
    
    public GSLCompilerException(ErrorList errors)
    {
        super(generateMessage(errors));
        this.errors = errors.getAllErrors();
    }
    
    public final int getErrorCount() { return errors.size(); }
    
    public final List<ErrorEntry> getErrors() { return errors; }
    
    @Override
    public final Iterator<ErrorEntry> iterator() { return errors.iterator(); }
    
    private static String generateMessage(ErrorList errors)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(errors.getErrorCount()).append(errors.getErrorCount() == 1 ? " error found:" : " errors found:");
        for(ErrorEntry e : errors)
        {
            sb.append("\n\t");
            if(e.getStartLine() == e.getEndLine())
                sb.append("In line ").append(e.getStartLine()).append(": ");
            else sb.append("From line ").append(e.getStartLine()).append(" to ").append(e.getEndLine()).append(": ");
            sb.append(e.getCause().getMessage());
        }
        return sb.toString();
    }
    
    public static final GSLCompilerException single(CompilerError error)
    {
        ErrorList list = new ErrorList();
        list.addError(0, 0, error);
        return new GSLCompilerException(list);
    }
}
