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
public class DataType extends RawCode
{
    private final GSLDataType type;
    private final Operator cast;
    
    private DataType(GSLDataType type, Operator castOp)
    {
        this.type = type;
        this.cast = castOp;
    }
    
    public final Operator getCastOperator() { return cast; }
    
    @Override
    public CodeFragmentType getCodeFragmentType() { return CodeFragmentType.DATA_TYPE; }

    @Override
    public String toString() { return type == null ? "ANY" : type.name(); }
    
    
    
    
    /* ALL DATATYPES */
    public static final DataType NULL = new DataType(GSLDataType.NULL, null);
    public static final DataType INTEGER = new DataType(GSLDataType.INTEGER, Operator.CAST_INT);
    public static final DataType FLOAT = new DataType(GSLDataType.FLOAT, Operator.CAST_FLOAT);
    public static final DataType BOOLEAN = new DataType(GSLDataType.BOOLEAN, Operator.CAST_BOOLEAN);
    public static final DataType STRING = new DataType(GSLDataType.STRING, Operator.CAST_STRING);
    public static final DataType CONST_TUPLE = new DataType(GSLDataType.CONST_TUPLE, Operator.CAST_CONST_TUPLE);
    public static final DataType CONST_MAP = new DataType(GSLDataType.CONST_MAP, Operator.CAST_CONST_MAP);
    public static final DataType FUNCTION = new DataType(GSLDataType.FUNCTION, Operator.CAST_FUNCTION);
    public static final DataType LIST = new DataType(GSLDataType.LIST, Operator.CAST_LIST);
    public static final DataType TUPLE = new DataType(GSLDataType.TUPLE, Operator.CAST_TUPLE);
    public static final DataType MAP = new DataType(GSLDataType.MAP, Operator.CAST_MAP);
    public static final DataType STRUCT = new DataType(GSLDataType.STRUCT, Operator.CAST_STRUCT);
    public static final DataType BLUEPRINT = new DataType(GSLDataType.BLUEPRINT, Operator.CAST_BLUEPRINT);
    public static final DataType OBJECT = new DataType(GSLDataType.OBJECT, Operator.CAST_OBJECT);
    public static final DataType ITERATOR = new DataType(GSLDataType.ITERATOR, Operator.CAST_ITERATOR);
    public static final DataType RAW_BYTES = new DataType(GSLDataType.RAW_BYTES, Operator.CAST_BYTES);
    public static final DataType NATIVE = new DataType(GSLDataType.NATIVE, Operator.CAST_NATIVE);
    
    public static final DataType ANY = new DataType(null, null);
}
