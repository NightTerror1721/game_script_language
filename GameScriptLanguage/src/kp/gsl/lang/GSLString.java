/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kp.gsl.exception.GSLRuntimeException;
import kp.gsl.exception.UnsupportedOperatorException;
import kp.gsl.lib.Def;

/**
 *
 * @author Asus
 */
public final class GSLString extends GSLImmutableValue
{
    final String string;
    
    public GSLString(String string)
    {
        if(string == null)
            throw new NullPointerException();
        this.string  = string;
    }
    
    @Override
    public final GSLDataType getGSLDataType() { return GSLDataType.STRING; }

    @Override
    public final int intValue() { return string.hashCode(); }

    @Override
    public final long longValue() { return string.hashCode(); }

    @Override
    public final float floatValue() { return string.hashCode(); }

    @Override
    public final double doubleValue() { return string.hashCode(); }
    
    @Override
    public final char charValue() { return (char) string.hashCode(); }

    @Override
    public final boolean boolValue() { return !string.isEmpty(); }

    @Override
    public final String toString() { return string; }

    @Override
    public final GSLValue[] toArray() { return Utils.arrayOf(this); }

    @Override
    public final List<GSLValue> toList() { return Utils.listOf(this); }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return Utils.mapOf(this); }
    
    @Override
    public final Stream<GSLValue> stream() { return Stream.of(this); }
    
    @Override
    public final GSLString cast() { return this; }
    
    
    @Override public final GSLInteger    operatorCastInteger() { return new GSLInteger(Long.decode(string)); }
    @Override public final GSLFloat      operatorCastFloat() { return new GSLFloat(Double.parseDouble(string)); }
    @Override public final GSLBoolean    operatorCastBoolean() { return string.isEmpty() ? TRUE : FALSE; }
    @Override public final GSLString     operatorCastString() { return this; }
    @Override public final GSLConstTuple operatorCastConstTuple() { return new GSLConstTuple(new GSLImmutableValue[] { this }); }
    @Override public final GSLConstMap   operatorCastConstMap() { return new GSLConstMap(Utils.constMapOf(this)); }
    @Override public final GSLFunction   operatorCastFunction() { return Utils.autoGetter(this); }
    @Override public final GSLList       operatorCastList() { return new GSLList(Utils.listOf(this)); }
    @Override public final GSLTuple      operatorCastTuple() { return new GSLTuple(new GSLValue[] { this }); }
    @Override public final GSLMap        operatorCastMap() { return new GSLMap(Utils.mapOf(this)); }
    @Override public final GSLStruct     operatorCastStruct() { return Utils.structOf(this); }
    @Override public final GSLBlueprint  operatorCastBlueprint() { return Utils.blueprintOf(this); }
    @Override public final GSLObject     operatorCastObject() { return Utils.objectOf(this); }
    @Override public final GSLIterator   operatorCastIterator() { return Utils.oneIter(this); }
    @Override public final GSLRawBytes   operatorCastRawBytes() { return new GSLRawBytes(string.getBytes()); }
    

    @Override public final GSLValue operatorEquals(GSLValue value) { return string.equals(value.toString()) ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return string.equals(value.toString()) ? FALSE : TRUE; }
    @Override public final GSLValue operatorGreater(GSLValue value) { return string.compareTo(value.toString()) > 0 ? TRUE : FALSE; }
    @Override public final GSLValue operatorSmaller(GSLValue value) { return string.compareTo(value.toString()) < 0 ? TRUE : FALSE; }
    @Override public final GSLValue operatorGreaterEquals(GSLValue value) { return string.compareTo(value.toString()) >= 0 ? TRUE : FALSE; }
    @Override public final GSLValue operatorSmallerEquals(GSLValue value) { return string.compareTo(value.toString()) <= 0 ? TRUE : FALSE; }
    @Override public final GSLValue operatorNegate() { return string.isEmpty() ? TRUE : FALSE; }
    @Override public final int      operatorLength() { return string.length(); }

    @Override public final GSLValue operatorPlus(GSLValue value) { throw new UnsupportedOperatorException(this, "+"); }
    @Override public final GSLValue operatorMinus(GSLValue value) { throw new UnsupportedOperatorException(this, "-"); }
    @Override public final GSLValue operatorMultiply(GSLValue value) { return new GSLString(string.repeat(value.intValue())); }
    @Override public final GSLValue operatorDivide(GSLValue value) { throw new UnsupportedOperatorException(this, "/"); }
    @Override public final GSLValue operatorRemainder(GSLValue value) { return new GSLString(String.format(string, value.stream().map(GSLValue::toJavaValue).toArray())); }
    @Override public final GSLValue operatorIncrease() { throw new UnsupportedOperatorException(this, "++"); }
    @Override public final GSLValue operatorDecrease() { throw new UnsupportedOperatorException(this, "--"); }
    @Override public final GSLValue operatorNegative() { throw new UnsupportedOperatorException(this, "-()"); }

    @Override public final GSLValue operatorBitwiseShiftLeft(GSLValue value) { throw new UnsupportedOperatorException(this, "<<"); }
    @Override public final GSLValue operatorBitwiseShiftRight(GSLValue value) { throw new UnsupportedOperatorException(this, ">>"); }
    @Override public final GSLValue operatorBitwiseAnd(GSLValue value) { throw new UnsupportedOperatorException(this, "&"); }
    @Override public final GSLValue operatorBitwiseOr(GSLValue value) { throw new UnsupportedOperatorException(this, "|"); }
    @Override public final GSLValue operatorBitwiseXor(GSLValue value) { throw new UnsupportedOperatorException(this, "^"); }
    @Override public final GSLValue operatorBitwiseNot() { throw new UnsupportedOperatorException(this, "~"); }

    @Override public final GSLValue operatorGet(GSLValue index)
    {
        return new GSLInteger(string.charAt(index.intValue()));
    }
    @Override public final GSLValue operatorGet(int index)
    {
        return new GSLInteger(string.charAt(index));
    }

    @Override
    public GSLValue operatorGetProperty(String name)
    {
        switch(name)
        {
            default: return NULL;
            case "compareTo": return COMPARE_TO;
            case "contains": return CONTAINS;
            case "endsWith": return ENDS;
            case "startsWith": return STARTS;
            case "equalsIgnoreCase": return EQUALS_IGNORE_CASE;
            case "getBytes": return BYTES;
            case "isBlank": return BLANK;
            case "isEmpty": return EMPTY;
            case "indexOf": return INDEX_OF;
            case "lastIndexOf": return LAST_INDEX_OF;
            case "matches": return MATCHES;
            case "repeat": return REPEAT;
            case "replace": return REPLACE;
            case "split": return SPLIT;
            case "strip": return STRIP;
            case "stripLeading": return STRIP_LEADING;
            case "stripTrailing": return STRIP_TRAILING;
            case "trim": return TRIM;
            case "substring": return SUB;
            case "toUpperCase": return UPPER;
            case "toLowerCase": return LOWER;
        }
    }

    @Override
    public final GSLValue operatorCall(GSLValue self, GSLVarargs args) { throw new UnsupportedOperatorException(this, "()"); }
    
    @Override
    public final GSLValue operatorNew(GSLVarargs args) { throw new UnsupportedOperatorException(this, "new"); }

    @Override
    public final GSLValue operatorIterator() { return Utils.oneIter(this); }
    @Override public final boolean hasNext() { throw new UnsupportedOperatorException(this, "hasNext"); }
    @Override public final GSLValue next() { throw new UnsupportedOperatorException(this, "next"); }

    @Override public final boolean equals(Object o)
    {
        return o instanceof GSLString &&
                string.equals(((GSLString) o).string);
    }
    @Override public final int hashCode() { return string.hashCode(); }
    
    
    private static final GSLValue COMPARE_TO = Def.<GSLString>intMethod((self, args) -> {
        if(args.boolValue1())
            return self.string.compareToIgnoreCase(args.arg0().toString());
        else return self.string.compareTo(args.arg0().toString());
    });
    
    private static final GSLValue CONTAINS = Def.<GSLString>boolMethod((self, args) -> {
        var arg = args.arg0();
        return arg.getGSLDataType().isNumber()
                ? self.string.contains(Character.toString(arg.charValue()))
                : self.string.contains(arg.toString());
    });
    
    private static final GSLValue ENDS = Def.<GSLString>boolMethod((self, args) -> {
        var arg = args.arg0();
        return arg.getGSLDataType().isNumber()
                ? self.string.endsWith(Character.toString(arg.charValue()))
                : self.string.endsWith(arg.toString());
    });
    
    private static final GSLValue STARTS = Def.<GSLString>boolMethod((self, args) -> {
        var arg = args.arg0();
        return arg.getGSLDataType().isNumber()
                ? self.string.startsWith(Character.toString(arg.charValue()))
                : self.string.startsWith(arg.toString());
    });
    
    private static final GSLValue EQUALS_IGNORE_CASE = Def.<GSLString>boolMethod((self, args) -> {
        return self.string.equalsIgnoreCase(args.arg0().toString());
    });
    
    private static final GSLValue BYTES = Def.<GSLString>method((self, args) -> {
        var arg = args.arg0();
        try { return arg.boolValue()
                ? new GSLRawBytes(self.string.getBytes(arg.toString()))
                : new GSLRawBytes(self.string.getBytes());
        } catch(UnsupportedEncodingException ex) { throw new GSLRuntimeException(ex); }
    });
    
    private static final GSLValue BLANK = Def.<GSLString>boolMethod((self, args) -> self.string.isBlank());
    private static final GSLValue EMPTY = Def.<GSLString>boolMethod((self, args) -> self.string.isEmpty());
    
    private static final GSLValue INDEX_OF = Def.<GSLString>intMethod((self, args) -> {
        var arg = args.arg0();
        return arg.getGSLDataType().isNumber()
                ? self.string.indexOf(arg.charValue())
                : self.string.indexOf(arg.toString());
    });
    private static final GSLValue LAST_INDEX_OF = Def.<GSLString>intMethod((self, args) -> {
        var arg = args.arg0();
        return arg.getGSLDataType().isNumber()
                ? self.string.lastIndexOf(arg.charValue())
                : self.string.lastIndexOf(arg.toString());
    });
    
    private static final GSLValue MATCHES = Def.<GSLString>boolMethod((self, args) -> self.string.matches(args.arg0().toString()));
    
    private static final GSLValue REPEAT = Def.<GSLString>stringMethod((self, args) -> self.string.repeat(args.arg0().intValue()));
    
    private static final GSLValue REPLACE = Def.<GSLString>stringMethod((self, args) -> {
        var old_regex = args.arg0();
        var new_replacement = args.arg1();
        if(old_regex.isNumber() && new_replacement.isNumber())
            return self.string.replace(old_regex.charValue(), new_replacement.charValue());
        return args.boolValue(2)
                ? self.string.replaceAll(old_regex.toString(), new_replacement.toString())
                : self.string.replace(old_regex.toString(), new_replacement.toString());
    });
    
    private static final GSLValue SPLIT = Def.<GSLString>method((self, args) -> {
        if(args.isArg1Null())
            return valueOf(self.string.split(args.arg0().toString()));
        return valueOf(self.string.split(args.arg0().toString(), args.arg1().intValue()));
    });
    
    private static final GSLValue STRIP = Def.<GSLString>stringMethod((self, args) -> self.string.strip());
    private static final GSLValue STRIP_LEADING = Def.<GSLString>stringMethod((self, args) -> self.string.stripLeading());
    private static final GSLValue STRIP_TRAILING = Def.<GSLString>stringMethod((self, args) -> self.string.stripTrailing());
    private static final GSLValue TRIM = Def.<GSLString>stringMethod((self, args) -> self.string.trim());
    
    private static final GSLValue SUB = Def.<GSLString>stringMethod((self, args) -> {
        var from = args.arg0();
        var to = args.arg1();
        return to.isNull()
                ? self.string.substring(from.intValue())
                : self.string.substring(from.intValue(), to.intValue());
    });
    
    private static final GSLValue UPPER = Def.<GSLString>stringMethod((self, args) -> self.string.toUpperCase());
    private static final GSLValue LOWER = Def.<GSLString>stringMethod((self, args) -> self.string.toUpperCase());
}
