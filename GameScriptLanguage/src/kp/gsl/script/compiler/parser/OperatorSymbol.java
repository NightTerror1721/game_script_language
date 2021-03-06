/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.compiler.parser;

import kp.gsl.lang.GSLDataType;

/**
 *
 * @author Asus
 */
public enum OperatorSymbol
{
    SUFIX_INCREMENT("++", 1, false, false),
    SUFIX_DECREMENT("--", 1, false, false),
    CALL("()", 1),
    INVOKE(".()", 1),
    NEW("new", 1),
    ARRAY_GET("[x]", 1),
    PROPERTY_GET(".", 1),
    PEEK("[]", 1),
    
    PREFIX_INCREMENT("++", 2, true, false),
    PREFIX_DECREMENT("--", 2, true, false),
    UNARY_PLUS("+", 2, true, false),
    UNARY_MINUS("-", 2, true, false),
    NOT("!", 2, true, true),
    BITWISE_NOT("~", 2, true, false),
    CAST_INT(GSLDataType.INTEGER.getLangTypeName(), 2, true, false),
    CAST_FLOAT(GSLDataType.FLOAT.getLangTypeName(), 2, true, false),
    CAST_BOOLEAN(GSLDataType.BOOLEAN.getLangTypeName(), 2, true, false),
    CAST_STRING(GSLDataType.STRING.getLangTypeName(), 2, true, false),
    CAST_CONST_TUPLE(GSLDataType.CONST_TUPLE.getLangTypeName(), 2, true, false),
    CAST_CONST_MAP(GSLDataType.CONST_MAP.getLangTypeName(), 2, true, false),
    CAST_FUNCTION(GSLDataType.FUNCTION.getLangTypeName(), 2, true, false),
    CAST_LIST(GSLDataType.LIST.getLangTypeName(), 2, true, false),
    CAST_TUPLE(GSLDataType.TUPLE.getLangTypeName(), 2, true, false),
    CAST_MAP(GSLDataType.MAP.getLangTypeName(), 2, true, false),
    CAST_STRUCT(GSLDataType.STRUCT.getLangTypeName(), 2, true, false),
    CAST_BLUEPRINT(GSLDataType.BLUEPRINT.getLangTypeName(), 2, true, false),
    CAST_OBJECT(GSLDataType.OBJECT.getLangTypeName(), 2, true, false),
    CAST_ITERATOR(GSLDataType.ITERATOR.getLangTypeName(), 2, true, false),
    CAST_BYTES(GSLDataType.RAW_BYTES.getLangTypeName(), 2, true, false),
    CAST_NATIVE(GSLDataType.NATIVE.getLangTypeName(), 2, true, false),
    LENGTH("length", 2, true, false),
    ISDEF("isdef", 2, true, true),
    ISUNDEF("!isdef", 2, true, true),
    TYPEID("typeid", 2, true, false),
    ITERATOR("iterator", 2, true, false),
    NEW_FUNCTION("function", 2, true, false),
    BASE("base", 2, true, false),
    CLASS("class", 2, true, false),
    
    MULTIPLICATION("*", 3),
    DIVISION("/", 3),
    REMAINDER("%", 3),
    
    ADDITION("+", 4),
    SUBTRACTION("-", 4),
    
    BITWISE_LEFT_SHIFT("<<", 5),
    BITWISE_RIGHT_SHIFT(">>", 5),
    
    GREATER_THAN(">", 6, false, true),
    SMALLER_THAN("<", 6, false, true),
    GREATER_EQUALS_THAN(">=", 6, false, true),
    SMALLER_EQUALS_THAN("<=", 6, false, true),
    
    EQUALS("==", 7, false, true),
    NOT_EQUALS("!=", 7, false, true),
    TYPED_EQUALS("===", 7, false, true),
    TYPED_NOT_EQUALS("!==", 7, false, true),
    
    BITWISE_AND("&", 8),
    
    BITWISE_XOR("^", 9),
    
    BITWISE_OR("|", 10),
    
    LOGICAL_AND("&&", 11),
    
    LOGICAL_OR("||", 12),
    
    CONCAT("..", 13),
    
    TERNARY_CONDITIONAL("?:", 14),
    
    ASSIGNMENT("=", 15, true, false),
    ASSIGNMENT_ADDITION("+=", 15, true, false),
    ASSIGNMENT_SUBTRACTION("-=", 15, true, false),
    ASSIGNMENT_MULTIPLICATION("*=", 15, true, false),
    ASSIGNMENT_DIVISION("/=", 15, true, false),
    ASSIGNMENT_REMAINDER("%=", 15, true, false),
    ASSIFNMENT_BITWISE_LEFT_SHIFT("<<=", 15, true, false),
    ASSIFNMENT_BITWISE_RIGHT_SHIFT(">>=", 15, true, false),
    ASSIFNMENT_BITWISE_AND("&=", 15, true, false),
    ASSIFNMENT_BITWISE_XOR("^=", 15, true, false),
    ASSIFNMENT_BITWISE_OR("|=", 15, true, false);
    
    private final String symbol;
    private final int priority;
    private final boolean rightToLeft;
    private final boolean conditional;
    
    private OperatorSymbol(String symbol, int priority, boolean rightToLeft, boolean conditional)
    {
        this.symbol = symbol;
        this.priority = priority;
        this.rightToLeft = rightToLeft;
        this.conditional = conditional;
    }
    private OperatorSymbol(String symbol, int priority) { this(symbol, priority, false, false); }
    
    public final String getSymbolName() { return symbol; }
    public final int getPriority() { return priority; }
    public final boolean hasRightToLeftOrder() { return rightToLeft; }
    public final boolean isConditional() { return conditional; }
    
    public final int comparePriority(OperatorSymbol op)
    {
        if(priority == op.priority)
            return hasRightToLeftOrder() || op.hasRightToLeftOrder() ? -1 : 0;
        return priority < op.priority ? 1 : -1;
    }
}
