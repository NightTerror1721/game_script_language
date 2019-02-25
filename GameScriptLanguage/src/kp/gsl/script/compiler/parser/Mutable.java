/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.compiler.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.StringJoiner;
import kp.gsl.exception.CompilerError;
import kp.gsl.lang.GSLImmutableValue;

/**
 *
 * @author Asus
 */
public final class Mutable extends Statement
{
    private final MutableEntry[] entries;
    private final MutableType type;
    private final boolean isLiteral;
    
    private Mutable(MutableEntry[] entries, MutableType type, boolean isLiteral)
    {
        this.entries = Objects.requireNonNull(entries);
        this.type = type;
        this.isLiteral = isLiteral;
    }
    
    public final MutableType getMutableType() { return type; }
    
    public final int getEntryCount() { return entries.length; }
    public final MutableEntry getEntry(int index) { return entries[index]; }
    
    @Override
    public final CodeFragmentType getCodeFragmentType() { return CodeFragmentType.MUTABLE; }
    
    public final boolean isMutableLiteral() { return isLiteral; }
    
    public final boolean isListBased() { return type == MutableType.LIST || type == MutableType.TUPLE; }
    public final boolean isMapBased() { return type == MutableType.MAP; }
    public final boolean isObjectBased() { return type == MutableType.OBJECT || type == MutableType.STRUCT || type == MutableType.BLUEPRINT; }
    
    public final Literal generateLiteral()
    {
        if(!isLiteral)
            throw new IllegalStateException();
        if(type == MutableType.TUPLE)
        {
            GSLImmutableValue[] array = new GSLImmutableValue[entries.length];
            for(int i=0;i<array.length;i++)
                array[i] = ((Literal) entries[i].value).getGSLValue();
            return Literal.valueOf(array);
        }
        else
        {
            HashMap<GSLImmutableValue, GSLImmutableValue> map = new HashMap<>();
            for(MutableEntry e : entries)
                map.put(((Literal) e.key).getGSLValue(), ((Literal) e.value).getGSLValue());
            return Literal.valueOf(map);
        }
    }
    
    
    
    private static Mutable listBased(CodeFragmentList frags, boolean isTuple) throws CompilerError
    {
        var mtype = isTuple ? MutableType.TUPLE : MutableType.LIST;
        CodeFragmentList[] parts = frags.split(Stopchar.COMMA);
        if(parts.length == 1 && parts[0].isEmpty())
            return new Mutable(new MutableEntry[0], mtype, isTuple);
        
        //boolean literal = true;
        var literal = new Flag(true);
        try
        {
            MutableEntry[] entries = Arrays.stream(parts).filter(p -> !p.isEmpty()).map(p -> {
                try {
                    var e = new MutableEntry(p);
                    if(literal.getState() && !e.isLiteral())
                        literal.setState(false);
                    return e;
                }
                catch(CompilerError ex) { throw new RuntimeException(ex); }
            }).toArray(MutableEntry[]::new);

            return new Mutable(entries, MutableType.LIST, literal.getState() && isTuple);
        }
        catch(RuntimeException ex) { throw (CompilerError) ex.getCause(); }
    }
    public static final Mutable list(CodeFragmentList frags) throws CompilerError { return listBased(frags, false); }
    public static final Mutable tuple(CodeFragmentList frags) throws CompilerError { return listBased(frags, true); }
    
    private static Mutable listBased(CodeFragmentList[] frags, boolean isTuple) throws CompilerError
    {
        boolean literal = true;
        MutableEntry[] entries = new MutableEntry[frags.length];
        for(int i=0;i<frags.length;i++)
        {
            entries[i] = new MutableEntry(frags[i]);
            if(literal && !entries[i].isLiteral())
                literal = false;
        }
        
        return new Mutable(entries, MutableType.LIST, literal && isTuple);
    }
    public static final Mutable list(CodeFragmentList[] frags) throws CompilerError { return listBased(frags, false); }
    public static final Mutable tuple(CodeFragmentList[] frags) throws CompilerError { return listBased(frags, true); }
    
    
    
