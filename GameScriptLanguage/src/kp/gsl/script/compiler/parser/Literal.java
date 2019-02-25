/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.compiler.parser;

import java.util.Map;
import java.util.regex.Pattern;
import kp.gsl.exception.CompilerError;
import kp.gsl.lang.GSLConstMap;
import kp.gsl.lang.GSLConstTuple;
import kp.gsl.lang.GSLDataType;
import kp.gsl.lang.GSLFloat;
import kp.gsl.lang.GSLImmutableValue;
import kp.gsl.lang.GSLInteger;
import kp.gsl.lang.GSLString;
import kp.gsl.lang.GSLValue;

/**
 *
 * @author Asus
 */
public final class Literal extends Statement
{
    public static final Literal NULL = new Literal(GSLValue.NULL);
    public static final Literal ZERO = new Literal(GSLValue.ZERO);
    public static final Literal ONE = new Literal(GSLValue.ONE);
    public static final Literal MINUSONE = new Literal(GSLValue.MINUSONE);
    public static final Literal TRUE = new Literal(GSLValue.TRUE);
    public static final Literal FALSE = new Literal(GSLValue.FALSE);
    
    private final GSLImmutableValue value;
    
    private Literal(GSLImmutableValue value)
    {
        this.value = value;
    }
    
    /*public final Literal fixDecimals()
    {
        if(value.isFloat())
        {
            double num = value.doubleValue();
            if(num == ((double)((int) num)))
                return valueOf((int) num);
        }
        return this;
    }*/
    
    public final GSLImmutableValue getGSLValue() { return value; }
    
    public final Literal operatorUnaryPlus() { return this; }
    public final Literal operatorUnaryMinus()
    {
        switch(value.getGSLDataType())
        {
            case INTEGER: return valueOf(-value.longValue());
            case FLOAT: return valueOf(-value.doubleValue());
            default: return this;
        }
    }
    public final Literal operatorNot() { return valueOf(value.operatorNegate()); }
    public final Literal operatorBitwiseNot() { return valueOf(value.operatorBitwiseNot()); }
    public final Literal operatorCastInt() { return valueOf(value.operatorCastInteger()); }
    public final Literal operatorCastFloat() { return valueOf(value.operatorCastFloat()); }
    public final Literal operatorCastBoolean() { return valueOf(value.operatorCastBoolean()); }
    public final Literal operatorCastString() { return valueOf(value.operatorCastString()); }
    public final Literal operatorCastConstTuple() { return valueOf(value.operatorCastConstTuple()); }
    public final Literal operatorCastConstMap() { return valueOf(value.operatorCastConstMap()); }
    public final Literal operatorCastFunction() { return valueOf(value.operatorCastFunction()); }
    public final Literal operatorLength() { return valueOf(value.operatorLength()); }
    public final Literal operatorIsdef() { return valueOf(!value.isNull()); }
    public final Literal operatorIsundef() { return valueOf(value.isNull()); }
    public final Literal operatorTypeid() { return valueOf(value.getGSLDataType().toString()); }
    public final Literal operatorMultiply(Literal other) { return valueOf(value.operatorMultiply(other.value)); }
    public final Literal operatorDivision(Literal other) { return valueOf(value.operatorDivide(other.value)); }
    public final Literal operatorRemainder(Literal other) { return valueOf(value.operatorRemainder(other.value)); }
    public final Literal operatorPlus(Literal other) { return valueOf(value.operatorPlus(other.value)); }
    public final Literal operatorMinus(Literal other) { return valueOf(value.operatorMinus(other.value)); }
    public final Literal operatorBitwiseShiftLeft(Literal other) { return valueOf(value.operatorBitwiseShiftLeft(other.value)); }
    public final Literal operatorBitwiseShiftRight(Literal other) { return valueOf(value.operatorBitwiseShiftRight(other.value)); }
    public final Literal operatorEquals(Literal other) { return valueOf(value.operatorEquals(other.value)); }
    public final Literal operatorNotEquals(Literal other) { return valueOf(value.operatorNotEquals(other.value)); }
    public final Literal operatorGreater(Literal other) { return valueOf(value.operatorGreater(other.value)); }
    public final Literal operatorSmaller(Literal other) { return valueOf(value.operatorSmaller(other.value)); }
    public final Literal operatorGreaterEquals(Literal other) { return valueOf(value.operatorGreaterEquals(other.value)); }
    public final Literal operatorSmallerEquals(Literal other) { return valueOf(value.operatorSmallerEquals(other.value)); }
    public final Literal operatorTypedEquals(Literal other) { return valueOf(value.operatorTypedEquals(other.value)); }
    public final Literal operatorTypedNotEquals(Literal other) { return valueOf(value.operatorTypedNotEquals(other.value)); }
    public final Literal operatorBitwiseAnd(Literal other) { return valueOf(value.operatorBitwiseAnd(other.value)); }
    public final Literal operatorBitwiseXor(Literal other) { return valueOf(value.operatorBitwiseXor(other.value)); }
    public final Literal operatorBitwiseOr(Literal other) { return valueOf(value.operatorBitwiseOr(other.value)); }
    public final Literal operatorConcat(Literal other) { return valueOf(value.toString().concat(other.value.toString())); }
    
