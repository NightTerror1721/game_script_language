/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.bytecode;

import kp.gsl.lang.GSLDataType;

/**
 *
 * @author Asus
 */
final class ByteOps
{
    private ByteOps() {}
    
    public static final byte B_ZERO = (byte) 0;
    
    public static final byte i2b(int i) { return (byte) (i & 0xff); }
    public static final byte i2b(int i, int idx) { return (byte) ((i & 0xff) >>> (idx * 8)); }
    
    public static final int b2i(byte b) { return b & 0xff; }
    public static final int b2i(byte b, int idx) { return (b & 0xff) << (idx * 8); }
    
    public static final int bs2i(byte... bs)
    {
        int i = 0;
        for(int idx = 0; i < bs.length; i++)
            i |= b2i(bs[idx], idx);
        return i;
    }
    
    public static final String b2s(byte b) { return Integer.toString(b2i(b)); }
    public static final String bs2s(byte... bs) { return Integer.toString(bs2i(bs)); }
    
    public static final String i2s(int i) { return Integer.toString(i); }
    
    public static final int ibc(int i)
    {
        return i > 0xffffff ? 4 :
               i > 0xffff   ? 3 :
               i > 0xff     ? 2 :
               1;
    }
    
    public static final boolean isb(int i) { return ibc(i) == 1; }
    public static final boolean isw(int i) { return ibc(i) >= 2; }
    public static final boolean isdw(int i) { return ibc(i) >= 4; }
    
    private static final GSLDataType[] T_VALUES = GSLDataType.values();
    public static final GSLDataType b2t(byte b) { return b < 0 || b >= T_VALUES.length ? GSLDataType.NULL : T_VALUES[b]; }
    public static final GSLDataType i2t(int i) { return i < 0 || i >= T_VALUES.length ? GSLDataType.NULL : T_VALUES[i]; }
    public static final byte t2b(GSLDataType t) { return (byte) (t.ordinal() & 0xff); }
    public static final int t2i(GSLDataType t) { return t.ordinal() & 0xff; }
    
}
