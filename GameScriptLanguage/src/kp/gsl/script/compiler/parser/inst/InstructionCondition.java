/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.compiler.parser.inst;

import java.util.List;
import java.util.Objects;
import kp.gsl.exception.CompilerError;
import kp.gsl.script.bytecode.BytecodeList;
import kp.gsl.script.bytecode.BytecodeList.BytecodeLocation;
import kp.gsl.script.bytecode.Bytecodes;
import kp.gsl.script.compiler.namespace.Namespace;
import kp.gsl.script.compiler.parser.CodeFragment;
import kp.gsl.script.compiler.parser.CodeFragmentList;
import kp.gsl.script.compiler.parser.CommandArguments;
import kp.gsl.script.compiler.parser.Operation;
import kp.gsl.script.compiler.parser.Scope;
import kp.gsl.script.compiler.parser.Statement;
import kp.gsl.script.compiler.parser.StatementParser;

/**
 *
 * @author Asus
 */
public final class InstructionCondition extends Instruction
{
    private final Statement condition;
    private final Statement action;
    private Statement elseAction;
    
    private InstructionCondition(Statement condition, Statement action)
    {
        this.condition = Objects.requireNonNull(condition);
        this.action = Objects.requireNonNull(action);
    }
    
    @Override
    public final InstructionId getInstructionId() { return InstructionId.CONDITION; }
    
    public final void setElseAction(CodeFragmentList list) throws CompilerError
    {
        if(elseAction != null)
            throw new IllegalStateException();
        if(list.isEmpty())
            throw new CompilerError("Malformed \"else\" command. Expected valid statement after \"if\" command.");
        CodeFragment frag = list.get(0);
        this.elseAction = frag.isScope() ? (Scope) frag : StatementParser.parse(list);
    }
    
    public static final InstructionCondition create(CodeFragmentList list) throws CompilerError
    {
        if(list.length() < 2)
            throw new CompilerError("Malformed \"if\" command. Expected valid statement and scope after command.");
        Statement condition, action;
        CodeFragment frag;
        
        frag = list.get(0);
        if(!frag.isCommandArguments())
            throw new CompilerError("Malformed \"if\" command. Expected valid statement and scope after command. But found: " + frag);
        CommandArguments args = (CommandArguments) frag;
        if(args.getArgumentCount() != 1)
            throw new CompilerError("Malformed \"if\" command. Expected valid statement after command. But found: " + args);
        condition = StatementParser.parse(args.getArgument(0));
        
        frag = list.get(1);
        action = frag.isScope() ? (Scope) frag : StatementParser.parse(list.subList(1));
        
        return new InstructionCondition(condition, action);
    }

    @Override
    public final void compileConstantPart(Namespace scope, List<Operation> functions) throws CompilerError
    {
        throw new CompilerError("Cannot compile conditionals in constant mode");
    }

    @Override
    public final void compileFunctionPart(Namespace scope, BytecodeList opcodes) throws CompilerError
    {
        BytecodeLocation elseLoc = StatementCompiler.compileDefaultIf(scope, opcodes, condition);
        StatementCompiler.compileScope(scope.createChildScope(NamespaceScopeType.NORMAL), opcodes, action);
        if(elseAction != null)
        {
            BytecodeLocation endIfLoc = opcodes.append(Bytecodes.goTo());
            opcodes.setJumpBytecodeLocationToBottom(elseLoc);
            StatementCompiler.compileScope(scope.createChildScope(NamespaceScopeType.NORMAL), opcodes, elseAction);
            opcodes.setJumpBytecodeLocationToBottom(endIfLoc);
        }
        else opcodes.setJumpBytecodeLocationToBottom(elseLoc);
    }
}
