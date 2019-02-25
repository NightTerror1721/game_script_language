/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.compiler;

import kp.gsl.exception.CompilerError;
import kp.gsl.script.bytecode.BytecodeList;
import kp.gsl.script.bytecode.Bytecodes;
import kp.gsl.script.compiler.namespace.Namespace;
import kp.gsl.script.compiler.namespace.NamespaceIdentifier;
import kp.gsl.script.compiler.parser.DataType;
import kp.gsl.script.compiler.parser.Statement;


/**
 *
 * @author Asus
 */
public final class StatementCompiler
{
    private StatementCompiler() {}
    
    public static final DataType compile(Namespace scope, BytecodeList opcodes, Statement statement, boolean pop) throws CompilerError
    {
        DataType resultType;
        switch(statement.getCodeFragmentType())
        {
            default: throw new IllegalStateException();
            case IDENTIFIER: resultType = compileIdentifier(scope, opcodes, statement); break;
            case LITERAL: resultType = compileLiteral(scope, opcodes, statement); break;
            case MUTABLE: resultType = compileMutable(scope, opcodes, statement); break;
            case OPERATION:
                resultType = compileOperation(scope, opcodes, statement, pop);
                pop = false;
                break;
        }
        if(pop)
            opcodes.append(Bytecodes.POP);
        return resultType;
    }
    
    public static final void compileScope(Namespace scope, BytecodeList opcodes, Statement statement) throws CompilerError
    {
        if(statement.isScope())
        {
            Scope sscope = (Scope) statement;
            for(Instruction inst : sscope)
                inst.compileFunctionPart(scope, opcodes);
        }
        else compile(scope, opcodes, statement, true);
    }
    
    public static final DataType compileIdentifier(Namespace scope, BytecodeList opcodes, Statement identifier) throws CompilerError
    {
        NamespaceIdentifier id = scope.getIdentifier(identifier.toString());
        switch(id.getIdentifierType())
        {
            case LOCAL_VARIABLE:
                opcodes.append(Bytecodes.loadVar(id.getIndex()));
                break;
            case GLOBAL_VARIABLE:
                opcodes.append(Bytecodes.loadGlobal(id.getIndex()));
                break;
            case ARGUMENT:
                compileLoadArgument(scope, opcodes, id);
                break;
            case CONSTANT:
                opcodes.append(Bytecodes.loadConst(id.getIndex()));
                break;
            case FUNCTION:
                opcodes.append(Bytecodes.loadFunction(id.getIndex()));
                break;
            case LIBRARY_ELEMENT:
                opcodes.append(Bytecodes.libeLoad(id.getIndex()));
                break;
            default: throw new IllegalStateException();
        }
        return id.getType();
    }
    
    private static DataType compileLoadArgument(Namespace scope, BytecodeList opcodes, NamespaceIdentifier arg) throws CompilerError
    {
        if(!arg.isArgument())
            throw new IllegalStateException();
        LocalVariable var = scope.argumentToLocal((Argument) arg);
        opcodes.append(Bytecodes.argToVar(var.getIndex(), arg.getIndex()));
        return var.getType();
    }
    
    public static final DataType compileLiteral(Namespace scope, BytecodeList opcodes, Statement literal) throws CompilerError
    {
        Literal lit = (Literal) literal;
        Constant c = scope.registerLiteral(lit);
        opcodes.append(Bytecodes.loadConst(c.getIndex()));
        return c.getType();
    }
    
    public static final DataType compileMutable(Namespace scope, BytecodeList opcodes, Statement mutable) throws CompilerError
    {
        Mutable mut = (Mutable) mutable;
        int len = mut.getEntryCount();
        if(mut.isArray())
        {
            compileLiteral(scope, opcodes, Literal.valueOf(len));
            opcodes.append(Bytecodes.ARRAY_NEW);
            for(int i=0;i<len;i++)
            {
                opcodes.append(Bytecodes.DUP);
                if(i > 0xff)
                    compileLiteral(scope, opcodes, Literal.valueOf(i));
                compile(scope, opcodes, mut.getEntry(i).getValue(), false);
                opcodes.append(i > 0xff ? Bytecodes.ARRAY_SET : Bytecodes.arrayIntSet(i));
            }
            return DataType.ARRAY;
        }
        else // Object
        {
            opcodes.append(Bytecodes.OBJ_NEW);
            for(int i=0;i<len;i++)
            {
                opcodes.append(Bytecodes.DUP);
                MutableEntry e = mut.getEntry(i);
                int nameIndex = scope.registerIdentifier(e.getKey());
                compile(scope, opcodes, e.getValue(), false);
                opcodes.append(Bytecodes.objPSet(nameIndex));
            }
            return DataType.OBJECT;
        }
    }
    
