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
    NULL,
    
    INTEGER,
    FLOAT,
    BOOLEAN,
    STRING,
    
    TUPLE,
    TABLE,
    
    FUNCTION,
    
    
    /* MUTABLE VALUES */
    LIST,
    VECTOR,
    MAP,
    STRUCT,
    BLUEPRINT,
    
    OBJECT,
    ITERATOR,
    RAW_BYTES,
    NATIVE;
    
    public final boolean isNumber()
    {
        switch(this)
        {
            case INTEGER: case FLOAT: return true;
            default: return false;
        }
    }
}