    public final LiteralType getLiteralType() { return LiteralType.decode(value.getGSLDataType()); }

    @Override
    public final CodeFragmentType getCodeFragmentType() { return CodeFragmentType.LITERAL; }
    
    public static final Literal valueOf(int value) { return new Literal(new GSLInteger(value)); }
    public static final Literal valueOf(long value) { return new Literal(new GSLInteger(value)); }
    
    public static final Literal valueOf(float value) { return new Literal(new GSLFloat(value)); }
    public static final Literal valueOf(double value) { return new Literal(new GSLFloat(value)); }
    
    public static final Literal valueOf(boolean value) { return value ? TRUE : FALSE; }
    
    public static final Literal valueOf(char value) { return new Literal(new GSLInteger(value)); }
    public static final Literal valueOf(String value) { return new Literal(new GSLString(value)); }
    
    public static final Literal valueOf(GSLImmutableValue[] value) { return new Literal(new GSLConstTuple(value)); }
    
    public static final Literal valueOf(Map<GSLImmutableValue, GSLImmutableValue> value) { return new Literal(new GSLConstMap(value)); }
    
    public static final Literal valueOf(GSLImmutableValue value) { return new Literal(value); }
    
    private static Literal valueOf(GSLValue value)
    {
        return valueOf(value instanceof GSLImmutableValue ? (GSLImmutableValue) value : GSLValue.NULL);
    }
    
    
    private static final Pattern INTEGER_P = Pattern.compile("(0|0[xX])?[0-9]+[bBsSlL]?[uU]?");
    private static final Pattern FLOAT_P = Pattern.compile("[0-9]+(\\.[0-9]+)?[fFdD]?");
    private static final int BYTES_INT = 4;
    private static final int BYTES_LONG = 8;
    
    
    public static final Literal decodeNumber(String str) throws CompilerError
    {
        if(INTEGER_P.matcher(str).matches())
            return decodeInteger(str);
        if(FLOAT_P.matcher(str).matches())
            return decodeFloat(str);
        return null;
    }
    
    private static int base(String str)
    {
        if(str.length() <= 1)
            return 10;
        char c = str.charAt(0);
        if(c != '0')
            return 10;
        c = str.charAt(1);
        return c == 'x' || c == 'X' ? 16 : 8;
    }
    private static int integerLen(String str)
    {
        char c = str.charAt(str.length() - 1);
        switch(c)
        {
            case 'l': case 'L': return BYTES_LONG;
            default: return BYTES_INT;
        }
    }
    private static Literal decodeInteger(String str) throws CompilerError
    {
        int base = base(str);
        int bytes = integerLen(str);
        int start = base == 8 ? 1 : base == 16 ? 2 : 0;
        int end = str.length() - (bytes != BYTES_INT ? 1 : 0);
        str = str.substring(start, end);
        
        try
        {
            switch(bytes)
            {
                case BYTES_LONG: return valueOf(Long.parseLong(str, base));
                default: return valueOf(Integer.parseInt(str, base));
            }
        }
        catch(NumberFormatException ex)
        {
            throw new CompilerError("Invalid Integer literal: " + ex);
        }
    }
    
    private static Literal decodeFloat(String str)
    {
        return valueOf(Double.parseDouble(str));
    }

    @Override
    public final String toString() { return value.toString(); }
    
    
    public enum LiteralType
    {
        NULL,
        INTEGER,
        FLOAT,
        BOOLEAN,
        STRING,
        CONST_TUPLE,
        CONST_MAP;
        
        private static LiteralType decode(GSLDataType type)
        {
            switch(type)
            {
                case NULL: return NULL;
                case INTEGER: return INTEGER;
                case FLOAT: return FLOAT;
                case BOOLEAN: return BOOLEAN;
                case STRING: return STRING;
                case CONST_TUPLE: return CONST_TUPLE;
                case CONST_MAP: return CONST_MAP;
                default: throw new IllegalStateException();
            }
        }
        
        public final DataType getDataType()
        {
            switch(this)
            {
                case NULL: return DataType.ANY;
                case INTEGER: return DataType.INTEGER;
                case FLOAT: return DataType.FLOAT;
                case BOOLEAN: return DataType.BOOLEAN;
                case STRING: return DataType.STRING;
                case CONST_TUPLE: return DataType.CONST_TUPLE;
                case CONST_MAP: return DataType.CONST_MAP;
                default: return DataType.ANY;
            }
        }
    }
}
