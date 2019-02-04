/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 *
 * @author Asus
 */
public abstract class GSLReference extends GSLValue
{
    @Override
    public final GSLDataType getGSLDataType() { return operatorReferenceGet().getGSLDataType(); }

    @Override
    public final boolean isMutable() { return true; }

    @Override
    public final int intValue() { return operatorReferenceGet().intValue(); }

    @Override
    public final long longValue() { return operatorReferenceGet().longValue(); }

    @Override
    public final float floatValue() { return operatorReferenceGet().floatValue(); }

    @Override
    public final double doubleValue() { return operatorReferenceGet().doubleValue(); }

    @Override
    public final char charValue() { return operatorReferenceGet().charValue(); }

    @Override
    public final boolean boolValue() { return operatorReferenceGet().boolValue(); }

    @Override
    public final String toString() { return operatorReferenceGet().toString(); }

    @Override
    public final GSLValue[] toArray() { return operatorReferenceGet().toArray(); }

    @Override
    public final List<GSLValue> toList() { return operatorReferenceGet().toList(); }

    @Override
    public final Map<GSLValue, GSLValue> toMap() { return operatorReferenceGet().toMap(); }

    @Override
    public final Stream<GSLValue> stream() { return operatorReferenceGet().stream(); }
    
    @Override public final GSLInteger    operatorCastInteger() { return operatorReferenceGet().operatorCastInteger(); }
    @Override public final GSLFloat      operatorCastFloat() { return operatorReferenceGet().operatorCastFloat(); }
    @Override public final GSLBoolean    operatorCastBoolean() { return operatorReferenceGet().operatorCastBoolean(); }
    @Override public final GSLString     operatorCastString() { return operatorReferenceGet().operatorCastString(); }
    @Override public final GSLConstTuple operatorCastConstTuple() { return operatorReferenceGet().operatorCastConstTuple(); }
    @Override public final GSLConstMap   operatorCastConstMap() { return operatorReferenceGet().operatorCastConstMap(); }
    @Override public final GSLFunction   operatorCastFunction() { return operatorReferenceGet().operatorCastFunction(); }
    @Override public final GSLList       operatorCastList() { return operatorReferenceGet().operatorCastList(); }
    @Override public final GSLTuple      operatorCastTuple() { return operatorReferenceGet().operatorCastTuple(); }
    @Override public final GSLMap        operatorCastMap() { return operatorReferenceGet().operatorCastMap(); }
    @Override public final GSLStruct     operatorCastStruct() { return operatorReferenceGet().operatorCastStruct(); }
    @Override public final GSLBlueprint  operatorCastBlueprint() { return operatorReferenceGet().operatorCastBlueprint(); }
    @Override public final GSLObject     operatorCastObject() { return operatorReferenceGet().operatorCastObject(); }
    @Override public final GSLIterator   operatorCastIterator() { return operatorReferenceGet().operatorCastIterator(); }
    @Override public final GSLRawBytes   operatorCastRawBytes() { return operatorReferenceGet().operatorCastRawBytes(); }

    @Override
    public final GSLValue operatorEquals(GSLValue value) { return operatorReferenceGet().operatorEquals(value); }

    @Override
    public final GSLValue operatorNotEquals(GSLValue value) { return operatorReferenceGet().operatorNotEquals(value); }

    @Override
    public final GSLValue operatorGreater(GSLValue value) { return operatorReferenceGet().operatorGreater(value); }

    @Override
    public final GSLValue operatorSmaller(GSLValue value) { return operatorReferenceGet().operatorSmaller(value); }

    @Override
    public final GSLValue operatorGreaterEquals(GSLValue value) { return operatorReferenceGet().operatorGreaterEquals(value); }

    @Override
    public final GSLValue operatorSmallerEquals(GSLValue value) { return operatorReferenceGet().operatorSmallerEquals(value); }

    @Override
    public final GSLValue operatorNegate() { return operatorReferenceGet().operatorNegate(); }

    @Override
    public final int operatorLength() { return operatorReferenceGet().operatorLength(); }

