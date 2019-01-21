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
import kp.gsl.lang.GSLFunction;
import kp.gsl.lang.GSLIterator;
import kp.gsl.lang.GSLValue;

/**
 *
 * @author Asus
 */
public final class Def
{
    private Def() {}
    
    public static final GSLFunction voidFunction(Consumer<GSLValue[]> function) { return new VoidFunction(function); }
    public static final GSLFunction function(java.util.function.Function<GSLValue[], GSLValue> function) { return new Function(function); }
    
    public static final <SV extends GSLValue> GSLFunction voidMethod(BiConsumer<SV, GSLValue[]> method) { return new VoidMethod<>(method); }
    public static final <SV extends GSLValue> GSLFunction method(BiFunction<SV, GSLValue[], GSLValue> method) { return new Method<>(method); }
    
    public static final <V extends GSLValue> GSLIterator iterator(Iterator<V> iterator) { return new DefaultIterator(iterator); }
    
    
    private static final class VoidFunction extends GSLFunction
    {
        private final Consumer<GSLValue[]> function;
        
        private VoidFunction(Consumer<GSLValue[]> function)
        {
            this.function = Objects.requireNonNull(function);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLValue[] args)
        {
            function.accept(args);
            return NULL;
        }
    }
    
    private static final class Function extends GSLFunction
    {
        private final java.util.function.Function<GSLValue[], GSLValue> function;
        
        private Function(java.util.function.Function<GSLValue[], GSLValue> function)
        {
            this.function = Objects.requireNonNull(function);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLValue[] args)
        {
            return function.apply(args);
        }
    }
    
    private static final class VoidMethod<SV extends GSLValue> extends GSLFunction
    {
        private final BiConsumer<SV, GSLValue[]> method;
        
        private VoidMethod(BiConsumer<SV, GSLValue[]> method)
        {
            this.method = Objects.requireNonNull(method);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLValue[] args)
        {
            method.accept(self.cast(), args);
            return NULL;
        }
    }
    
    private static final class Method<SV extends GSLValue> extends GSLFunction
    {
        private final BiFunction<SV, GSLValue[], GSLValue> method;
        
        private Method(BiFunction<SV, GSLValue[], GSLValue> method)
        {
            this.method = Objects.requireNonNull(method);
        }

        @Override
        public final GSLValue operatorCall(GSLValue self, GSLValue[] args)
        {
            return method.apply(self.cast(), args);
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
