/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script;

/**
 *
 * @author Asus
 */
public interface Instruction
{
    int NOOP            = 0x00; // [0] => [0]
    
    int LOAD_CONST      = 0x01; // [0] => [+1] <const_idx>
    int LOAD_CONST16    = 0x02; // [0] => [+1] <const_idx|0-7> <const_idx|8-15>
    int LOAD_VAR        = 0x03; // [0] => [+1] <var_idx>
    int LOAD_FUNCTION   = 0x04; // [0] => [+1] <func_idx>
    int LOAD_FUNCTION16 = 0x05; // [0] => [+1] <func_idx|0-7> <func_idx|8-15>
    int LOAD_CLOSURE    = 0x06; // [-?>=0] => [+1] <pars_count> <func_idx>
    int LOAD_CLOSURE16  = 0x07; // [-?>=0] => [+1] <pars_count> <func_idx|0-7> <func_idx|8-15>
    int LOAD_GLOBAL     = 0x08; // [0] => [+1] <identifier_idx>
    int LOAD_GLOBAL16   = 0x09; // [0] => [+1] <identifier_idx|0-7> <identifier_idx|8-15>
    int LOAD_NULL       = 0x0A; // [0] => [+1]
    
    int STORE_GLOBAL    = 0x0B; // [-1] => [0] <identifier_idx>
    int STORE_GLOBAL16  = 0x0C; // [-1] => [0] <identifier_idx|0-7> <identifier_idx|8-15>
    int STORE_VAR       = 0x0D; // [-1] => [0] <var_idx>
    int ST_VAR_NULL     = 0x0E; // [-1] => [0] <var_idx>
    int ST_VAR_INT      = 0x0F; // [-1] => [0] <var_idx>
    int ST_VAR_FLOAT    = 0x10; // [-1] => [0] <var_idx>
    int ST_VAR_BOOL     = 0x11; // [-1] => [0] <var_idx>
    int ST_VAR_STRING   = 0x12; // [-1] => [0] <var_idx>
    int ST_VAR_CTUPLE   = 0x13; // [-1] => [0] <var_idx>
    int ST_VAR_CMAP     = 0x14; // [-1] => [0] <var_idx>
    int ST_VAR_FUNCTION = 0x15; // [-1] => [0] <var_idx>
    int ST_VAR_LIST     = 0x16; // [-1] => [0] <var_idx>
    int ST_VAR_TUPLE    = 0x17; // [-1] => [0] <var_idx>
    int ST_VAR_MAP      = 0x18; // [-1] => [0] <var_idx>
    int ST_VAR_STRUCT   = 0x19; // [-1] => [0] <var_idx>
    int ST_VAR_BPRINT   = 0x1A; // [-1] => [0] <var_idx>
    int ST_VAR_OBJECT   = 0x1B; // [-1] => [0] <var_idx>
    int ST_VAR_ITERATOR = 0x1C; // [-1] => [0] <var_idx>
    int ST_VAR_BYTES    = 0x1D; // [-1] => [0] <var_idx>
    int ST_VAR_NATIVE   = 0x1E; // [-1] => [0] <var_idx>
    
    int LIST_NEW        = 0x1F; // [0] => [+1]
    int TUPLE_NEW       = 0x20; // [-1] => [+1]
    int MAP_NEW         = 0x21; // [0] => [+1]
    int STRUCT_NEW      = 0x22; // [-1] => [+1]
    int BLUEPRINT_NEW   = 0x23; // [-1] => [+1]
    int OBJECT_NEW      = 0x24; // [bool_has_parent ? -1 : 0] => [+1] <bool_has_parent>
    int ITERATOR_NEW    = 0x25; // [-2] => [+1]
    int BYTES_NEW       = 0x26; // [-1] => [+1]
    
    int CAST_INT        = 0x27; // [-1] => [+1]
    int CAST_FLOAT      = 0x28; // [-1] => [+1]
    int CAST_BOOL       = 0x29; // [-1] => [+1]
    int CAST_STRING     = 0x2A; // [-1] => [+1]
    int CAST_CTUPLE     = 0x2B; // [-1] => [+1]
    int CAST_CMAP       = 0x2C; // [-1] => [+1]
    int CAST_FUNCTION   = 0x2D; // [-1] => [+1]
    int CAST_LIST       = 0x2E; // [-1] => [+1]
    int CAST_TUPLE      = 0x2F; // [-1] => [+1]
    int CAST_MAP        = 0x30; // [-1] => [+1]
    int CAST_STRUCT     = 0x31; // [-1] => [+1]
    int CAST_BPRINT     = 0x32; // [-1] => [+1]
    int CAST_OBJECT     = 0x33; // [-1] => [+1]
    int CAST_ITERATOR   = 0x34; // [-1] => [+1]
    int CAST_BYTES      = 0x35; // [-1] => [+1]
    int CAST_NATIVE     = 0x36; // [-1] => [+1]
    
