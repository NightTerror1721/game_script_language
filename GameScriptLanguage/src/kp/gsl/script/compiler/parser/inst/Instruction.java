/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.compiler.parser.inst;

import java.util.List;
import kp.gsl.exception.CompilerError;
import kp.gsl.script.bytecode.BytecodeList;
import kp.gsl.script.compiler.namespace.Namespace;
import kp.gsl.script.compiler.parser.Operation;

/**
 *
 * @author Asus
 */
public abstract class Instruction
{
    public abstract InstructionId getInstructionId();
    
    public abstract void compileConstantPart(Namespace scope, List<Operation> functions) throws CompilerError;
    public abstract void compileFunctionPart(Namespace scope, BytecodeList bytecodes) throws CompilerError;
}
