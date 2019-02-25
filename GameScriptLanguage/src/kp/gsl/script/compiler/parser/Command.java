/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.compiler.parser;

/**
 *
 * @author Asus
 */
public final class Command extends RawCode
{
    private final CommandId id;
    
    private Command(CommandId id)
    {
        if(id == null)
            throw new NullPointerException();
        this.id = id;
    }
    
    public final CommandId getCommandId() { return id; }
    
    @Override
    public final CodeFragmentType getCodeFragmentType() { return CodeFragmentType.COMMAND; }
    
    
    public static final Command
            VAR = new Command(CommandId.VAR),
            GLOBAL = new Command(CommandId.GLOBAL),
            CONST = new Command(CommandId.CONST),
            FUNCTION = new Command(CommandId.FUNCTION),
            INCLUDE = new Command(CommandId.INCLUDE),
            IMPORT = new Command(CommandId.IMPORT),
            FROM = new Command(CommandId.FROM),
            IF = new Command(CommandId.IF),
            ELSE = new Command(CommandId.ELSE),
            FOR = new Command(CommandId.FOR),
            WHILE = new Command(CommandId.WHILE),
            SWITCH = new Command(CommandId.SWITCH),
            BREAK = new Command(CommandId.BREAK),
            CONTINUE = new Command(CommandId.CONTINUE),
            RETURN = new Command(CommandId.RETURN),
            YIELD = new Command(CommandId.YIELD);

    @Override
    public final String toString() { return id.toString(); }
    
}
