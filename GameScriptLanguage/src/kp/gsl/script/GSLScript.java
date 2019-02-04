/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script;

import java.util.Objects;
import kp.gsl.GSLGlobals;
import kp.gsl.exception.GSLRuntimeException;
import kp.gsl.lang.GSLBlueprint;
import kp.gsl.lang.GSLFunction;
import kp.gsl.lang.GSLImmutableValue;
import kp.gsl.lang.GSLIterator;
import kp.gsl.lang.GSLList;
import kp.gsl.lang.GSLMap;
import kp.gsl.lang.GSLObject;
import kp.gsl.lang.GSLRawBytes;
import kp.gsl.lang.GSLReference;
import kp.gsl.lang.GSLStruct;
import kp.gsl.lang.GSLTuple;
import kp.gsl.lang.GSLValue;
import static kp.gsl.lang.GSLValue.NULL;
import kp.gsl.lang.GSLVarargs;
import static kp.gsl.lang.GSLVarargs.NO_ARGS;
import kp.gsl.lib.GSLLibraryElement;
import static kp.gsl.script.Instruction.*;

/**
 *
 * @author Asus
 */
public final class GSLScript
{
    final GSLGlobals globals;
    final GSLImmutableValue[] constants;
    final String[] identifiers;
    final byte[][] functions;
    final GSLLibraryElement[] libElements;
    private final GSLFunction[] functionCache;
    
    public GSLScript(
            GSLGlobals globals,
            GSLImmutableValue[] constants,
            String[] identifiers,
            byte[][] functions,
            GSLLibraryElement[] libElements)
    {
        this.globals = Objects.requireNonNull(globals);
        this.constants = Objects.requireNonNull(constants);
        this.identifiers = Objects.requireNonNull(identifiers);
        this.functions = Objects.requireNonNull(functions);
        this.libElements = Objects.requireNonNull(libElements);
        this.functionCache = new GSLFunction[functions.length];
    }
    
    private GSLValue executeFunction(GSLGlobals globals, byte[] code, GSLValue self, GSLVarargs args)
    {
        int sit;
        int inst = ScriptConstants.CODE_INIT;
        var stack = new GSLValue[(code[ScriptConstants.CODE_STACK_LEN] & 0xff) + (sit = (code[ScriptConstants.CODE_VARS_LEN] & 0xff))];
        
        for(;;)
        {
            switch(code[inst++] & 0xff)
            {
                default: throw new GSLRuntimeException("Invalid instruction: " + Integer.toHexString(code[inst - 1] & 0xff));
                case NOOP: break;
                
                case LOAD_CONST: stack[sit++] = constants[code[inst++] & 0xff]; break;
                case LOAD_CONST16: stack[sit++] = constants[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)]; break;
                case LOAD_VAR: stack[sit++] = stack[code[inst++] & 0xff]; break;
                case LOAD_FUNCTION: stack[sit++] = getLocalFunction(code[inst++] & 0xff); break;
                case LOAD_FUNCTION16: stack[sit++] = getLocalFunction((code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)); break;
                case LOAD_CLOSURE: {
                    var len = code[inst++] & 0xff;
                    var fidx = code[inst++] & 0xff;
                    var a = len > 0 ? new StackCopyArgs(stack, sit, len) : NO_ARGS;
                    sit -= len;
                    stack[sit++] = new Closure(fidx, a, len);
                } break;
                case LOAD_CLOSURE16: {
                    var len = code[inst++] & 0xff;
                    var fidx = (code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8);
                    var a = len > 0 ? new StackCopyArgs(stack, sit, len) : NO_ARGS;
                    sit -= len;
                    stack[sit++] = new Closure(fidx, a, len);
                } break;
                case LOAD_GLOBAL: stack[sit++] = globals.getGlobalValue(identifiers[code[inst++] & 0xff]); break;
                case LOAD_GLOBAL16: stack[sit++] = globals.getGlobalValue(identifiers[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)]); break;
                case LOAD_NULL: stack[sit++] = NULL; break;
                
                case STORE_GLOBAL: globals.setGlobalValue(identifiers[code[inst++] & 0xff], stack[--sit]); break;
                case STORE_GLOBAL16: globals.setGlobalValue(identifiers[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)], stack[--sit]); break;
                case STORE_VAR: stack[code[inst++] & 0xff] = stack[--sit]; break;
                case ST_VAR_NULL: stack[code[inst++] & 0xff] = NULL; break;
                case ST_VAR_INT: stack[code[inst++] & 0xff] = stack[--sit].operatorCastInteger(); break;
                case ST_VAR_FLOAT: stack[code[inst++] & 0xff] = stack[--sit].operatorCastFloat(); break;
                case ST_VAR_BOOL: stack[code[inst++] & 0xff] = stack[--sit].operatorCastBoolean(); break;
                case ST_VAR_STRING: stack[code[inst++] & 0xff] = stack[--sit].operatorCastString(); break;
                case ST_VAR_CTUPLE: stack[code[inst++] & 0xff] = stack[--sit].operatorCastConstTuple(); break;
                case ST_VAR_CMAP: stack[code[inst++] & 0xff] = stack[--sit].operatorCastConstMap(); break;
                case ST_VAR_FUNCTION: stack[code[inst++] & 0xff] = stack[--sit].operatorCastFunction(); break;
                case ST_VAR_LIST: stack[code[inst++] & 0xff] = stack[--sit].operatorCastList(); break;
                case ST_VAR_TUPLE: stack[code[inst++] & 0xff] = stack[--sit].operatorCastTuple(); break;
                case ST_VAR_MAP: stack[code[inst++] & 0xff] = stack[--sit].operatorCastMap(); break;
                case ST_VAR_STRUCT: stack[code[inst++] & 0xff] = stack[--sit].operatorCastStruct(); break;
                case ST_VAR_BPRINT: stack[code[inst++] & 0xff] = stack[--sit].operatorCastBlueprint(); break;
                case ST_VAR_OBJECT: stack[code[inst++] & 0xff] = stack[--sit].operatorCastObject(); break;
                case ST_VAR_ITERATOR: stack[code[inst++] & 0xff] = stack[--sit].operatorCastIterator(); break;
                case ST_VAR_BYTES: stack[code[inst++] & 0xff] = stack[--sit].operatorCastRawBytes(); break;
                case ST_VAR_NATIVE: stack[code[inst++] & 0xff] = stack[--sit].operatorCastNative(); break;
                