    int REF_NEW         = 0x37; // [0] => [+1] <var_idx>
    int LOAD_REF        = 0x38; // [-1] => [+1]
    int STORE_REF       = 0x39; // [-2] => [0]
    
    int POP             = 0x3A; // [-1] => [0]
    int SWAP            = 0x3B; // [-2] => [+2]
    int SWAP2           = 0x3C; // [-3] => [+3]
    int DUP             = 0x3D; // [-1] => [+2]
    int DUP2            = 0x3E; // [-2] => [+3]
    
    int GOTO            = 0x3F; // [0] => [0] <instruction_idx> <IGNORED>
    int GOTO16          = 0x40; // [0] => [0] <instruction_idx|0-7> <instruction_idx|8-15>
    
    int RETURN          = 0x41; // [-1] => [0]
    int RETURN_NULL     = 0x42; // [0] => [0]
    int RETURN_INT      = 0x43; // [-1] => [0]
    int RETURN_FLOAT    = 0x44; // [-1] => [0]
    int RETURN_BOOL     = 0x45; // [-1] => [0]
    int RETURN_STRING   = 0x46; // [-1] => [0]
    int RETURN_CTUPLE   = 0x47; // [-1] => [0]
    int RETURN_CMAP     = 0x48; // [-1] => [0]
    int RETURN_FUNCTION = 0x49; // [-1] => [0]
    int RETURN_LIST     = 0x4A; // [-1] => [0]
    int RETURN_TUPLE    = 0x4B; // [-1] => [0]
    int RETURN_MAP      = 0x4C; // [-1] => [0]
    int RETURN_STRUCT   = 0x4D; // [-1] => [0]
    int RETURN_BPRINT   = 0x4E; // [-1] => [0]
    int RETURN_OBJECT   = 0x4F; // [-1] => [0]
    int RETURN_ITERATOR = 0x50; // [-1] => [0]
    int RETURN_BYTES    = 0x51; // [-1] => [0]
    int RETURN_NATIVE   = 0x52; // [-1] => [0]
    
    int PLUS            = 0x53; // [-2] => [+1]
    int MINUS           = 0x54; // [-2] => [+1]
    int MULTIPLY        = 0x55; // [-2] => [+1]
    int DIVIDE          = 0x56; // [-2] => [+1]
    int REMAINDER       = 0x57; // [-2] => [+1]
    int NEGATIVE        = 0x58; // [-1] => [+1]
    int INCREASE        = 0x59; // [-1] => [+1]
    int DECREASE        = 0x5A; // [-1] => [+1]
    
    int BW_SFH_L        = 0x5B; // [-2] => [+1]
    int BW_SFH_R        = 0x5C; // [-2] => [+1]
    int BW_AND          = 0x5D; // [-2] => [+1]
    int BW_OR           = 0x5E; // [-2] => [+1]
    int BW_XOR          = 0x5F; // [-2] => [+1]
    int BW_NOT          = 0x60; // [-1] => [+1]
    
    int EQ              = 0x61; // [-2] => [+1]
    int NOEQ            = 0x62; // [-2] => [+1]
    int T_EQ            = 0x63; // [-2] => [+1]
    int T_NOEQ          = 0x64; // [-2] => [+1]
    int GR              = 0x65; // [-2] => [+1]
    int SM              = 0x66; // [-2] => [+1]
    int GREQ            = 0x67; // [-2] => [+1]
    int SMEQ            = 0x68; // [-2] => [+1]
    int IS_DEF          = 0x69; // [-1] => [+1]
    int IS_UNDEF        = 0x6A; // [-1] => [+1]
    int NOT             = 0x6B; // [-1] => [+1]
    int CONCAT          = 0x6C; // [-2] => [+1]
    int LENGTH          = 0x6D; // [-1] => [+1]
    int TYPEID          = 0x6E; // [-1] => [+1]
    int ITERATOR        = 0x6F; // [-1] => [+1]
    