    public static final DataType compileOperation(Namespace scope, BytecodeList opcodes, Statement operation, boolean pop) throws CompilerError
    {
        DataType type;
        Operation op = (Operation) operation;
        switch(op.getOperator().getOperatorType())
        {
            case UNARY: type = compileUnary(scope, opcodes, op.getOperator(), op.getOperand(0)); break;
            case BINARY: type = compileBinary(scope, opcodes, op.getOperator(), op.getOperand(0), op.getOperand(1)); break;
            case ARRAY_GET: type = compileArrayGet(scope, opcodes, op.getOperand(0), op.getOperand(1)); break;
            case PROPERTY_GET: type = compilePropertyGet(scope, opcodes, op.getOperand(0), op.getOperand(1)); break;
            case CALL: type = compileCall(scope, opcodes, op.getOperand(0), op.getOperand(1), pop); pop = false; break;
            case INVOKE: type = compileInvoke(scope, opcodes, op.getOperand(0), op.getOperand(1), op.getOperand(2), pop); pop = false; break;
            case NEW: type = compileNew(scope, opcodes, op.getOperand(0), op.getOperand(1), pop); pop = false; break;
            case TERNARY_CONDITIONAL: type = compileTernaryCondition(scope, opcodes, op.getOperand(0), op.getOperand(1), op.getOperand(2), pop); pop = false; break;
            case NEW_FUNCTION: type = compileNewFunction(scope, opcodes, op, pop); pop = false; break;
            case ASSIGNMENT: type = compileAssignment(scope, opcodes, op.getOperator(), op.getOperand(0), op.getOperand(1), pop); pop = false; break;
            default: throw new IllegalStateException();
        }
        if(pop)
            opcodes.append(Bytecodes.POP);
        return type;
    }
    
    private static DataType compileUnary(Namespace scope, BytecodeList opcodes, Operator operator, Statement op) throws CompilerError
    {
        if(operator == Operator.ADDRESS_OF)
        {
            if(!op.isIdentifier())
                throw new CompilerError("Expected valid local variable to \"&\" operator.");
            NamespaceIdentifier id = scope.getIdentifier(op.toString());
            if(!id.isLocalVariable())
                throw new CompilerError("Expected valid local variable to \"&\" operator.");
            opcodes.append(Bytecodes.refLocal(id.getIndex(), id.getType().getTypeId()));
            return DataType.ANY;
        }
        if(operator == Operator.INDIRECTION)
        {
            NamespaceIdentifier id;
            if(op.isIdentifier() && (id = scope.getIdentifier(op.toString())).isLibraryElement())
            {
                opcodes.append(Bytecodes.libeRefGet(id.getIndex()));
                return DataType.ANY;
            }
            else
            {
                DataType type = compile(scope, opcodes, op, false);
                opcodes.append(Bytecodes.REF_GET);
                return type;
            }
        }
        final DataType type;
        switch(operator.getSymbol())
        {
            case SUFIX_INCREMENT:
                return store(scope, opcodes, op, (s, o) -> { compile(scope, opcodes, op, false); o.append(Bytecodes.INC, Bytecodes.DUP); return null; });
            case SUFIX_DECREMENT:
                return store(scope, opcodes, op, (s, o) -> { compile(scope, opcodes, op, false); o.append(Bytecodes.DEC, Bytecodes.DUP); return null; });
            case PREFIX_INCREMENT:
                return store(scope, opcodes, op, (s, o) -> { compile(scope, opcodes, op, false); o.append(Bytecodes.DUP, Bytecodes.INC); return null; });
            case PREFIX_DECREMENT:
                return store(scope, opcodes, op, (s, o) -> { compile(scope, opcodes, op, false); o.append(Bytecodes.DUP, Bytecodes.DEC); return null; });
            case UNARY_PLUS:
                type = compile(scope, opcodes, op, false);
                return type;
            case UNARY_MINUS:
                type = compile(scope, opcodes, op, false);
                opcodes.append(Bytecodes.NEG);
                return type;
            case NOT:
                type = compile(scope, opcodes, op, false);
                opcodes.append(Bytecodes.INV);
                return type;
            case BITWISE_NOT:
                type = compile(scope, opcodes, op, false);
                opcodes.append(Bytecodes.BW_NOT);
                return type;
            case CAST_INT:
                compile(scope, opcodes, op, false);
                opcodes.append(Bytecodes.CAST_INT);
                return DataType.INTEGER;
            case CAST_FLOAT:
                compile(scope, opcodes, op, false);
                opcodes.append(Bytecodes.CAST_FLOAT);
                return DataType.FLOAT;
            case CAST_STRING:
                compile(scope, opcodes, op, false);
                opcodes.append(Bytecodes.CAST_STRING);
                return DataType.STRING;
            case CAST_ARRAY:
                compile(scope, opcodes, op, false);
                opcodes.append(Bytecodes.CAST_ARRAY);
                return DataType.ARRAY;
            case CAST_OBJECT:
                compile(scope, opcodes, op, false);
                opcodes.append(Bytecodes.CAST_OBJECT);
                return DataType.OBJECT;
            case LENGTH:
                compile(scope, opcodes, op, false);
                opcodes.append(Bytecodes.LEN);
                return DataType.INTEGER;
            case ISDEF:
                compile(scope, opcodes, op, false);
                opcodes.append(Bytecodes.ISDEF);
                return DataType.INTEGER;
            case ISUNDEF:
                compile(scope, opcodes, op, false);
                opcodes.append(Bytecodes.ISUNDEF);
                return DataType.INTEGER;
            case TYPEID:
                compile(scope, opcodes, op, false);
                opcodes.append(Bytecodes.TYPEID);
                return DataType.INTEGER;
            case ITERATOR:
                compile(scope, opcodes, op, false);
                opcodes.append(Bytecodes.ITERATOR);
                return DataType.ANY;
            default: throw new IllegalStateException();
        }
    }
    
