/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import static kp.gsl.lang.GSLValue.NULL;

/**
 *
 * @author Asus
 */
public abstract class GSLVarargs
{
    public abstract int numberOfArguments();
    public abstract GSLValue arg0();
    public abstract GSLValue arg1();
    public abstract GSLValue arg(int index);
    
    public final boolean isArg0Null() { return arg0() == NULL; }
    public final boolean isArg1Null() { return arg1() == NULL; }
    public final boolean isArgNull(int index) { return arg(index) == NULL; }
    
    public final boolean boolValue0() { return arg0().boolValue(); }
    public final boolean boolValue1() { return arg1().boolValue(); }
    public final boolean boolValue(int index) { return arg(index).boolValue(); }
    
    
    private static final class Empty extends GSLVarargs
    {
        @Override public final int numberOfArguments() { return 0; }
        @Override public final GSLValue arg0() { return NULL; }
        @Override public final GSLValue arg1() { return NULL; }
        @Override public final GSLValue arg(int index) { return NULL; }
    }
    public static final GSLVarargs NO_ARGS = new Empty();
    
    static final class Pair extends GSLVarargs
    {
        private final GSLValue v0, v1;
        
        public Pair(GSLValue v0, GSLValue v1)
        {
            this.v0 = v0 == null ? NULL : v0;
            this.v1 = v1 == null ? NULL : v1;
        }
        
        @Override public final int numberOfArguments() { return 2; }
        @Override public final GSLValue arg0() { return v0; }
        @Override public final GSLValue arg1() { return v1; }
        @Override public final GSLValue arg(int index)
        {
            switch(index)
            {
                case 0: return v0;
                case 1: return v1;
                default: return NULL;
            }
        }
    }
    
    static final class LinkedPair extends GSLVarargs
    {
        private final GSLValue value;
        private final GSLVarargs rest;
        
        public LinkedPair(GSLValue value, GSLVarargs rest)
        {
            this.value = value == null ? NULL : value;
            this.rest = rest == null ? NO_ARGS : rest;
        }
        
        @Override public final int numberOfArguments() { return rest.numberOfArguments() + 1; }
        @Override public final GSLValue arg0() { return value; }
        @Override public final GSLValue arg1() { return rest.arg0(); }
        @Override public final GSLValue arg(int index) { return index == 0 ? value : rest.arg(index - 1); }
    }
    
    static final class Joined extends GSLVarargs
    {
        private final GSLVarargs v0, v1;
        
        public Joined(GSLVarargs v0, GSLVarargs v1)
        {
            this.v0 = v0 == null ? NO_ARGS : v0;
            this.v1 = v1 == null ? NO_ARGS : v1;
        }
        
        @Override public final int numberOfArguments() { return v0.numberOfArguments() + v1.numberOfArguments(); }
        @Override public final GSLValue arg0() { return v0.numberOfArguments() > 0 ? v0.arg0() : v1.arg0(); }
        @Override public final GSLValue arg1()
        {
            switch(v0.numberOfArguments())
            {
                case 0: return v1.arg1();
                case 1: return v1.arg0();
                default: return v0.arg1();
            }
        }
        @Override public final GSLValue arg(int index)
        {
            return index < v0.numberOfArguments() ? v0.arg(index) : v1.arg(index);
        }
    }
    
    static final class Array extends GSLVarargs
    {
        private final GSLValue[] values;
        
        public Array(GSLValue... values)
        {
            this.values = values == null ? new GSLValue[] {} : values;
        }
        
        @Override public final int numberOfArguments() { return values.length; }
        @Override public final GSLValue arg0() { return values.length > 0 ? values[0] : NULL; }
        @Override public final GSLValue arg1() { return values.length > 1 ? values[1] : NULL; }
        @Override public final GSLValue arg(int index) { return values.length > index ? values[index] : NULL; }
    }
    
    static final class Sub extends GSLVarargs
    {
        private final GSLVarargs args;
        private final int from, len;
        