    private static Mutable objectBased(CodeFragmentList frags, boolean constKeys, boolean constValues) throws CompilerError
    {
        var mtype = constKeys ? constValues ? MutableType.BLUEPRINT : MutableType.STRUCT : MutableType.OBJECT;
        CodeFragmentList[] parts = frags.split(Stopchar.COMMA);
        if(parts.length == 1 && parts[0].isEmpty())
            return new Mutable(new MutableEntry[0], mtype, true);
        
        boolean literal = true;
        MutableEntry[] entries = new MutableEntry[parts.length];
        for(int i=0;i<parts.length;i++)
        {
            CodeFragmentList[] part = parts[i].split(Stopchar.TWO_POINTS);
            if(part.length != 2)
                throw new CompilerError("Malformed object literal.");
            entries[i] = new MutableEntry(part[0], part[1], false);
            if(literal && !entries[i].isLiteral())
                literal = false;
        }
        
        return new Mutable(entries, mtype, literal);
    }
    public static final Mutable object(CodeFragmentList frags) throws CompilerError { return objectBased(frags, false, false); }
    public static final Mutable struct(CodeFragmentList frags) throws CompilerError { return objectBased(frags, true, false); }
    public static final Mutable blueprint(CodeFragmentList frags) throws CompilerError { return objectBased(frags, true, true); }
    
    public static final Mutable tryMapOrList(CodeFragmentList frags) throws CompilerError
    {
        CodeFragmentList[] parts = frags.split(Stopchar.COMMA);
        if(parts.length == 0)
            return list(new CodeFragmentList[] {});
        if(parts.length == 1)
        {
            if(parts[0].isEmpty())
                return list(new CodeFragmentList[] {});
            else if(parts[0].length() == 1 && parts[0].get(0) == Stopchar.TWO_POINTS)
                return new Mutable(new MutableEntry[0], MutableType.MAP, true);
            else list(parts);
        }
        
        boolean literal = true;
        MutableEntry[] entries = new MutableEntry[parts.length];
        for(int i=0;i<parts.length;i++)
        {
            CodeFragmentList[] part = parts[i].split(Stopchar.TWO_POINTS);
            if(part.length != 2)
            {
                if(part.length == 1)
                    return list(parts);
                throw new CompilerError("Malformed map literal.");
            }
            entries[i] = new MutableEntry(part[0], part[1], true);
            if(literal && !entries[i].isLiteral())
                literal = false;
        }
        
        return new Mutable(entries, MutableType.MAP, literal);
    }

    @Override
    public final String toString()
    {
        StringJoiner joiner;
        switch(type)
        {
            case LIST: {
                joiner = new StringJoiner(", ", "[", "]");
                Arrays.stream(entries).map(e -> e.getValue().toString()).forEach(joiner::add);
            } break;
            case TUPLE: {
                joiner = new StringJoiner(", ", "(", ")");
                Arrays.stream(entries).map(e -> e.getValue().toString()).forEach(joiner::add);
            } break;
            case MAP: {
                joiner = new StringJoiner(", ", "[", "]");
                Arrays.stream(entries).map(e -> e.getKey() + ": " + e.getValue()).forEach(joiner::add);
            } break;
            default: {
                joiner = new StringJoiner(", ", "{", "}");
                Arrays.stream(entries).map(e -> e.getKey() + ": " + e.getValue()).forEach(joiner::add);
            } break;
        }
        return joiner.toString();
    }
    
    
    
    
    public static final class MutableEntry
    {
        private final Statement key;
        private final Statement value;
        
        private MutableEntry(CodeFragmentList key, CodeFragmentList value, boolean fullKeys) throws CompilerError
        {
            if(key == null)
                this.key = null;
            else
            {
                Statement s = StatementParser.parse(key);
                if(fullKeys)
                    this.key = s;
                else switch(s.getCodeFragmentType())
                {
                    case IDENTIFIER: this.key = s; break;
                    case LITERAL:
                        if(((Literal) s).getLiteralType() == Literal.LiteralType.STRING)
                        {
                            this.key = s;
                            break;
                        }
                    default: throw new CompilerError("Expected valid identifier or string literal in object property name. But found: " + s);
                }
            }
            this.value = StatementParser.parse(value);
        }
        private MutableEntry(CodeFragmentList value) throws CompilerError { this(null, value, true); }
        
        public final Statement getKey() { return key; }
        public final Statement getValue() { return value; }
        
        public final String getObjectBasedKey()
        {
            return key.toString();
        }
        
        public final boolean isLiteral() { return value.isLiteral(); }
    }
    
    public enum MutableType { LIST, TUPLE, MAP, OBJECT, STRUCT, BLUEPRINT; }
    
    private static final class Flag
    {
        private boolean flag;
        
        private Flag(boolean initialState) { this.flag = flag; }
        
        public final void setState(boolean state) { this.flag = state; }
        public final boolean getState() { return flag; }
    }
}