    private static DataType compileBinary(Namespace scope, BytecodeList opcodes, Operator operator, Statement left, Statement right) throws CompilerError
    {
        if(operator == Operator.LOGICAL_AND || operator == Operator.LOGICAL_OR)
        {
            OpcodeLocation ifJump = compileCondition(scope, opcodes, left);
            if(operator == Operator.LOGICAL_AND)
            {
                OpcodeLocation gotoJumo = opcodes.append(Bytecodes.goTo());
                opcodes.setJumpOpcodeLocationToBottom(ifJump);
                ifJump = compileCondition(scope, opcodes, right);
                compileLiteral(scope, opcodes, Literal.FALSE);
                OpcodeLocation lastGoto = opcodes.append(Bytecodes.goTo());
                opcodes.setJumpOpcodeLocationTarget(gotoJumo, ifJump.next());
                compileLiteral(scope, opcodes, Literal.TRUE);
                opcodes.setJumpOpcodeLocationToBottom(lastGoto);
            }
            else
            {
                OpcodeLocation rightJump = compileCondition(scope, opcodes, right);
                compileLiteral(scope, opcodes, Literal.FALSE);
                OpcodeLocation lastGoto = opcodes.append(Bytecodes.goTo());
                opcodes.setJumpOpcodeLocationToBottom(ifJump);
                opcodes.setJumpOpcodeLocationToBottom(rightJump);
                compileLiteral(scope, opcodes, Literal.TRUE);
                opcodes.setJumpOpcodeLocationToBottom(lastGoto);
                
            }
            scope.getRuntimeStack().pop();
            return DataType.INTEGER;
        }
        DataType tleft = compile(scope, opcodes, left, false);
        DataType tright = compile(scope, opcodes, right, false);
        if(operator == Operator.CONCAT)
        {
            opcodes.append(Bytecodes.CONCAT);
            return DataType.STRING;
        }
        DataType type = binaryDataCheck(scope, opcodes, tleft, tright);
        
        switch(operator.getSymbol())
        {
            case MULTIPLICATION:
                opcodes.append(Bytecodes.MUL);
                return type;
            case DIVISION:
                opcodes.append(Bytecodes.DIV);
                return type;
            case REMAINDER:
                opcodes.append(Bytecodes.REM);
                return type;
            case ADDITION:
                opcodes.append(Bytecodes.ADD);
                return type;
            case SUBTRACTION:
                opcodes.append(Bytecodes.SUB);
                return type;
            case BITWISE_LEFT_SHIFT:
                opcodes.append(Bytecodes.BW_SFH_L);
                return type;
            case BITWISE_RIGHT_SHIFT:
                opcodes.append(Bytecodes.BW_SFH_R);
                return type;
            case GREATER_THAN:
                opcodes.append(Bytecodes.GR);
                return type;
            case SMALLER_THAN:
                opcodes.append(Bytecodes.SM);
                return type;
            case GREATER_EQUALS_THAN:
                opcodes.append(Bytecodes.GREQ);
                return type;
            case SMALLER_EQUALS_THAN:
                opcodes.append(Bytecodes.SMEQ);
                return type;
            case EQUALS:
                opcodes.append(Bytecodes.EQ);
                return type;
            case NOT_EQUALS:
                opcodes.append(Bytecodes.NEQ);
                return type;
            case TYPED_EQUALS:
                opcodes.append(Bytecodes.TEQ);
                return type;
            case TYPED_NOT_EQUALS:
                opcodes.append(Bytecodes.TNEQ);
                return type;
            case BITWISE_AND:
                opcodes.append(Bytecodes.BW_AND);
                return type;
            case BITWISE_XOR:
                opcodes.append(Bytecodes.BW_XOR);
                return type;
            case BITWISE_OR:
                opcodes.append(Bytecodes.BW_OR);
                return type;
            default: throw new IllegalStateException();
        }
    }
    private static DataType binaryDataCheck(Namespace scope, BytecodeList opcodes, DataType tleft, DataType tright) throws CompilerError
    {
        if(DataType.canUseImplicitCast(tleft, tright))
            return tleft;
        if(tleft == DataType.INTEGER && tright == DataType.FLOAT)
            return DataType.FLOAT;
        throw new CompilerError("Cannot cast " + tleft + " to " + tright);
    }
    