    @Override
    public final GSLValue operatorPlus(GSLValue value) { return operatorReferenceGet().operatorPlus(value); }

    @Override
    public final GSLValue operatorMinus(GSLValue value) { return operatorReferenceGet().operatorMinus(value); }

    @Override
    public final GSLValue operatorMultiply(GSLValue value) { return operatorReferenceGet().operatorMultiply(value); }

    @Override
    public final GSLValue operatorDivide(GSLValue value) { return operatorReferenceGet().operatorDivide(value); }

    @Override
    public final GSLValue operatorRemainder(GSLValue value) { return operatorReferenceGet().operatorRemainder(value); }

    @Override
    public final GSLValue operatorIncrease() { return operatorReferenceGet().operatorIncrease(); }

    @Override
    public final GSLValue operatorDecrease() { return operatorReferenceGet().operatorDecrease(); }

    @Override
    public final GSLValue operatorNegative() { return operatorReferenceGet().operatorNegative(); }

    @Override
    public final GSLValue operatorBitwiseShiftLeft(GSLValue value) { return operatorReferenceGet().operatorBitwiseShiftLeft(value); }

    @Override
    public final GSLValue operatorBitwiseShiftRight(GSLValue value) { return operatorReferenceGet().operatorBitwiseShiftRight(value); }

    @Override
    public final GSLValue operatorBitwiseAnd(GSLValue value) { return operatorReferenceGet().operatorBitwiseAnd(value); }

    @Override
    public final GSLValue operatorBitwiseOr(GSLValue value) { return operatorReferenceGet().operatorBitwiseOr(value); }

    @Override
    public final GSLValue operatorBitwiseXor(GSLValue value) { return operatorReferenceGet().operatorBitwiseXor(value); }

    @Override
    public final GSLValue operatorBitwiseNot() { return operatorReferenceGet().operatorBitwiseNot(); }

    @Override
    public final GSLValue operatorGet(GSLValue index) { return operatorReferenceGet().operatorGet(index); }
    
    @Override
    public final GSLValue operatorGet(int index) { return operatorReferenceGet().operatorGet(index); }

    @Override
    public final void operatorSet(GSLValue index, GSLValue value) { operatorReferenceGet().operatorSet(index, value); }
    
    @Override
    public final void operatorSet(int index, GSLValue value) { operatorReferenceGet().operatorSet(index, value); }
    
    @Override
    public final void operatorAdd(GSLValue value) { operatorReferenceGet().operatorAdd(value); }

    @Override
    public final GSLValue operatorGetProperty(String name) { return operatorReferenceGet().operatorGetProperty(name); }

    @Override
    public final void operatorSetProperty(String name, GSLValue value) { operatorReferenceGet().operatorSetProperty(name, value); }

    @Override
    public final void operatorDelProperty(String name) { operatorReferenceGet().operatorDelProperty(name); }

    @Override
    public final GSLValue operatorCall(GSLValue self, GSLVarargs args) { return operatorReferenceGet().operatorCall(self, args); }

    @Override
    public final GSLValue operatorNew(GSLVarargs args) { return operatorReferenceGet().operatorNew(args); }

    @Override
    public abstract GSLValue operatorReferenceGet();

    @Override
    public abstract void operatorReferenceSet(GSLValue value);

    @Override
    public final GSLValue operatorIterator() { return operatorReferenceGet().operatorIterator(); }

    @Override
    public final boolean hasNext() { return operatorReferenceGet().hasNext(); }

    @Override
    public final GSLValue next() { return operatorReferenceGet().next(); }

    @Override
    public final boolean equals(Object o) { return operatorReferenceGet().equals(o); }

    @Override
    public final int hashCode() { return operatorReferenceGet().hashCode(); }
    
    
    
    
    public static final class StackReference extends GSLReference
    {
        private final GSLValue[] stack;
        private final int index;
        
        public StackReference(GSLValue[] stack, int index)
        {
            this.stack = stack;
            this.index = index;
        }

        @Override
        public final GSLValue operatorReferenceGet() { return stack[index]; }

        @Override
        public final void operatorReferenceSet(GSLValue value) { stack[index] = value; }
    }
}