    int GET             = 0x70; // [-2] => [+1]
    int GET_I           = 0x71; // [-1] => [+1] <local_value>
    int SET             = 0x72; // [-3] => [0]
    int SET_I           = 0x73; // [-2] => [0] <local_value>
    int PEEK            = 0x74; // [-1] => [1]
    int ADD             = 0x75; // [-2] => [0]
    
    int PROPERTY_GET    = 0x76; // [-1] => [+1] <identifier_idx>
    int PROPERTY_GET16  = 0x77; // [-1] => [+1] <identifier_idx|0-7> <identifier_idx|8-15>
    int PROPERTY_SET    = 0x78; // [-2] => [0] <identifier_idx>
    int PROPERTY_SET16  = 0x79; // [-2] => [0] <identifier_idx|0-7> <identifier_idx|8-15>
    
    int IF              = 0x7A; // [-1] => [0] <instruction_idx> <IGNORED>
    int IF16            = 0x7B; // [-1] => [0] <instruction_idx|0-7> <instruction_idx|8-15>
    
    int IF_EQ           = 0x7C; // [-2] => [0] <instruction_idx> <IGNORED>
    int IF_EQ16         = 0x7D; // [-2] => [0] <instruction_idx|0-7> <instruction_idx|8-15>
    int IF_NOEQ         = 0x7E; // [-2] => [0] <instruction_idx> <IGNORED>
    int IF_NOEQ16       = 0x7F; // [-2] => [0] <instruction_idx|0-7> <instruction_idx|8-15>
    int IF_T_EQ         = 0x80; // [-2] => [0] <instruction_idx> <IGNORED>
    int IF_T_EQ16       = 0x81; // [-2] => [0] <instruction_idx|0-7> <instruction_idx|8-15>
    int IF_TNEQ         = 0x82; // [-2] => [0] <instruction_idx> <IGNORED>
    int IF_TNEQ16       = 0x83; // [-2] => [0] <instruction_idx|0-7> <instruction_idx|8-15>
    int IF_GR           = 0x84; // [-2] => [0] <instruction_idx> <IGNORED>
    int IF_GR16         = 0x85; // [-2] => [0] <instruction_idx|0-7> <instruction_idx|8-15>
    int IF_SM           = 0x86; // [-2] => [0] <instruction_idx> <IGNORED>
    int IF_SM16         = 0x87; // [-2] => [0] <instruction_idx|0-7> <instruction_idx|8-15>
    int IF_GREQ         = 0x88; // [-2] => [0] <instruction_idx> <IGNORED>
    int IF_GREQ16       = 0x89; // [-2] => [0] <instruction_idx|0-7> <instruction_idx|8-15>
    int IF_SMEQ         = 0x8A; // [-2] => [0] <instruction_idx> <IGNORED>
    int IF_SMEQ16       = 0x8B; // [-2] => [0] <instruction_idx|0-7> <instruction_idx|8-15>
    int IF_NOT          = 0x8C; // [-1] => [0] <instruction_idx> <IGNORED>
    int IF_NOT16        = 0x8D; // [-1] => [0] <instruction_idx|0-7> <instruction_idx|8-15>
    
    int IF_DEF          = 0x8E; // [-1] => [0] <instruction_idx> <IGNORED>
    int IF_DEF16        = 0x8F; // [-1] => [0] <instruction_idx|0-7> <instruction_idx|8-15>
    int IF_UNDEF        = 0x90; // [-1] => [0] <instruction_idx> <IGNORED>
    int IF_UNDEF16      = 0x91; // [-1] => [0] <instruction_idx|0-7> <instruction_idx|8-15>
    
    int LOCAL_CALL      = 0x92; // [-?>=0] => [+1] <args_len> <func_idx>
    int LOCAL_CALL16    = 0x93; // [-?>=0] => [+1] <args_len> <func_idx|0-7> <func_idx|8-15>
    int LOCAL_VCALL     = 0x94; // [-?>=0] => [0] <args_len> <func_idx>
    int LOCAL_VCALL16   = 0x95; // [-?>=0] => [0] <args_len> <func_idx|0-7> <func_idx|8-15>
    
    int CALL            = 0x96; // [-?>=1] => [+1] <args_len>
    int VCALL           = 0x97; // [-?>=1] => [0] <args_len>
    
    int INVOKE          = 0x98; // [-?>=1] => [+1] <args_len> <identifier_idx>
    int INVOKE16        = 0x99; // [-?>=1] => [+1] <args_len> <identifier_idx|0-7> <identifier_idx|8-15>
    int VINVOKE         = 0x9A; // [-?>=1] => [0] <args_len> <identifier_idx>
    int VINVOKE16       = 0x9B; // [-?>=1] => [0] <args_len> <identifier_idx|0-7> <identifier_idx|8-15>
    