                case LIST_NEW: stack[sit++] = new GSLList(); break;
                case TUPLE_NEW: stack[sit - 1] = new GSLTuple(new GSLValue[stack[sit - 1].intValue()]); break;
                case MAP_NEW: stack[sit++] = new GSLMap(); break;
                case STRUCT_NEW: {
                    var parent = stack[sit - 1];
                    var props = stack[sit - 2].stream().map(GSLValue::toString).toArray(String[]::new);
                    stack[--sit] = new GSLStruct(parent == NULL ? null : parent, props);
                } break;
                case BLUEPRINT_NEW: {
                    var parent = stack[sit - 1];
                    var base = stack[sit - 2];
                    stack[--sit] = GSLBlueprint.assimilate(base.cast(), parent == NULL ? null : parent);
                } break;
                case OBJECT_NEW: stack[sit++] = new GSLObject(); break;
                case ITERATOR_NEW: stack[sit -= 2] = new CustomIterator(stack[sit - 1], stack[sit], stack[sit + 1]); break;
                case BYTES_NEW: stack[sit - 1] = new GSLRawBytes(new byte[stack[sit - 1].intValue()]); break;
                
                case CAST_INT: stack[sit - 1] = stack[sit - 1].operatorCastInteger(); break;
                case CAST_FLOAT: stack[sit - 1] = stack[sit - 1].operatorCastFloat(); break;
                case CAST_BOOL: stack[sit - 1] = stack[sit - 1].operatorCastBoolean(); break;
                case CAST_STRING: stack[sit - 1] = stack[sit - 1].operatorCastString(); break;
                case CAST_CTUPLE: stack[sit - 1] = stack[sit - 1].operatorCastConstTuple(); break;
                case CAST_CMAP: stack[sit - 1] = stack[sit - 1].operatorCastConstMap(); break;
                case CAST_FUNCTION: stack[sit - 1] = stack[sit - 1].operatorCastFunction(); break;
                case CAST_LIST: stack[sit - 1] = stack[sit - 1].operatorCastList(); break;
                case CAST_TUPLE: stack[sit - 1] = stack[sit - 1].operatorCastTuple(); break;
                case CAST_MAP: stack[sit - 1] = stack[sit - 1].operatorCastMap(); break;
                case CAST_STRUCT: stack[sit - 1] = stack[sit - 1].operatorCastStruct(); break;
                case CAST_BPRINT: stack[sit - 1] = stack[sit - 1].operatorCastBlueprint(); break;
                case CAST_OBJECT: stack[sit - 1] = stack[sit - 1].operatorCastObject(); break;
                case CAST_ITERATOR: stack[sit - 1] = stack[sit - 1].operatorCastIterator(); break;
                case CAST_BYTES: stack[sit - 1] = stack[sit - 1].operatorCastRawBytes(); break;
                case CAST_NATIVE: stack[sit - 1] = stack[sit - 1].operatorCastNative(); break;
                
                case REF_NEW: stack[sit++] = new Reference(stack, code[inst++] & 0xff); break;
                case LOAD_REF: stack[sit - 1] = stack[sit - 1].operatorReferenceGet(); break;
                case STORE_REF: stack[sit - 2].operatorReferenceSet(stack[--sit]); break;
                
