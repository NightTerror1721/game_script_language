/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script;

import java.util.Objects;
import kp.gsl.GSLGlobals;
import kp.gsl.exception.GSLRuntimeException;
import kp.gsl.lang.AbstractObject;
import kp.gsl.lang.GSLBlueprint;
import kp.gsl.lang.GSLFunction;
import kp.gsl.lang.GSLImmutableValue;
import kp.gsl.lang.GSLInteger;
import kp.gsl.lang.GSLIterator;
import kp.gsl.lang.GSLList;
import kp.gsl.lang.GSLMap;
import kp.gsl.lang.GSLObject;
import kp.gsl.lang.GSLRawBytes;
import kp.gsl.lang.GSLReference;
import kp.gsl.lang.GSLString;
import kp.gsl.lang.GSLStruct;
import kp.gsl.lang.GSLTuple;
import kp.gsl.lang.GSLValue;
import static kp.gsl.lang.GSLValue.FALSE;
import static kp.gsl.lang.GSLValue.NULL;
import static kp.gsl.lang.GSLValue.TRUE;
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
    
    private GSLValue executeFunction(GSLGlobals globals, byte[] code, Generator gen, GSLValue self, GSLVarargs args)
    {
        int sit, inst;
        if(code[ScriptConstants.CODE_IS_GEN] != 0)
        {
            if(gen == null)
                return new Generator(code, self, args);
            inst = gen.inst;
        }
        else inst = ScriptConstants.CODE_INIT;
        
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
                case STRUCT_NEW: stack[sit - 1] = GSLStruct.assimilate(stack[sit - 1].cast()); break;
                case BLUEPRINT_NEW: {
                    var base = stack[sit - 1].<AbstractObject>cast();
                    stack[sit - 1] = GSLBlueprint.assimilate(base, base.getParent());
                } break;
                case OBJECT_NEW: {
                    if(code[inst++] != 0)
                    {
                        var parent = stack[sit - 1];
                        stack[sit - 1] = new GSLObject(parent == NULL ? null : parent);
                    }
                    else stack[sit++] = new GSLObject();
                } break;
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
                
                case PLUS: stack[(--sit) - 1] = stack[sit - 1].operatorPlus(stack[sit]); break;
                case MINUS: stack[(--sit) - 1] = stack[sit - 1].operatorMinus(stack[sit]); break;
                case MULTIPLY: stack[(--sit) - 1] = stack[sit - 1].operatorMultiply(stack[sit]); break;
                case DIVIDE: stack[(--sit) - 1] = stack[sit - 1].operatorDivide(stack[sit]); break;
                case REMAINDER: stack[(--sit) - 1] = stack[sit - 1].operatorRemainder(stack[sit]); break;
                case NEGATIVE: stack[sit - 1] = stack[sit - 1].operatorNegative(); break;
                case INCREASE: stack[sit - 1] = stack[sit - 1].operatorIncrease(); break;
                case DECREASE: stack[sit - 1] = stack[sit - 1].operatorDecrease(); break;
                
                case BW_SFH_L: stack[(--sit) - 1] = stack[sit - 1].operatorBitwiseShiftLeft(stack[sit]); break;
                case BW_SFH_R: stack[(--sit) - 1] = stack[sit - 1].operatorBitwiseShiftRight(stack[sit]); break;
                case BW_AND: stack[(--sit) - 1] = stack[sit - 1].operatorBitwiseAnd(stack[sit]); break;
                case BW_OR: stack[(--sit) - 1] = stack[sit - 1].operatorBitwiseOr(stack[sit]); break;
                case BW_XOR: stack[(--sit) - 1] = stack[sit - 1].operatorBitwiseXor(stack[sit]); break;
                case BW_NOT: stack[sit - 1] = stack[sit - 1].operatorBitwiseNot(); break;
                
                case EQ: stack[(--sit) - 1] = stack[sit - 1].operatorEquals(stack[sit]); break;
                case NOEQ: stack[(--sit) - 1] = stack[sit - 1].operatorEquals(stack[sit]).boolValue() ? FALSE : TRUE; break;
                case T_EQ: stack[(--sit) - 1] = stack[sit - 1].operatorTypedEquals(stack[sit]); break;
                case T_NOEQ: stack[(--sit) - 1] = stack[sit - 1].operatorTypedNotEquals(stack[sit]); break;
                case GR: stack[(--sit) - 1] = stack[sit - 1].operatorGreater(stack[sit]); break;
                case SM: stack[(--sit) - 1] = stack[sit - 1].operatorSmaller(stack[sit]); break;
                case GREQ: stack[(--sit) - 1] = stack[sit - 1].operatorGreaterEquals(stack[sit]); break;
                case SMEQ: stack[(--sit) - 1] = stack[sit - 1].operatorSmallerEquals(stack[sit]); break;
                case IS_DEF: stack[sit - 1] = stack[sit - 1].isNull() ? TRUE : FALSE; break;
                case IS_UNDEF: stack[sit - 1] = stack[sit - 1].isNull() ? FALSE : TRUE; break;
                case NOT: stack[sit - 1] = stack[sit - 1].operatorNegate(); break;
                case CONCAT: stack[(--sit) - 1] = new GSLString(stack[sit - 1].toString().concat(stack[sit].toString())); break;
                case LENGTH: stack[sit - 1] = new GSLInteger(stack[sit - 1].operatorLength()); break;
                case TYPEID: stack[sit - 1] = new GSLString(stack[sit - 1].getGSLDataType().name().toLowerCase()); break;
                case ITERATOR: stack[sit - 1] = stack[sit - 1].operatorIterator(); break;
                
                case GET: stack[(--sit) - 1] = stack[sit - 1].operatorGet(stack[sit]); break;
                case GET_I: stack[sit - 1] = stack[sit - 1].operatorGet(code[inst++] & 0xff); break;
                case SET: stack[(sit -= 2) - 1].operatorSet(stack[sit], stack[sit + 1]); break;
                case SET_I: stack[(--sit) - 1].operatorSet(code[inst++] & 0xff, stack[sit]); break;
                case PEEK: stack[sit - 1] = stack[sit - 1].operatorPeek(); break;
                case ADD: stack[sit -= 2].operatorAdd(stack[sit + 1]); break;
                
                case PROPERTY_GET: stack[sit - 1] = stack[sit - 1].operatorGetProperty(identifiers[code[inst++] & 0xff]); break;
                case PROPERTY_GET16: stack[sit - 1] = stack[sit - 1].operatorGetProperty(identifiers[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)]); break;
                case PROPERTY_SET: stack[(--sit) - 1].operatorSetProperty(identifiers[code[inst++] & 0xff], stack[sit]); break;
                case PROPERTY_SET16: stack[(--sit) - 1].operatorSetProperty(identifiers[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)], stack[sit]); break;
                
                case IF: if(stack[--sit].boolValue()) inst = code[inst++] & 0xff; else inst += 2; break;
                case IF16: if(stack[--sit].boolValue()) inst = (code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8); else inst += 2; break;
                
                case IF_EQ: if(stack[--sit].operatorEquals(stack[--sit]).boolValue()) inst = code[inst++] & 0xff; else inst += 2; break;
                case IF_EQ16: if(stack[--sit].operatorEquals(stack[--sit]).boolValue()) inst = (code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8); else inst += 2; break;
                case IF_NOEQ: if(stack[--sit].operatorEquals(stack[--sit]).boolValue()) inst += 2; else inst = code[inst++] & 0xff; break;
                case IF_NOEQ16: if(stack[--sit].operatorEquals(stack[--sit]).boolValue()) inst += 2; else inst = (code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8); break;
                case IF_T_EQ: if(stack[--sit].operatorTypedEquals(stack[--sit]).boolValue()) inst = code[inst++] & 0xff; else inst += 2; break;
                case IF_T_EQ16: if(stack[--sit].operatorTypedEquals(stack[--sit]).boolValue()) inst = (code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8); else inst += 2; break;
                case IF_TNEQ: if(stack[--sit].operatorTypedNotEquals(stack[--sit]).boolValue()) inst = code[inst++] & 0xff; else inst += 2; break;
                case IF_TNEQ16: if(stack[--sit].operatorTypedNotEquals(stack[--sit]).boolValue()) inst = (code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8); else inst += 2; break;
                case IF_GR: if(stack[--sit].operatorGreater(stack[--sit]).boolValue()) inst = code[inst++] & 0xff; else inst += 2; break;
                case IF_GR16: if(stack[--sit].operatorGreater(stack[--sit]).boolValue()) inst = (code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8); else inst += 2; break;
                case IF_SM: if(stack[--sit].operatorSmaller(stack[--sit]).boolValue()) inst = code[inst++] & 0xff; else inst += 2; break;
                case IF_SM16: if(stack[--sit].operatorSmaller(stack[--sit]).boolValue()) inst = (code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8); else inst += 2; break;
                case IF_GREQ: if(stack[--sit].operatorGreaterEquals(stack[--sit]).boolValue()) inst = code[inst++] & 0xff; else inst += 2; break;
                case IF_GREQ16: if(stack[--sit].operatorGreaterEquals(stack[--sit]).boolValue()) inst = (code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8); else inst += 2; break;
                case IF_SMEQ: if(stack[--sit].operatorSmallerEquals(stack[--sit]).boolValue()) inst = code[inst++] & 0xff; else inst += 2; break;
                case IF_SMEQ16: if(stack[--sit].operatorSmallerEquals(stack[--sit]).boolValue()) inst = (code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8); else inst += 2; break;
                case IF_NOT: if(stack[--sit].operatorNegate().boolValue()) inst = code[inst++] & 0xff; else inst += 2; break;
                case IF_NOT16: if(stack[--sit].operatorNegate().boolValue()) inst = (code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8); else inst += 2; break;
                
                case IF_DEF: if(stack[--sit].isNull()) inst = code[inst++] & 0xff; else inst += 2; break;
                case IF_DEF16: if(stack[--sit].isNull()) inst = (code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8); else inst += 2; break;
                case IF_UNDEF: if(!stack[--sit].isNull()) inst = code[inst++] & 0xff; else inst += 2; break;
                case IF_UNDEF16: if(!stack[--sit].isNull()) inst = (code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8); else inst += 2; break;
                
                case LOCAL_CALL: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        stack[sit++] = executeFunction(globals, functions[code[inst++] & 0xff], null, NULL, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        stack[sit++] = executeFunction(globals, functions[code[inst++] & 0xff], null, NULL, a);
                    }
                } break;
                case LOCAL_CALL16: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        stack[sit++] = executeFunction(globals, functions[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)], null, NULL, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        stack[sit++] = executeFunction(globals, functions[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)], null, NULL, a);
                    }
                } break;
                case LOCAL_VCALL: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        executeFunction(globals, functions[code[inst++] & 0xff], null, NULL, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        executeFunction(globals, functions[code[inst++] & 0xff], null, NULL, a);
                    }
                } break;
                case LOCAL_VCALL16: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        executeFunction(globals, functions[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)], null, NULL, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        executeFunction(globals, functions[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)], null, NULL, a);
                    }
                } break;
                
                case CALL: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        stack[sit - 1] = stack[sit - 1].operatorCall(NULL, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        stack[sit - 1] = stack[sit - 1].operatorCall(NULL, a);
                    }
                } break;
                case VCALL: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        stack[--sit].operatorCall(NULL, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        stack[--sit].operatorCall(NULL, a);
                    }
                } break;
                
                case INVOKE: {
                    int len;
                    var s = stack[sit - 1];
                    if((len = code[inst++] & 0xff) < 1)
                        stack[sit - 1] = s.operatorGetProperty(identifiers[code[inst++] & 0xff]).operatorCall(s, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        stack[sit - 1] = s.operatorGetProperty(identifiers[code[inst++] & 0xff]).operatorCall(s, a);
                    }
                } break;
                case INVOKE16: {
                    int len;
                    var s = stack[sit - 1];
                    if((len = code[inst++] & 0xff) < 1)
                        stack[sit - 1] = s.operatorGetProperty(identifiers[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)]).operatorCall(s, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        stack[sit - 1] = s.operatorGetProperty(identifiers[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)]).operatorCall(s, a);
                    }
                } break;
                case VINVOKE: {
                    int len;
                    var s = stack[--sit];
                    if((len = code[inst++] & 0xff) < 1)
                        s.operatorGetProperty(identifiers[code[inst++] & 0xff]).operatorCall(s, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        s.operatorGetProperty(identifiers[code[inst++] & 0xff]).operatorCall(s, a);
                    }
                } break;
                case VINVOKE16: {
                    int len;
                    var s = stack[--sit];
                    if((len = code[inst++] & 0xff) < 1)
                        s.operatorGetProperty(identifiers[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)]).operatorCall(s, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        s.operatorGetProperty(identifiers[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)]).operatorCall(s, a);
                    }
                } break;
                
                case LIBE_LOAD: stack[sit++] = libElements[code[inst++] & 0xff].toGSLValue(globals); break;
                case LIBE_LOAD16: stack[sit++] = libElements[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)].toGSLValue(globals); break;
                case LIBE_GET: stack[sit - 1] = libElements[code[inst++] & 0xff].operatorGet(globals, stack[sit - 1]);
                case LIBE_GET16: stack[sit - 1] = libElements[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)].operatorGet(globals, stack[sit - 1]);
                case LIBE_GET_I: stack[sit++] = libElements[code[inst++] & 0xff].operatorGet(globals, code[inst++] & 0xff);
                case LIBE_GET_I16: stack[sit++] = libElements[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)].operatorGet(globals, code[inst++] & 0xff);
                case LIBE_P_GET: stack[sit++] = libElements[code[inst++] & 0xff].operatorGetProperty(globals, identifiers[code[inst++] & 0xff]);
                case LIBE_P16_GET: stack[sit++] = libElements[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)].operatorGetProperty(globals, identifiers[code[inst++] & 0xff]);
                case LIBE_P_GET16: stack[sit++] = libElements[code[inst++] & 0xff].operatorGetProperty(globals, identifiers[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)]);
                case LIBE_P16_GET16: stack[sit++] = libElements[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)]
                        .operatorGetProperty(globals, identifiers[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)]);
                case LIBE_CALL: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        stack[sit++] = libElements[code[inst++] & 0xff].operatorCall(globals, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        stack[sit++] = libElements[code[inst++] & 0xff].operatorCall(globals, a);
                    }
                } break;
                case LIBE_CALL16: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        stack[sit++] = libElements[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)].operatorCall(globals, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        stack[sit++] = libElements[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)].operatorCall(globals, a);
                    }
                } break;
                case LIBE_VCALL: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        libElements[code[inst++] & 0xff].operatorCall(globals, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        libElements[code[inst++] & 0xff].operatorCall(globals, a);
                    }
                } break;
                case LIBE_VCALL16: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        libElements[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)].operatorCall(globals, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        libElements[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)].operatorCall(globals, a);
                    }
                } break;
                case LIBE_NEW: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        stack[sit++] = libElements[code[inst++] & 0xff].operatorNew(globals, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        stack[sit++] = libElements[code[inst++] & 0xff].operatorNew(globals, a);
                    }
                } break;
                case LIBE_NEW16: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        stack[sit++] = libElements[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)].operatorNew(globals, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        stack[sit++] = libElements[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)].operatorNew(globals, a);
                    }
                } break;
                case LIBE_VNEW: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        libElements[code[inst++] & 0xff].operatorNew(globals, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        libElements[code[inst++] & 0xff].operatorNew(globals, a);
                    }
                } break;
                case LIBE_VNEW16: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        libElements[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)].operatorNew(globals, NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        libElements[(code[inst++] & 0xff) | ((code[inst++] & 0xff) << 8)].operatorNew(globals, a);
                    }
                } break;
                
                case ARGS_TO_ARRAY: stack[code[inst++] & 0xff] = argsToArray(args, code[inst++] & 0xff); break;
                case ARG_TO_VAR: stack[sit++] = stack[code[inst++] & 0xff] = args.arg(code[inst++] & 0xff); break;
                
                case NEW: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        stack[sit - 1] = stack[sit - 1].operatorNew(NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        stack[sit - 1] = stack[sit - 1].operatorNew(a);
                    }
                } break;
                case VNEW: {
                    int len;
                    if((len = code[inst++] & 0xff) < 1)
                        stack[--sit].operatorNew(NO_ARGS);
                    else
                    {
                        var a = new StackCopyArgs(stack, sit, len);
                        sit -= len;
                        stack[--sit].operatorNew(a);
                    }
                } break;
                case SELF: stack[sit++] = self; break;
                case CLASS: stack[sit - 1] = stack[sit - 1].<AbstractObject>cast().getParent(); break;
                case BASE: stack[sit - 1] = stack[sit - 1].<AbstractObject>cast().getParent().<AbstractObject>cast().getParent(); break;
                
                case YIELD: gen.yield(inst); return stack[--sit];
                case YIELD_NULL: gen.yield(inst); return NULL;
                case YIELD_INT: gen.yield(inst); return stack[--sit].operatorCastInteger();
                case YIELD_FLOAT: gen.yield(inst); return stack[--sit].operatorCastFloat();
                case YIELD_BOOL: gen.yield(inst); return stack[--sit].operatorCastBoolean();
                case YIELD_STRING: gen.yield(inst); return stack[--sit].operatorCastString();
                case YIELD_CTUPLE: gen.yield(inst); return stack[--sit].operatorCastConstTuple();
                case YIELD_CMAP: gen.yield(inst); return stack[--sit].operatorCastConstMap();
                case YIELD_FUNCTION: gen.yield(inst); return stack[--sit].operatorCastFunction();
                case YIELD_LIST: gen.yield(inst); return stack[--sit].operatorCastList();
                case YIELD_TUPLE: gen.yield(inst); return stack[--sit].operatorCastTuple();
                case YIELD_MAP: gen.yield(inst); return stack[--sit].operatorCastMap();
                case YIELD_STRUCT: gen.yield(inst); return stack[--sit].operatorCastStruct();
                case YIELD_BPRINT: gen.yield(inst); return stack[--sit].operatorCastBlueprint();
                case YIELD_OBJECT: gen.yield(inst); return stack[--sit].operatorCastObject();
                case YIELD_ITERATOR: gen.yield(inst); return stack[--sit].operatorCastIterator();
                case YIELD_BYTES: gen.yield(inst); return stack[--sit].operatorCastRawBytes();
                case YIELD_NATIVE: gen.yield(inst); return stack[--sit].operatorCastNative();
            }
        }
    }
    
    public final GSLFunction getLocalFunction(int index)
    {
        return functionCache[index] == null ? functionCache[index] : (functionCache[index] = new LocalFunction(index));
    }
    
    private static GSLTuple argsToArray(GSLVarargs args, int offset)
    {
        if(offset >= args.numberOfArguments())
            return new GSLTuple(new GSLValue[]{});
        if(offset < 1)
            return new GSLTuple(GSLVarargs.varargsAsArray(args));
        return new GSLTuple(GSLVarargs.varargsAsArray(args, offset));
    }
    
    private class LocalFunction extends GSLFunction
    {
        final int functionIdx;
        
        private LocalFunction(int functionIdx) { this.functionIdx = functionIdx; }
        
        @Override
        public GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            return executeFunction(globals, functions[functionIdx], null, self, args);
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
                return executeFunction(globals, functions[functionIdx], null, self, (args.numberOfArguments() > 0 ? GSLVarargs.varargsOf(inners, args) : inners));
            return executeFunction(globals, functions[functionIdx], null, self, args);
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
    
    private final class Generator extends GSLIterator
    {
        private final byte[] code;
        private final GSLValue self;
        private final GSLVarargs args;
        private int inst = ScriptConstants.CODE_INIT;
        private boolean hasNext = true;
        private GSLValue next;
        
        private Generator(byte[] code, GSLValue self, GSLVarargs args)
        {
            this.code = code;
            this.self = self;
            this.args = args;
            execute();
        }

        @Override
        public boolean hasNext() { return next != null; }

        @Override
        public final GSLValue next()
        {
            var result = next;
            execute();
            return result == null ? NULL : result;
        }
        
        private void execute()
        {
            if(hasNext)
            {
                hasNext = false;
                var result = executeFunction(globals, code, this, self, args);
                next = hasNext ? result : null;
            }
        }
        
        private void yield(int instructionIdx)
        {
            hasNext = true;
            inst = instructionIdx;
        }
    }
}