    private static DataType compileArrayGet(Namespace scope, BytecodeList opcodes, Statement arrayOp, Statement indexOp) throws CompilerError
    {
        NamespaceIdentifier id;
        if(arrayOp.isIdentifier() && (id = scope.getIdentifier(arrayOp.toString())).isLibraryElement());
        else
        {
            id = null;
            compile(scope, opcodes, arrayOp, false);
        }
        if(indexOp.isLiteral())
        {
            SGSImmutableValue value = ((Literal) indexOp).getSGSValue();
            if(value.isInteger() && value.toInt() >= 0 && value.toInt() <= 0xff)
                if(id != null)
                    opcodes.append(Bytecodes.libeArrayIntGet(id.getIndex(), value.toInt()));
                else opcodes.append(Bytecodes.arrayIntGet(value.toInt()));
            else
            {
                compileLiteral(scope, opcodes, indexOp);
                if(id != null)
                    opcodes.append(Bytecodes.libeArrayGet(id.getIndex()));
                else opcodes.append(Bytecodes.ARRAY_GET);
            }
        }
        else
        {
            compile(scope, opcodes, indexOp, false);
            if(id != null)
                opcodes.append(Bytecodes.libeArrayGet(id.getIndex()));
            else opcodes.append(Bytecodes.ARRAY_GET);
        }
        return DataType.ANY;
    }
    
    private static DataType compilePropertyGet(Namespace scope, BytecodeList opcodes, Statement objectOp, Identifier identifier) throws CompilerError
    {
        NamespaceIdentifier id;
        if(objectOp.isIdentifier() && (id = scope.getIdentifier(objectOp.toString())).isLibraryElement())
            opcodes.append(Bytecodes.libePGet(id.getIndex(), scope.registerIdentifier(identifier.toString())));
        else
        {
            compile(scope, opcodes, objectOp, false);
            opcodes.append(Bytecodes.objPGet(scope.registerIdentifier(identifier.toString())));
        }
        return DataType.ANY;
    }
    
    private static DataType compileCall(Namespace scope, BytecodeList opcodes, Statement funcOp, Arguments args, boolean popReturn) throws CompilerError
    {
        if(funcOp.isIdentifier())
        {
            NamespaceIdentifier id = scope.getIdentifier(funcOp.toString());
            if(id.isFunction())
            {
                int count = compileArguments(scope, opcodes, args);
                opcodes.append(Bytecodes.localCall(id.getIndex(), count, popReturn));
                return id.getReturnType();
            }
            else if(id.isLibraryElement())
            {
                int count = compileArguments(scope, opcodes, args);
                opcodes.append(Bytecodes.libeCall(id.getIndex(), count, popReturn));
                return id.getReturnType();
            }
        }
        compile(scope, opcodes, funcOp, false);
        int count = compileArguments(scope, opcodes, args);
        opcodes.append(Bytecodes.call(count, popReturn));
        return DataType.ANY;
    }
    
