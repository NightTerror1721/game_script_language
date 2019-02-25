/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.compiler.parser;

import java.util.Arrays;

/**
 *
 * @author Asus
 */
public abstract class CodeFragment
{
    public abstract CodeFragmentType getCodeFragmentType();
    //public abstract boolean isOperand();
    public abstract boolean isStatement();
    @Override public abstract String toString();
    
    public final boolean isIdentifier() { return getCodeFragmentType() == CodeFragmentType.IDENTIFIER; }
    public final boolean isLiteral() { return getCodeFragmentType() == CodeFragmentType.LITERAL; }
    public final boolean isMutable() { return getCodeFragmentType() == CodeFragmentType.MUTABLE; }
    public final boolean isOperator() { return getCodeFragmentType() == CodeFragmentType.OPERATOR; }
    public final boolean isOperation() { return getCodeFragmentType() == CodeFragmentType.OPERATION; }
    public final boolean isStopchar() { return getCodeFragmentType() == CodeFragmentType.STOPCHAR; }
    public final boolean isArguments() { return getCodeFragmentType() == CodeFragmentType.ARGUMENTS; }
    public final boolean isVarargs() { return getCodeFragmentType() == CodeFragmentType.VARARGS; }
    public final boolean isScope() { return getCodeFragmentType() == CodeFragmentType.SCOPE; }
    public final boolean isDataType() { return getCodeFragmentType() == CodeFragmentType.DATA_TYPE; }
    public final boolean isCommandArguments() { return getCodeFragmentType() == CodeFragmentType.COMMAND_ARGUMENTS; }
    public final boolean isCommand() { return getCodeFragmentType() == CodeFragmentType.COMMAND; }
    
    public final boolean isReturnBasedCode()
    {
        return this == Command.RETURN || this == Command.YIELD;
    }
    
    public final boolean is(CodeFragmentType type) { return getCodeFragmentType() == type; }
    
    public final boolean is(CodeFragmentType type0, CodeFragmentType type1)
    {
        var self = getCodeFragmentType();
        return self == type0 || self == type1;
    }
    
    public final boolean is(CodeFragmentType type0, CodeFragmentType type1, CodeFragmentType type2)
    {
        var self = getCodeFragmentType();
        return self == type0 || self == type1 || self == type2;
    }
    
    public final boolean is(CodeFragmentType... types)
    {
        var self = getCodeFragmentType();
        return Arrays.stream(types).anyMatch(self::equals);
    }
}