        public Sub(GSLVarargs args, int from, int to)
        {
            if(args == null)
            {
                this.args = NO_ARGS;
                this.from = 0;
            }
            else
            {
                this.args = args;
                this.from = from;
            }
            this.len = Math.max(0, to - from);
        }
        
        @Override public final int numberOfArguments() { return len; }
        @Override public final GSLValue arg0() { return len > 0 ? args.arg(from) : NULL; }
        @Override public final GSLValue arg1() { return len > 1 ? args.arg(from + 1) : NULL; }
        @Override public final GSLValue arg(int index) { return len < index ? args.arg(from + index) : NULL; }
    }
    
    
    public static final GSLVarargs varargsOf() { return NO_ARGS; }
    public static final GSLVarargs varargsOf(GSLValue value) { return value; }
    public static final GSLVarargs varargsOf(GSLValue v0, GSLValue v1) { return new Pair(v0, v1); }
    public static final GSLVarargs varargsOf(GSLValue v, GSLVarargs rest) { return new LinkedPair(v, rest); }
    public static final GSLVarargs varargsOf(GSLVarargs v0, GSLVarargs v1) { return new Joined(v0, v1); }
    public static final GSLVarargs varargsOf(GSLValue v0, GSLValue v1, GSLValue v2) { return new Array(new GSLValue[] { v0, v1, v2 }); }
    public static final GSLVarargs varargsOf(GSLValue v0, GSLValue v1, GSLValue v2, GSLValue v3) { return new Array(new GSLValue[] { v0, v1, v2, v3 }); }
    public static final GSLVarargs varargsOf(GSLValue v0, GSLValue v1, GSLValue v2, GSLValue v3, GSLValue v4) { return new Array(new GSLValue[] { v0, v1, v2, v3, v4 }); }
    public static final GSLVarargs varargsOf(GSLValue... values) { return new Array(values); }
    public static final GSLVarargs subVarargs(GSLVarargs args, int from, int to) { return new Sub(args, from, to); }
    public static final GSLVarargs subVarargs(GSLVarargs args, int from) { return new Sub(args, from, 0); }
    
    public static final Iterator<GSLValue> varargsAsIterator(GSLVarargs varargs)
    {
        return new Iterator<>()
        {
            private final int len = varargs.numberOfArguments();
            private int it;
            @Override public final boolean hasNext() { return it < len; }
            @Override public final GSLValue next() { return varargs.arg(it++); }
        };
    }
    
    public static final Iterable<GSLValue> varargsAsIterable(GSLVarargs varargs) { return () -> varargsAsIterator(varargs); }
    
    public static final Stream<GSLValue> varargsAsStream(GSLVarargs varargs)
    {
        return StreamSupport.stream(Spliterators.spliterator(varargsAsIterator(varargs),
                varargs.numberOfArguments(), Spliterator.ORDERED | Spliterator.SIZED), false);
    }
    
    public static final GSLValue[] varargsAsArray(GSLVarargs varargs) { return varargsAsStream(varargs).toArray(GSLValue[]::new); }
    public static final GSLValue[] varargsAsArray(GSLVarargs varargs, int from, int to)
    {
        return varargsAsStream(varargs).skip(from).limit(to - from).toArray(GSLValue[]::new);
    }
    public static final GSLValue[] varargsAsArray(GSLVarargs varargs, int from)
    {
        return varargsAsStream(varargs).skip(from).toArray(GSLValue[]::new);
    }
    public static final List<GSLValue> varargsAsList(GSLVarargs varargs) { return varargsAsStream(varargs).collect(Collectors.toList()); }
    public static final List<GSLValue> varargsAsList(GSLVarargs varargs, int from, int to)
    {
        return varargsAsStream(varargs).skip(from).limit(to - from).collect(Collectors.toList());
    }
    public static final List<GSLValue> varargsAsList(GSLVarargs varargs, int from)
    {
        return varargsAsStream(varargs).skip(from).collect(Collectors.toList());
    }
}