    private static DataType compileInvoke(Namespace scope, BytecodeList opcodes, Statement objOp, Identifier property, Arguments args, boolean popReturn) throws CompilerError
    {
        compile(scope, opcodes, objOp, false);
        int count = compileArguments(scope, opcodes, args);
        //NamespaceIdentifier id = scope.getIdentifier(property.toString());
        int identifierIndex = scope.registerIdentifier(property.toString());
        opcodes.append(Bytecodes.invoke(identifierIndex, count, popReturn));
        return DataType.ANY;
    }
    
    private static DataType compileNew(Namespace scope, BytecodeList opcodes, Statement base, Arguments args, boolean popReturn) throws CompilerError
    {
        if(base.isIdentifier())
        {
            NamespaceIdentifier id = scope.getIdentifier(base.toString());
            if(id.isLibraryElement())
            {
                int count = compileArguments(scope, opcodes, args);
                opcodes.append(Bytecodes.libeNew(id.getIndex(), count, popReturn));
                return DataType.OBJECT;
            }
        }
        compile(scope, opcodes, base, false);
        int count = compileArguments(scope, opcodes, args);
        opcodes.append(Bytecodes.New(count, popReturn));
        return DataType.OBJECT;
    }
    
    private static int compileArguments(Namespace scope, BytecodeList opcodes, Arguments args) throws CompilerError
    {
        for(Statement arg : args)
            compile(scope, opcodes, arg, false);
        return args.getArgumentCount();
    }
    
    private static DataType compileTernaryCondition(Namespace scope, BytecodeList opcodes, Statement condOp, Statement trueOp, Statement falseOp, boolean pop) throws CompilerError
    {
        OpcodeLocation trueJump = compileCondition(scope, opcodes, condOp);
        DataType falseType = compile(scope, opcodes, falseOp, pop);
        OpcodeLocation endJump = opcodes.append(Bytecodes.goTo());
        opcodes.setJumpOpcodeLocationToBottom(trueJump);
        DataType trueType = compile(scope, opcodes, trueOp, pop);
        opcodes.setJumpOpcodeLocationToBottom(endJump);
        if(pop)
            opcodes.append(Bytecodes.POP);
        return DataType.canUseImplicitCast(trueType, falseType)
                ? trueType : DataType.canUseImplicitCast(falseType, trueType)
                ? falseType : DataType.ANY;
    }
    
    private static DataType compileNewFunction(Namespace scope, BytecodeList opcodes, Operation op, boolean pop) throws CompilerError
    {
        if(op.getOperandCount() == 2)
            return compileNewFunction(scope, opcodes, null, op.getOperand(0), op.getOperand(1), pop);
        return compileNewFunction(scope, opcodes, op.getOperand(0), op.getOperand(1), op.getOperand(2), pop);
    }
    public static final DataType compileNewFunction(Namespace scope, BytecodeList opcodes, Identifier name, Arguments pars, Scope funcScope, boolean pop) throws CompilerError
    {
        Function func = scope.createFunction(name == null ? null : name.toString());
        Namespace child = scope.createChildScope(NamespaceType.FUNCTION);
        compileNewFunctionParameters(child, pars);
        FunctionCompiler.compile(func, child, funcScope);
        if(child.hasInheritedIds())
        {
            for(LocalVariable id : child.getInheritedIds())
                if(id.isArgument())
                    compileLoadArgument(scope, opcodes, id);
                else opcodes.append(Bytecodes.loadVar(id.getIndex()));
            opcodes.append(Bytecodes.loadClosure(func.getIndex(), child.getInheritedIdCount()));
            if(name != null)
            {
                if(!pop)
                    opcodes.append(Bytecodes.DUP);
                NamespaceIdentifier id = scope.registerClosure(func);
                compileStoreIdentifier(scope, opcodes, id, DataType.ANY);
            }
            else if(pop)
                opcodes.append(Bytecodes.POP);
        }
        else
        {
            if(name != null)
                scope.registerFunction(func);
            if(!pop)
                opcodes.append(Bytecodes.loadFunction(func.getIndex()));
        }
        return DataType.ANY;
    }
    
