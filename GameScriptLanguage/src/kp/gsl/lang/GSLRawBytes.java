/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kp.gsl.exception.NotPointerException;
import kp.gsl.exception.UnsupportedOperatorException;
import kp.gsl.lib.Def;

/**
 *
 * @author Asus
 */
public class GSLRawBytes extends GSLValue
{
    final byte[] bytes;
    
    public GSLRawBytes(byte[] bytes)
    {
        if(bytes == null)
            throw new NullPointerException();
        this.bytes = bytes;
    }
    
    public final byte[] bytes() { return bytes; }
    public final byte[] copyBytes() { return Arrays.copyOf(bytes, bytes.length); }
    
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.RAW_BYTES; }

    @Override
    public final int intValue() { return hashCode(); }

    @Override
    public final long longValue() { return hashCode(); }

    @Override
    public final float floatValue() { return hashCode(); }

    @Override
    public final double doubleValue() { return hashCode(); }
    
    @Override
    public final char charValue() { return (char) hashCode(); }

    @Override
    public final boolean boolValue() { return true; }

    @Override
    public final String toString() { return Arrays.toString(bytes); }

    @Override
    public final GSLValue[] toArray()
    {
        var array = new GSLValue[(bytes.length / 8) + (bytes.length % 8 == 0 ? 0 : 1)];
        for(var i = 0; i < array.length; i++)
        {
            long value = 0;
            for(int bi = i * 8, idx = 0; bi < bytes.length; bi++, idx += 8)
                value |= bytes[bi] << idx;
            array[i] = new GSLInteger(value);
        }
        return array;
    }

    @Override
    public final List<GSLValue> toList() { return List.of(toArray()); }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return Utils.arrayToMap(toArray()); }
    
    @Override
    public final GSLRawBytes cast() { return this; }
    
    @Override
    public final boolean isMutable() { return true; }

    @Override public final GSLValue operatorEquals(GSLValue value) { return this == value || equals(value) ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return this != value && !equals(value) ? TRUE : FALSE; }
    @Override public final GSLValue operatorGreater(GSLValue value) { throw new UnsupportedOperatorException(this, ">"); }
    @Override public final GSLValue operatorSmaller(GSLValue value) { throw new UnsupportedOperatorException(this, "<"); }
    @Override public final GSLValue operatorGreaterEquals(GSLValue value) { throw new UnsupportedOperatorException(this, ">="); }
    @Override public final GSLValue operatorSmallerEquals(GSLValue value) { throw new UnsupportedOperatorException(this, "<="); }
    @Override public final GSLValue operatorNegate() { return FALSE; }
    @Override public final int      operatorLength() { return bytes.length; }

    @Override public final GSLValue operatorPlus(GSLValue value) { throw new UnsupportedOperatorException(this, "+"); }
    @Override public final GSLValue operatorMinus(GSLValue value) { throw new UnsupportedOperatorException(this, "-"); }
    @Override public final GSLValue operatorMultiply(GSLValue value) { throw new UnsupportedOperatorException(this, "*"); }
    @Override public final GSLValue operatorDivide(GSLValue value) { throw new UnsupportedOperatorException(this, "/"); }
    @Override public final GSLValue operatorRemainder(GSLValue value) { throw new UnsupportedOperatorException(this, "%"); }
    @Override public final GSLValue operatorIncrease() { throw new UnsupportedOperatorException(this, "++"); }
    @Override public final GSLValue operatorDecrease() { throw new UnsupportedOperatorException(this, "--"); }
    @Override public final GSLValue operatorNegative() { throw new UnsupportedOperatorException(this, "-()"); }

    @Override public final GSLValue operatorBitwiseShiftLeft(GSLValue value) { throw new UnsupportedOperatorException(this, ">>"); }
    @Override public final GSLValue operatorBitwiseShiftRight(GSLValue value) { throw new UnsupportedOperatorException(this, "<<"); }
    @Override public final GSLValue operatorBitwiseAnd(GSLValue value) { throw new UnsupportedOperatorException(this, "&"); }
    @Override public final GSLValue operatorBitwiseOr(GSLValue value) { throw new UnsupportedOperatorException(this, "|"); }
    @Override public final GSLValue operatorBitwiseXor(GSLValue value) { throw new UnsupportedOperatorException(this, "^"); }
    @Override public final GSLValue operatorBitwiseNot() { throw new UnsupportedOperatorException(this, "~"); }

    @Override public final GSLValue operatorGet(GSLValue index)
    {
        return new GSLInteger(bytes[index.intValue()]);
    }
    @Override public final GSLValue operatorGet(int index)
    {
        return new GSLInteger(bytes[index]);
    }
    @Override public final void     operatorSet(GSLValue index, GSLValue value)
    {
        bytes[index.intValue()] = (byte) value.intValue();
    }
    @Override public final void     operatorSet(int index, GSLValue value)
    {
        bytes[index] = (byte) value.intValue();
    }

    @Override
    public GSLValue operatorGetProperty(String name)
    {
        switch(name)
        {
            default: return NULL;
            case "setInt8": case "setByte": return S_BYTE;
            case "setInt16": return S_SHORT;
            case "setInt32": return S_INT;
            case "setInt64": return S_LONG;
            case "setFloat32": return S_FLOAT;
            case "setFloat64": return S_DOUBLE;
        }
    }
    @Override public final void operatorSetProperty(String name, GSLValue value) { throw new UnsupportedOperatorException(this, ".="); }

    @Override
    public final GSLValue operatorCall(GSLValue self, GSLValue[] args) { throw new UnsupportedOperatorException(this, "()"); }
    
    @Override
    public final GSLValue operatorNew(GSLValue[] args) { throw new UnsupportedOperatorException(this, "new"); }

    @Override
    public final GSLValue operatorIterator()
    {
        return Def.<GSLValue>iterator(new Iterator<>()
        {
            private int it;
            @Override public final boolean hasNext() { return it < bytes.length; }
            @Override public final GSLValue next() { return new GSLInteger(bytes[it++]); }
        });
    }
    @Override public final boolean hasNext() { throw new UnsupportedOperatorException(this, "hasNext"); }
    @Override public final GSLValue next() { throw new UnsupportedOperatorException(this, "next"); }

    @Override public final boolean equals(Object o)
    {
        return o instanceof GSLRawBytes &&
                Arrays.equals(bytes, ((GSLRawBytes) o).bytes);
    }
    @Override public final int hashCode() { return Arrays.hashCode(bytes); }

    @Override public final GSLValue operatorReferenceGet() { throw new NotPointerException(this); }
    @Override public final void     operatorReferenceSet(GSLValue value) { throw new NotPointerException(this); }
    
    
    
    /* SET METHODS */
    public final void setByte(int offset, int value) { bytes[offset] = (byte) value; }
    public final void setShort(int offset, int value)
    {
        bytes[offset    ] = (byte) ((value >>> 8) & 0xff);
        bytes[offset + 1] = (byte) ((value)       & 0xff);
    }
    public final void setInt(int offset, int value)
    {
        bytes[offset    ] = (byte) ((value >>> 24) & 0xff);
        bytes[offset + 1] = (byte) ((value >>> 16) & 0xff);
        bytes[offset + 2] = (byte) ((value >>>  8) & 0xff);
        bytes[offset + 3] = (byte) ((value)        & 0xff);
    }
    public final void setLong(int offset, long value)
    {
        bytes[offset    ] = (byte) ((value >>> 56) & 0xff);
        bytes[offset + 1] = (byte) ((value >>> 48) & 0xff);
        bytes[offset + 2] = (byte) ((value >>> 40) & 0xff);
        bytes[offset + 3] = (byte) ((value >>> 32) & 0xff);
        bytes[offset + 4] = (byte) ((value >>> 24) & 0xff);
        bytes[offset + 5] = (byte) ((value >>> 16) & 0xff);
        bytes[offset + 6] = (byte) ((value >>>  8) & 0xff);
        bytes[offset + 7] = (byte) ((value)        & 0xff);
    }
    public final void setFloat(int offset, float value) { setInt(offset, Float.floatToIntBits(value)); }
    public final void setDouble(int offset, double value) { setLong(offset, Double.doubleToLongBits(value)); }
    
    public final void setShortBE(int offset, int value)
    {
        bytes[offset + 1] = (byte) ((value >>> 8) & 0xff);
        bytes[offset    ] = (byte) ((value)       & 0xff);
    }
    public final void setIntBE(int offset, int value)
    {
        bytes[offset + 3] = (byte) ((value >>> 24) & 0xff);
        bytes[offset + 2] = (byte) ((value >>> 16) & 0xff);
        bytes[offset + 1] = (byte) ((value >>>  8) & 0xff);
        bytes[offset    ] = (byte) ((value)        & 0xff);
    }
    public final void setLongBE(int offset, long value)
    {
        bytes[offset + 7] = (byte) ((value >>> 56) & 0xff);
        bytes[offset + 6] = (byte) ((value >>> 48) & 0xff);
        bytes[offset + 5] = (byte) ((value >>> 40) & 0xff);
        bytes[offset + 4] = (byte) ((value >>> 32) & 0xff);
        bytes[offset + 3] = (byte) ((value >>> 24) & 0xff);
        bytes[offset + 2] = (byte) ((value >>> 16) & 0xff);
        bytes[offset + 1] = (byte) ((value >>>  8) & 0xff);
        bytes[offset    ] = (byte) ((value)        & 0xff);
    }
    public final void setFloatBE(int offset, float value) { setIntBE(offset, Float.floatToIntBits(value)); }
    public final void setDoubleBE(int offset, double value) { setLongBE(offset, Double.doubleToLongBits(value)); }
    
    
    /* GET METHODS */
    public final int getByte(int offset) { return bytes[offset]; }
    public final int getShort(int offset)
    {
        return ((bytes[offset    ] & 0xff) << 8) |
               ((bytes[offset + 1] & 0xff));
    }
    public final int getInt(int offset)
    {
        return ((bytes[offset    ] & 0xff) << 24) |
               ((bytes[offset + 1] & 0xff) << 16) |
               ((bytes[offset + 2] & 0xff) <<  8) |
               ((bytes[offset + 3] & 0xff));
    }
    public final long getLong(int offset)
    {
        return ((bytes[offset    ] & 0xffL) << 56L) |
               ((bytes[offset + 1] & 0xffL) << 48L) |
               ((bytes[offset + 2] & 0xffL) << 40L) |
               ((bytes[offset + 3] & 0xffL) << 32L) |
               ((bytes[offset + 4] & 0xffL) << 24L) |
               ((bytes[offset + 5] & 0xffL) << 16L) |
               ((bytes[offset + 6] & 0xffL) <<  8L) |
               ((bytes[offset + 7] & 0xffL));
    }
    public final float getFloat(int offset) { return Float.intBitsToFloat(getInt(offset)); }
    public final double getDouble(int offset) { return Double.longBitsToDouble(getLong(offset)); }
    
    public final int getShortBE(int offset)
    {
        return ((bytes[offset + 1] & 0xff) << 8) |
               ((bytes[offset    ] & 0xff));
    }
    public final int getIntBE(int offset)
    {
        return ((bytes[offset + 3] & 0xff) << 24) |
               ((bytes[offset + 2] & 0xff) << 16) |
               ((bytes[offset + 1] & 0xff) <<  8) |
               ((bytes[offset    ] & 0xff));
    }
    public final long getLongBE(int offset)
    {
        return ((bytes[offset + 7] & 0xffL) << 56L) |
               ((bytes[offset + 6] & 0xffL) << 48L) |
               ((bytes[offset + 5] & 0xffL) << 40L) |
               ((bytes[offset + 4] & 0xffL) << 32L) |
               ((bytes[offset + 3] & 0xffL) << 24L) |
               ((bytes[offset + 2] & 0xffL) << 16L) |
               ((bytes[offset + 1] & 0xffL) <<  8L) |
               ((bytes[offset    ] & 0xffL));
    }
    public final float getFloatBE(int offset) { return Float.intBitsToFloat(getIntBE(offset)); }
    public final double getDoubleBE(int offset) { return Double.longBitsToDouble(getLongBE(offset)); }
    
    
    
    
    private static final GSLValue S_BYTE = Def.<GSLRawBytes>voidMethod((self, args) -> self.setByte(args[0].intValue(), args[1].intValue()));
    private static final GSLValue S_SHORT = Def.<GSLRawBytes>voidMethod((self, args) -> self.setShort(args[0].intValue(), args[1].intValue()));
    private static final GSLValue S_INT = Def.<GSLRawBytes>voidMethod((self, args) -> self.setInt(args[0].intValue(), args[1].intValue()));
    private static final GSLValue S_LONG = Def.<GSLRawBytes>voidMethod((self, args) -> self.setLong(args[0].intValue(), args[1].longValue()));
    private static final GSLValue S_FLOAT = Def.<GSLRawBytes>voidMethod((self, args) -> self.setFloat(args[0].intValue(), args[1].floatValue()));
    private static final GSLValue S_DOUBLE = Def.<GSLRawBytes>voidMethod((self, args) -> self.setDouble(args[0].intValue(), args[1].intValue()));
    
    private static final GSLValue S_SHORT_BE = Def.<GSLRawBytes>voidMethod((self, args) -> self.setShortBE(args[0].intValue(), args[1].intValue()));
    private static final GSLValue S_INT_BE = Def.<GSLRawBytes>voidMethod((self, args) -> self.setIntBE(args[0].intValue(), args[1].intValue()));
    private static final GSLValue S_LONG_BE = Def.<GSLRawBytes>voidMethod((self, args) -> self.setLongBE(args[0].intValue(), args[1].longValue()));
    private static final GSLValue S_FLOAT_BE = Def.<GSLRawBytes>voidMethod((self, args) -> self.setFloatBE(args[0].intValue(), args[1].floatValue()));
    private static final GSLValue S_DOUBLE_BE = Def.<GSLRawBytes>voidMethod((self, args) -> self.setDoubleBE(args[0].intValue(), args[1].intValue()));
    
    private static final GSLValue G_BYTE = Def.<GSLRawBytes>method((self, args) -> new GSLInteger(self.getByte(args[0].intValue())));
    private static final GSLValue G_SHORT = Def.<GSLRawBytes>method((self, args) -> new GSLInteger(self.getShort(args[0].intValue())));
    private static final GSLValue G_INT = Def.<GSLRawBytes>method((self, args) -> new GSLInteger(self.getInt(args[0].intValue())));
    private static final GSLValue G_LONG = Def.<GSLRawBytes>method((self, args) -> new GSLInteger(self.getLong(args[0].intValue())));
    private static final GSLValue G_FLOAT = Def.<GSLRawBytes>method((self, args) -> new GSLFloat(self.getFloat(args[0].intValue())));
    private static final GSLValue G_DOUBLE = Def.<GSLRawBytes>method((self, args) -> new GSLFloat(self.getDouble(args[0].intValue())));
    
    private static final GSLValue G_SHORT_BE = Def.<GSLRawBytes>method((self, args) -> new GSLInteger(self.getShortBE(args[0].intValue())));
    private static final GSLValue G_INT_BE = Def.<GSLRawBytes>method((self, args) -> new GSLInteger(self.getIntBE(args[0].intValue())));
    private static final GSLValue G_LONG_BE = Def.<GSLRawBytes>method((self, args) -> new GSLInteger(self.getLongBE(args[0].intValue())));
    private static final GSLValue G_FLOAT_BE = Def.<GSLRawBytes>method((self, args) -> new GSLFloat(self.getFloatBE(args[0].intValue())));
    private static final GSLValue G_DOUBLE_BE = Def.<GSLRawBytes>method((self, args) -> new GSLFloat(self.getDoubleBE(args[0].intValue())));
}
