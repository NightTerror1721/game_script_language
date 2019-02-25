/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

/**
 *
 * @author Asus
 */
public enum GSLDataType
{
    /* IMMUTABLE VALUES */
    NULL(Typename.NULL_T),
    
    INTEGER(Typename.INT),
    FLOAT(Typename.FLOAT),
    BOOLEAN(Typename.BOOL),
    STRING(Typename.STRING),
    
    CONST_TUPLE(Typename.CONST_TUPLE),
    CONST_MAP(Typename.CONST_MAP),
    
    FUNCTION(Typename.CALLABLE),
    
    
    /* MUTABLE VALUES */
    LIST(Typename.LIST),
    TUPLE(Typename.TUPLE),
    MAP(Typename.MAP),
    STRUCT(Typename.STRUCT),
    BLUEPRINT(Typename.BLUEPRINT),
    
    OBJECT(Typename.OBJECT),
    ITERATOR(Typename.ITERATOR),
    RAW_BYTES(Typename.BYTES),
    NATIVE(Typename.NATIVE);
    
    private final String langName;
    
    private GSLDataType(String langName) { this.langName = langName; }
    
    public final boolean isNumber()
    {
        switch(this)
        {
            case INTEGER: case FLOAT: return true;
            default: return false;
        }
    }
    
    public final String getLangTypeName() { return langName; }
    
    @Override
    public final String toString()
    {
        return langName;
    }
    
    /* TYPE_NAMES */
    public static interface Typename
    {
        String NULL_T = "null_t";
        
        String INT = "int";
        String FLOAT = "float";
        String BOOL = "bool";
        String STRING = "string";

        String CONST_TUPLE = "const_tuple";
        String CONST_MAP = "const_map";

        String CALLABLE = "callable";


        /* MUTABLE VALUES */
        String LIST = "list";
        String TUPLE = "tuple";
        String MAP = "map";
        String STRUCT = "struct";
        String BLUEPRINT = "blueprint";

        String OBJECT = "object";
        String ITERATOR = "iterator";
        String BYTES = "bytes";
        String NATIVE = "native";
    }
}