    public static final void compileNewFunctionParameters(Namespace scope, Arguments pars) throws CompilerError
    {
        for(Statement par : pars)
        {
            if(par.isIdentifier())
                scope.createArgument(par.toString(), DataType.ANY);
            else if(par.isVarargs())
                scope.createVarArgument(((Varargs) par).getName().toString());
            else throw new CompilerError("Expected valid identifier for parameter name or varargs. But found: " + par);
        }
    }
    
    private static DataType compileAssignment(Namespace scope, BytecodeList opcodes, Operator operator, Statement dest, Statement source, boolean pop) throws CompilerError
    {
        return store(scope, opcodes, dest, (s, o) -> {
            if(operator.hasInnerOperator())
                return compile(s, o, Operation.binary(operator.getInnerOperator(), dest, source), false);
            return compile(s, o, source, false);
        });
    }
    
    private static DataType store(Namespace scope, BytecodeList opcodes, Statement dest, StoreSource source) throws CompilerError
    {
        if(dest.isIdentifier())
        {
            DataType sourceType = source.compile(scope, opcodes);
            NamespaceIdentifier id = scope.getIdentifier(dest.toString());
            if(sourceType == null)
                sourceType = id.getType();
            return compileStoreIdentifier(scope, opcodes, id, sourceType);
        }
        else if(dest.isOperation())
        {
            Operation op = (Operation) dest;
            if(op.isArrayGet())
                return compileArraySet(scope, opcodes, op.getOperand(0), op.getOperand(1), source);
            else if(op.isPropertyGet())
                return compilePropertySet(scope, opcodes, op.getOperand(0), op.getOperand(1), source);
            else if(op.getOperator() == Operator.INDIRECTION)
                return compileIndirectionSet(scope, opcodes, op.getOperand(0), source);
            else throw new CompilerError("Cannot store into :" + dest);
        }
        else throw new CompilerError("Cannot store into :" + dest);
    }
    
    private static DataType compileStoreIdentifier(Namespace scope, BytecodeList opcodes, NamespaceIdentifier identifier, DataType sourceType) throws CompilerError
    {
        DataType destType = identifier.getType();
        if(!DataType.canUseImplicitCast(destType, sourceType))
            throw new CompilerError("Cannot assign " + sourceType + " to " + identifier.getType());
        if(destType != sourceType)
        {
            switch(destType.getTypeId())
            {
                case Type.INTEGER: opcodes.append(Bytecodes.CAST_INT); break;
                case Type.FLOAT: opcodes.append(Bytecodes.CAST_FLOAT); break;
                case Type.STRING: opcodes.append(Bytecodes.CAST_STRING); break;
                case Type.ARRAY: opcodes.append(Bytecodes.CAST_ARRAY); break;
                case Type.OBJECT: opcodes.append(Bytecodes.CAST_OBJECT); break;
            }
        }
        switch(identifier.getIdentifierType())
        {
            case LOCAL_VARIABLE: opcodes.append(Bytecodes.storeVar(identifier.getIndex())); break;
            case GLOBAL_VARIABLE: opcodes.append(Bytecodes.storeGlobal(identifier.getIndex())); break;
            default: throw new CompilerError("Cannot store value into " + identifier.getIdentifierType());
        }
            
        return identifier.getType();
    }
    
    private static DataType compileArraySet(Namespace scope, BytecodeList opcodes, Statement arrayOp, Statement indexOp, StoreSource source) throws CompilerError
    {
        NamespaceIdentifier id;
        if(arrayOp.isIdentifier() && (id = scope.getIdentifier(arrayOp.toString())).isLibraryElement());
        else
        {
            id = null;
            compile(scope, opcodes, arrayOp, false);
        }
        if(indexOp.isLiteral())
        {
            SGSImmutableValue value = ((Literal) indexOp).getSGSValue();
            if(value.isInteger() && value.toInt() >= 0 && value.toInt() <= 0xff)
            {
                source.compile(scope, opcodes);
                opcodes.append(Bytecodes.arrayIntSet(value.toInt()));
            }
            else
            {
                compileLiteral(scope, opcodes, indexOp);
                source.compile(scope, opcodes);
                if(id != null)
                    opcodes.append(Bytecodes.libeArrayGet(id.getIndex()));
                else opcodes.append(Bytecodes.ARRAY_SET);
            }
        }
        else
        {
            compile(scope, opcodes, indexOp, false);
            source.compile(scope, opcodes);
            opcodes.append(Bytecodes.ARRAY_SET);
        }
        return DataType.ANY;
    }
    