                case POP: sit--; break;
                case SWAP: {
                    var aux = stack[sit - 2];
                    stack[sit - 2] = stack[sit - 1];
                    stack[sit - 1] = aux;
                } break;
                case SWAP2: {
                    var aux = stack[sit - 3];
                    stack[sit - 3] = stack[sit - 1];
                    stack[sit - 1] = aux;
                } break;
                case DUP: stack[sit++] = stack[sit - 2]; break;
                case DUP2: stack[sit++] = stack[sit - 3]; break;
                
                case GOTO: inst = code[inst++] & 0xff; break;
                case GOTO16: inst = (code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8); break;
                
                case RETURN: return stack[--sit];
                case RETURN_NULL: return NULL;
                case RETURN_INT: return stack[--sit].operatorCastInteger();
                case RETURN_FLOAT: return stack[--sit].operatorCastFloat();
                case RETURN_BOOL: return stack[--sit].operatorCastBoolean();
                case RETURN_STRING: return stack[--sit].operatorCastString();
                case RETURN_CTUPLE: return stack[--sit].operatorCastConstTuple();
                case RETURN_CMAP: return stack[--sit].operatorCastConstMap();
                case RETURN_FUNCTION: return stack[--sit].operatorCastFunction();
                case RETURN_LIST: return stack[--sit].operatorCastList();
                case RETURN_TUPLE: return stack[--sit].operatorCastTuple();
                case RETURN_MAP: return stack[--sit].operatorCastMap();
                case RETURN_STRUCT: return stack[--sit].operatorCastStruct();
                case RETURN_BPRINT: return stack[--sit].operatorCastBlueprint();
                case RETURN_OBJECT: return stack[--sit].operatorCastObject();
                case RETURN_ITERATOR: return stack[--sit].operatorCastIterator();
                case RETURN_BYTES: return stack[--sit].operatorCastRawBytes();
                case RETURN_NATIVE: return stack[--sit].operatorCastNative();
                
            }
        }
    }
    
    public final GSLFunction getLocalFunction(int index)
    {
        return functionCache[index] == null ? functionCache[index] : (functionCache[index] = new LocalFunction(index));
    }
    
    private class LocalFunction extends GSLFunction
    {
        final int functionIdx;
        
        private LocalFunction(int functionIdx) { this.functionIdx = functionIdx; }
        
        @Override
        public GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            return executeFunction(globals, functions[functionIdx], self, args);
        }
    }
    
    private final class Closure extends LocalFunction
    {
        private final GSLVarargs inners;
        private final int len;
        
        private Closure(int functionIdx, GSLVarargs inners, int len)
        {
            super(functionIdx);
            this.inners = inners;
            this.len = len;
        }
        
        @Override
        public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            if(len > 0)
                return executeFunction(globals, functions[functionIdx], self, (args.numberOfArguments() > 0 ? GSLVarargs.varargsOf(inners, args) : inners));
            return executeFunction(globals, functions[functionIdx], self, args);
        }
    }
    
    private static final class StackCopyArgs extends GSLVarargs
    {
        private final GSLValue[] stack;
        
        private StackCopyArgs(GSLValue[] stack, int sit, int len)
        {
            this.stack = new GSLValue[len];
            System.arraycopy(stack, sit - len, this.stack, 0, len);
        }
        
        @Override public final int numberOfArguments() { return stack.length; }
        @Override public final GSLValue arg0() { return stack.length > 0 ? stack[0] : NULL; }
        @Override public final GSLValue arg1() { return stack.length > 1 ? stack[1] : NULL; }
        @Override public final GSLValue arg(int index) { return stack.length > index ? stack[index] : NULL; }
    }
    
    private static final class CustomIterator extends GSLIterator
    {
        private final GSLValue self;
        private final GSLValue hasNext;
        private final GSLValue next;
        
        private CustomIterator(GSLValue self, GSLValue hasNext, GSLValue next)
        {
            this.self = self;
            this.hasNext = hasNext;
            this.next = next;
        }
        
        @Override
        public final boolean hasNext() { return hasNext.operatorCall(self, NO_ARGS).boolValue(); }

        @Override
        public final GSLValue next() { return next.operatorCall(self, NO_ARGS); }
        
    }
    
    private static final class Reference extends GSLReference
    {
        private final GSLValue[] stack;
        private final int offset;
        
        private Reference(GSLValue[] stack, int offset)
        {
            this.stack = stack;
            this.offset = offset;
        }
        
        @Override
        public final GSLValue operatorReferenceGet() { return stack[offset]; }

        @Override
        public final void operatorReferenceSet(GSLValue value) { stack[offset] = value; }
        
    }
}
