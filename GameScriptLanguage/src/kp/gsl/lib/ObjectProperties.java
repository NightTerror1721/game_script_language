/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lib;

/**
 *
 * @author Asus
 */
public interface ObjectProperties
{
    String CAST_INTEGER = "__int__";
    String CAST_FLOAT = "__float__";
    String CAST_STRING = "__string__";
    String CAST_BOOL = "__bool__";
    String CAST_LIST = "__list__";
    String CAST_MAP = "__map__";
    
    String OP_EQ = "__eq__";
    String OP_GR = "__gr__";
    String OP_SM = "__sm__";
    String OP_GREQ = "__greq__";
    String OP_SMEQ = "__smeq__";
    String OP_NOT = "__not__";
    String OP_LEN = "__len__";
    
    String OP_PLUS = "__plus__";
    String OP_MINUS = "__minus__";
    String OP_MUL = "__mul__";
    String OP_DIV = "__div__";
    String OP_MOD = "__mod__";
    String OP_INC = "__inc__";
    String OP_DEC = "__dec__";
    String OP_NEG = "__neg__";
    
    String OP_BSHL = "__bshl__";
    String OP_BSHR = "__bshr__";
    String OP_BAND = "__band__";
    String OP_BOR = "__bor__";
    String OP_BXOR = "__bxor__";
    String OP_BNOT = "__bnot__";
    
    String OP_GET = "__get__";
    String OP_SET = "__set__";
    String OP_ADD = "__add__";
    
    String OP_CALL = "__call__";
    
    String OP_NEW = "__new__";
    
    String OP_ITER = "__iter__";
    String OP_HASNEXT = "__hasnext__";
    String OP_NEXT = "__next__";
    
    String OP_HASHCODE = "__hashcode__";
    
    String CONSTRUCTOR = "__init__";
}