    private static DataType compilePropertySet(Namespace scope, BytecodeList opcodes, Statement objectOp, Identifier identifier, StoreSource source) throws CompilerError
    {
        compile(scope, opcodes, objectOp, false);
        source.compile(scope, opcodes);
        opcodes.append(Bytecodes.objPSet(scope.registerIdentifier(identifier.toString())));
        return DataType.ANY;
    }
    
    private static DataType compileIndirectionSet(Namespace scope, BytecodeList opcodes, Statement operand, StoreSource source) throws CompilerError
    {
        compile(scope, opcodes, operand, false);
        source.compile(scope, opcodes);
        opcodes.append(Bytecodes.REF_SET);
        return DataType.ANY;
    }
    
    public static final OpcodeLocation compileCondition(Namespace scope, BytecodeList opcodes, Statement statement) throws CompilerError
    {
        Operation op;
        if(statement.isOperation() && (op = (Operation) statement).getOperatorSymbol().isConditional())
        {
            switch(op.getOperatorSymbol())
            {
                case NOT:
                    compile(scope, opcodes, op.getOperand(0), false);
                    return opcodes.append(Bytecodes.ifInv());
                case ISDEF:
                    compile(scope, opcodes, op.getOperand(0), false);
                    return opcodes.append(Bytecodes.ifDef());
                case ISUNDEF:
                    compile(scope, opcodes, op.getOperand(0), false);
                    return opcodes.append(Bytecodes.ifUndef());
                case GREATER_THAN:
                    compile(scope, opcodes, op.getOperand(0), false);
                    compile(scope, opcodes, op.getOperand(1), false);
                    return opcodes.append(Bytecodes.ifGr());
                case SMALLER_THAN:
                    compile(scope, opcodes, op.getOperand(0), false);
                    compile(scope, opcodes, op.getOperand(1), false);
                    return opcodes.append(Bytecodes.ifSm());
                case GREATER_EQUALS_THAN:
                    compile(scope, opcodes, op.getOperand(0), false);
                    compile(scope, opcodes, op.getOperand(1), false);
                    return opcodes.append(Bytecodes.ifGrEq());
                case SMALLER_EQUALS_THAN:
                    compile(scope, opcodes, op.getOperand(0), false);
                    compile(scope, opcodes, op.getOperand(1), false);
                    return opcodes.append(Bytecodes.ifSmEq());
                case EQUALS:
                    compile(scope, opcodes, op.getOperand(0), false);
                    compile(scope, opcodes, op.getOperand(1), false);
                    return opcodes.append(Bytecodes.ifEq());
                case NOT_EQUALS:
                    compile(scope, opcodes, op.getOperand(0), false);
                    compile(scope, opcodes, op.getOperand(1), false);
                    return opcodes.append(Bytecodes.ifNEq());
                case TYPED_EQUALS:
                    compile(scope, opcodes, op.getOperand(0), false);
                    compile(scope, opcodes, op.getOperand(1), false);
                    return opcodes.append(Bytecodes.ifTEq());
                case TYPED_NOT_EQUALS:
                    compile(scope, opcodes, op.getOperand(0), false);
                    compile(scope, opcodes, op.getOperand(1), false);
                    return opcodes.append(Bytecodes.ifTNEq());
                default: throw new IllegalStateException();
            }
        }
        else
        {
            compile(scope, opcodes, statement, false);
            return opcodes.append(Bytecodes.IF());
        }
    }
    public static final OpcodeLocation compileDefaultIf(Namespace scope, BytecodeList opcodes, Statement statement) throws CompilerError
    {
        OpcodeLocation loc = compileCondition(scope, opcodes, statement);
        OpcodeLocation locJump = opcodes.append(Bytecodes.goTo());
        opcodes.setJumpOpcodeLocationToBottom(loc);
        return locJump;
    }
    
    @FunctionalInterface
    private static interface StoreSource
    {
        DataType compile(Namespace scope, BytecodeList opcodes) throws CompilerError;
    }
}
