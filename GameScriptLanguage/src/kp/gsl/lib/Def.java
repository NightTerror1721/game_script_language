/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lib;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;
import kp.gsl.lang.GSLFloat;
import kp.gsl.lang.GSLFunction;
import kp.gsl.lang.GSLInteger;
import kp.gsl.lang.GSLIterator;
import kp.gsl.lang.GSLString;
import kp.gsl.lang.GSLValue;
import kp.gsl.lang.GSLVarargs;

/**
 *
 * @author Asus
 */
public final class Def
{
    private Def() {}
    
    public static final GSLFunction voidFunction(Consumer<GSLVarargs> function) { return new VoidFunction(function); }
    public static final GSLFunction function(java.util.function.Function<GSLVarargs, GSLValue> function) { return new Function(function); }
    public static final GSLFunction boolFunction(BoolFunctionClosure function) { return new BoolFunction(function); }
    public static final GSLFunction intFunction(ToLongFunction<GSLVarargs> function) { return new IntFunction(function); }
    public static final GSLFunction floatFunction(ToDoubleFunction<GSLVarargs> function) { return new FloatFunction(function); }
    public static final GSLFunction stringFunction(java.util.function.Function<GSLVarargs, String> function) { return new StringFunction(function); }
    
    public static final <SV extends GSLValue> GSLFunction voidMethod(BiConsumer<SV, GSLVarargs> method) { return new VoidMethod<>(method); }
    public static final <SV extends GSLValue> GSLFunction method(BiFunction<SV, GSLVarargs, GSLValue> method) { return new Method<>(method); }
    public static final <SV extends GSLValue> GSLFunction boolMethod(BoolMethodClosure<SV> method) { return new BoolMethod<>(method); }
    public static final <SV extends GSLValue> GSLFunction intMethod(ToLongBiFunction<SV, GSLVarargs> method) { return new IntMethod<>(method); }
    public static final <SV extends GSLValue> GSLFunction floatMethod(ToDoubleBiFunction<SV, GSLVarargs> method) { return new FloatMethod<>(method); }
    public static final <SV extends GSLValue> GSLFunction stringMethod(BiFunction<SV, GSLVarargs, String> method) { return new StringMethod<>(method); }
    
    public static final <V extends GSLValue> GSLIterator iterator(Iterator<V> iterator) { return new DefaultIterator(iterator); }
    
    
    private static final class VoidFunction extends GSLFunction
    {
        private final Consumer<GSLVarargs> function;
        
        private VoidFunction(Consumer<GSLVarargs> function)
        {
            this.function = Objects.requireNonNull(function);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            function.accept(args);
            return NULL;
        }
    }
    
    private static final class Function extends GSLFunction
    {
        private final java.util.function.Function<GSLVarargs, GSLValue> function;
        
        private Function(java.util.function.Function<GSLVarargs, GSLValue> function)
        {
            this.function = Objects.requireNonNull(function);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            return function.apply(args);
        }
    }
    
    private static final class BoolFunction extends GSLFunction
    {
        private final BoolFunctionClosure function;
        
        private BoolFunction(BoolFunctionClosure function)
        {
            this.function = Objects.requireNonNull(function);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            return function.applyAsBool(args) ? TRUE : FALSE;
        }
    }
    @FunctionalInterface
    public static interface BoolFunctionClosure { boolean applyAsBool(GSLVarargs args); }
    
    private static final class IntFunction extends GSLFunction
    {
        private final ToLongFunction<GSLVarargs> function;
        
        private IntFunction(ToLongFunction<GSLVarargs> function)
        {
            this.function = Objects.requireNonNull(function);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            return new GSLInteger(function.applyAsLong(args));
        }
    }
    
    private static final class FloatFunction extends GSLFunction
    {
        private final ToDoubleFunction<GSLVarargs> function;
        
        private FloatFunction(ToDoubleFunction<GSLVarargs> function)
        {
            this.function = Objects.requireNonNull(function);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            return new GSLFloat(function.applyAsDouble(args));
        }
    }
    
    private static final class StringFunction extends GSLFunction
    {
        private final java.util.function.Function<GSLVarargs, String> function;
        
        private StringFunction(java.util.function.Function<GSLVarargs, String> function)
        {
            this.function = Objects.requireNonNull(function);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            return new GSLString(function.apply(args));
        }
    }
    
    private static final class VoidMethod<SV extends GSLValue> extends GSLFunction
    {
        private final BiConsumer<SV, GSLVarargs> method;
        
        private VoidMethod(BiConsumer<SV, GSLVarargs> method)
        {
            this.method = Objects.requireNonNull(method);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            method.accept(self.cast(), args);
            return NULL;
        }
    }
    
    private static final class Method<SV extends GSLValue> extends GSLFunction
    {
        private final BiFunction<SV, GSLVarargs, GSLValue> method;
        
        private Method(BiFunction<SV, GSLVarargs, GSLValue> method)
        {
            this.method = Objects.requireNonNull(method);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            return method.apply(self.cast(), args);
        }
    }
    
    private static final class BoolMethod<SV extends GSLValue> extends GSLFunction
    {
        private final BoolMethodClosure<SV> method;
        
        private BoolMethod(BoolMethodClosure<SV> method)
        {
            this.method = Objects.requireNonNull(method);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            return method.applyAsBool(self.cast(), args) ? TRUE : FALSE;
        }
    }
    @FunctionalInterface
    public static interface BoolMethodClosure<SV extends GSLValue> { boolean applyAsBool(SV self, GSLVarargs args); }
    
    private static final class IntMethod<SV extends GSLValue> extends GSLFunction
    {
        private final ToLongBiFunction<SV, GSLVarargs> method;
        
        private IntMethod(ToLongBiFunction<SV, GSLVarargs> method)
        {
            this.method = Objects.requireNonNull(method);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            return new GSLInteger(method.applyAsLong(self.cast(), args));
        }
    }
    
    private static final class FloatMethod<SV extends GSLValue> extends GSLFunction
    {
        private final ToDoubleBiFunction<SV, GSLVarargs> method;
        
        private FloatMethod(ToDoubleBiFunction<SV, GSLVarargs> method)
        {
            this.method = Objects.requireNonNull(method);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            return new GSLFloat(method.applyAsDouble(self.cast(), args));
        }
    }
    
    private static final class StringMethod<SV extends GSLValue> extends GSLFunction
    {
        private final BiFunction<SV, GSLVarargs, String> method;
        
        private StringMethod(BiFunction<SV, GSLVarargs, String> method)
        {
            this.method = Objects.requireNonNull(method);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
        {
            return new GSLString(method.apply(self.cast(), args));
        }
    }
    
    private static final class DefaultIterator<V extends GSLValue> extends GSLIterator
    {
        private final Iterator<V> iterator;
        
        private DefaultIterator(Iterator<V> iterator) { this.iterator = Objects.requireNonNull(iterator); }
        
        @Override
        public final boolean hasNext() { return iterator.hasNext(); }

        @Override
        public final GSLValue next() { return iterator.next(); }
    }
}