    int LIBE_LOAD       = 0x9C; // [0] => [+1] <libelement_idx>
    int LIBE_LOAD16     = 0x9D; // [0] => [+1] <libelement_idx|0-7> <libelement_idx|8-15>
    int LIBE_GET        = 0x9E; // [-1] => [+1] <libelement_idx>
    int LIBE_GET16      = 0x9F; // [-1] => [+1] <libelement_idx|0-7> <libelement_idx|8-15>
    int LIBE_GET_I      = 0xA0; // [0] => [+1] <libelement_idx> <local_value>
    int LIBE_GET_I16    = 0xA1; // [0] => [+1] <libelement_idx|0-7> <libelement_idx|8-15> <local_value>
    int LIBE_P_GET      = 0xA2; // [0] => [+1] <libelement_idx> <identifier_idx>
    int LIBE_P16_GET    = 0xA3; // [0] => [+1] <libelement_idx> <identifier_idx|0-7> <identifier_idx|8-15>
    int LIBE_P_GET16    = 0xA4; // [0] => [+1] <libelement_idx|0-7> <libelement_idx|8-15> <identifier_idx>
    int LIBE_P16_GET16  = 0xA5; // [0] => [+1] <libelement_idx|0-7> <libelement_idx|8-15> <identifier_idx|0-7> <identifier_idx|8-15>
    int LIBE_CALL       = 0xA6; // [-?>=0] => [+1] <args_len> <libelement_idx>
    int LIBE_CALL16     = 0xA7; // [-?>=0] => [+1] <args_len> <libelement_idx|0-7> <libelement_idx|8-15>
    int LIBE_VCALL      = 0xA8; // [-?>=0] => [0] <args_len> <libelement_idx>
    int LIBE_VCALL16    = 0xA9; // [-?>=0] => [0] <args_len> <libelement_idx|0-7> <libelement_idx|8-15>
    int LIBE_NEW        = 0xAA; // [-?>=0] => [+1] <args_len> <libelement_idx>
    int LIBE_NEW16      = 0xAB; // [-?>=0] => [+1] <args_len> <libelement_idx|0-7> <libelement_idx|8-15>
    int LIBE_VNEW       = 0xAC; // [-?>=0] => [0] <args_len> <libelement_idx>
    int LIBE_VNEW16     = 0xAD; // [-?>=0] => [0] <args_len> <libelement_idx|0-7> <libelement_idx|8-15>
    
    int ARGS_TO_ARRAY   = 0xAE; // [0] => [0] <var_idx> <offset_idx>
    int ARG_TO_VAR      = 0xAF; // [0] => [+1] <var_idx> <arg_idx>
    
    int NEW             = 0xB0; // [-?>=0] => [+1] <args_len>
    int VNEW            = 0xB1; // [-?>=0] => [0] <args_len>
    int SELF            = 0xB2; // [0] => [+1]
    int CLASS           = 0xB3; // [-1] => [+1]
    int BASE            = 0xB4; // [-1] => [+1]
    
    int YIELD           = 0xB5; // [-1] => [0]
    int YIELD_NULL      = 0xB6; // [0] => [0]
    int YIELD_INT       = 0xB7; // [-1] => [0]
    int YIELD_FLOAT     = 0xB8; // [-1] => [0]
    int YIELD_BOOL      = 0xB9; // [-1] => [0]
    int YIELD_STRING    = 0xBA; // [-1] => [0]
    int YIELD_CTUPLE    = 0xBB; // [-1] => [0]
    int YIELD_CMAP      = 0xBC; // [-1] => [0]
    int YIELD_FUNCTION  = 0xBD; // [-1] => [0]
    int YIELD_LIST      = 0xBE; // [-1] => [0]
    int YIELD_TUPLE     = 0xBF; // [-1] => [0]
    int YIELD_MAP       = 0xC0; // [-1] => [0]
    int YIELD_STRUCT    = 0xC1; // [-1] => [0]
    int YIELD_BPRINT    = 0xC2; // [-1] => [0]
    int YIELD_OBJECT    = 0xC3; // [-1] => [0]
    int YIELD_ITERATOR  = 0xC4; // [-1] => [0]
    int YIELD_BYTES     = 0xC5; // [-1] => [0]
    int YIELD_NATIVE    = 0xC6; // [-1] => [0]
}
