/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.compiler.parser;

import java.util.Objects;
import java.util.regex.Pattern;
import kp.gsl.exception.CompilerError;

/**
 *
 * @author Asus
 */
public class Identifier extends Statement
{
    private final String name;
    
    private Identifier(String name) { this.name = Objects.requireNonNull(name); }

    @Override
    public final CodeFragmentType getCodeFragmentType() { return CodeFragmentType.IDENTIFIER; }

    @Override
    public final String toString() { return name; }
    
    private static final Pattern ID_P = Pattern.compile("[_A-Za-z][_0-9A-Za-z]*");
    
    public static final boolean isValid(String identifier) { return ID_P.matcher(identifier).matches(); }
    
    public static final Identifier create(String name) throws CompilerError
    {
        if(!isValid(name))
            throw new CompilerError("\"" + name + "\" is not a valid identifier.");
        return new Identifier(name);
    }
}
