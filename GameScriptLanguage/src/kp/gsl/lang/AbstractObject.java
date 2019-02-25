/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.lang;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import kp.gsl.exception.GSLRuntimeException;
import kp.gsl.exception.NotPointerException;
import kp.gsl.exception.UnsupportedOperatorException;
import kp.gsl.lang.AbstractObject.Property;
import static kp.gsl.lang.GSLValue.NULL;
import kp.gsl.lib.Def;
import kp.gsl.lib.ObjectProperties;

/**
 *
 * @author Asus
 * @param <M>
 */
public abstract class AbstractObject extends GSLValue
{
    final GSLValue parent;
    final Map<String, Property> props;
    
    AbstractObject(Map<String, Property> props, GSLValue parent)
    {
        if(props == null)
            throw new NullPointerException();
        this.props = props;
        this.parent = parent;
    }
    AbstractObject(AbstractObject model) //Move constructor
    {
        this.parent = model.parent;
        this.props = model.props;
    }
    
    public final GSLValue getParent() { return parent; }
    
    @Override
    public final int intValue()
    {
        var prop = operatorGetProperty(ObjectProperties.CAST_INTEGER);
        return prop == NULL ? hashCode() : prop.operatorCall(this, NO_ARGS).intValue();
    }

    @Override
    public final long longValue()
    {
        var prop = operatorGetProperty(ObjectProperties.CAST_INTEGER);
        return prop == NULL ? hashCode() : prop.operatorCall(this, NO_ARGS).longValue();
    }

    @Override
    public final float floatValue()
    {
        var prop = operatorGetProperty(ObjectProperties.CAST_FLOAT);
        return prop == NULL ? hashCode() : prop.operatorCall(this, NO_ARGS).floatValue();
    }

    @Override
    public final double doubleValue()
    {
        var prop = operatorGetProperty(ObjectProperties.CAST_FLOAT);
        return prop == NULL ? hashCode() : prop.operatorCall(this, NO_ARGS).doubleValue();
    }
    
    @Override
    public final char charValue()
    {
        var prop = operatorGetProperty(ObjectProperties.CAST_INTEGER);
        return (char) (prop == NULL ? hashCode() : prop.operatorCall(this, NO_ARGS).charValue());
    }

    @Override
    public final boolean boolValue()
    {
        var prop = operatorGetProperty(ObjectProperties.CAST_BOOL);
        return prop == NULL ? true : prop.operatorCall(this, NO_ARGS).boolValue();
    }

    @Override
    public final String toString()
    {
        var prop = operatorGetProperty(ObjectProperties.CAST_STRING);
        return prop == NULL ? ("object::" + hashCode()) : prop.operatorCall(this, NO_ARGS).toString();
    }

    @Override
    public final GSLValue[] toArray()
    {
        var prop = operatorGetProperty(ObjectProperties.CAST_LIST);
        return prop == NULL ? (stream().toArray(GSLValue[]::new)) : prop.operatorCall(this, NO_ARGS).toArray();
    }

    @Override
    public final List<GSLValue> toList()
    {
        var prop = operatorGetProperty(ObjectProperties.CAST_LIST);
        return prop == NULL ? (stream().collect(Collectors.toList())) : prop.operatorCall(this, NO_ARGS).toList();
    }

    @Override
    public final Map<GSLValue, GSLValue> toMap()
    {
        var prop = operatorGetProperty(ObjectProperties.CAST_MAP);
        return prop == NULL ? Utils.collectionToMap(stream().collect(Collectors.toList())) : prop.operatorCall(this, NO_ARGS).toMap();
    }
    
    @Override
    public final Stream<GSLValue> stream()
    {
        var iter = operatorIterator();
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new InnerIter(iter), Spliterator.ORDERED), false);
    }
    
    @Override
    public final boolean isMutable() { return true; }
    
    
    
    
    @Override public final GSLInteger    operatorCastInteger() { return new GSLInteger(longValue()); }
    @Override public final GSLFloat      operatorCastFloat() { return new GSLFloat(doubleValue()); }
    @Override public final GSLBoolean    operatorCastBoolean() { return boolValue() ? TRUE : FALSE; }
    @Override public final GSLString     operatorCastString() { return new GSLString(toString()); }
    @Override public final GSLConstTuple operatorCastConstTuple() { return new GSLConstTuple(Utils.toImmutable(toArray())); }
    @Override public final GSLConstMap   operatorCastConstMap() { return new GSLConstMap(Utils.toImmutable(toMap())); }
    @Override public final GSLFunction   operatorCastFunction() { return Def.method(AbstractObject.this::operatorCall); }
    @Override public final GSLList       operatorCastList() { return new GSLList(toList()); }
    @Override public final GSLTuple      operatorCastTuple() { return new GSLTuple(toArray()); }
    @Override public final GSLMap        operatorCastMap() { return new GSLMap(toMap()); }
    @Override public final GSLIterator   operatorCastIterator()
    {
        return Def.iterator(new Iterator<GSLValue>()
        {
            @Override public final boolean hasNext() { return AbstractObject.this.hasNext(); }
            @Override public final GSLValue next() { return AbstractObject.this.next(); }
        });
    }
    @Override public final GSLRawBytes   operatorCastRawBytes() { return Utils.objToBytes(props); }
    
    
    

    @Override public final GSLValue operatorEquals(GSLValue value) { return equals(value) ? TRUE : FALSE; }
    @Override public final GSLValue operatorNotEquals(GSLValue value) { return equals(value) ? FALSE : TRUE; }
    @Override public final GSLValue operatorGreater(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_GR);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, ">");
        return prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorSmaller(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_SM);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "<");
        return prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorGreaterEquals(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_GREQ);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, ">=");
        return prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorSmallerEquals(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_SMEQ);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "<=");
        return prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorNegate()
    {
        var prop = operatorGetProperty(ObjectProperties.OP_NOT);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "!");
        return prop.operatorCall(this, NO_ARGS);
    }
    @Override public final int      operatorLength()
    {
        var prop = operatorGetProperty(ObjectProperties.OP_LEN);
        return prop == NULL ? 1 : prop.operatorCall(this, NO_ARGS).intValue();
    }

    @Override public final GSLValue operatorPlus(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_PLUS);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "+");
        return prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorMinus(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_MINUS);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "-");
        return prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorMultiply(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_MUL);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "*");
        return prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorDivide(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_DIV);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "/");
        return prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorRemainder(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_MOD);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "%");
        return prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorIncrease()
    {
        var prop = operatorGetProperty(ObjectProperties.OP_INC);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "++");
        return prop.operatorCall(this, NO_ARGS);
    }
    @Override public final GSLValue operatorDecrease()
    {
        var prop = operatorGetProperty(ObjectProperties.OP_DEC);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "--");
        return prop.operatorCall(this, NO_ARGS);
    }
    @Override public final GSLValue operatorNegative()
    {
        var prop = operatorGetProperty(ObjectProperties.OP_NEG);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "-()");
        return prop.operatorCall(this, NO_ARGS);
    }

    @Override public final GSLValue operatorBitwiseShiftLeft(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_BSHL);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "<<");
        return prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorBitwiseShiftRight(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_BSHR);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, ">>");
        return prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorBitwiseAnd(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_BAND);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "&");
        return prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorBitwiseOr(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_BOR);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "|");
        return prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorBitwiseXor(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_BXOR);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "^");
        return prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorBitwiseNot()
    {
        var prop = operatorGetProperty(ObjectProperties.OP_BNOT);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "~");
        return prop.operatorCall(this, NO_ARGS);
    }

    @Override public final GSLValue operatorGet(GSLValue index)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_GET);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "[]");
        return prop.operatorCall(this, (GSLVarargs) index);
    }
    @Override public final GSLValue operatorGet(int index)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_GET);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "[]");
        return prop.operatorCall(this, (GSLVarargs) new GSLInteger(index));
    }
    @Override public final void operatorSet(GSLValue index, GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_SET);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "[x]=");
        prop.operatorCall(this, new GSLVarargs.Pair(index, value));
    }
    @Override public final void operatorSet(int index, GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_SET);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "[x]=");
        prop.operatorCall(this, new GSLVarargs.Pair(new GSLInteger(index), value));
    }
    @Override public final void operatorAdd(GSLValue value)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_ADD);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "[]=");
        prop.operatorCall(this, (GSLVarargs) value);
    }
    @Override public final GSLValue operatorPeek()
    {
        var prop = operatorGetProperty(ObjectProperties.OP_PEEK);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "[]");
        return prop.operatorCall(this);
    }

    @Override
    public final GSLValue operatorGetProperty(String name)
    {
        Property prop = props.getOrDefault(name, null);
        return prop == null
                ? parent == null ? NULL : parent.operatorGetProperty(name)
                : prop.value;
    }
    
    @Override
    public void operatorSetProperty(String name, GSLValue value)
    {
        Property prop = props.getOrDefault(name, null);
        if(prop == null)
        {
            prop = new Property(name, false);
            props.put(name, prop);
        }
        prop.setValue(value);
    }
    
    @Override
    public void operatorDelProperty(String name)
    {
        props.remove(name);
    }

    @Override
    public final GSLValue operatorCall(GSLValue self, GSLVarargs args)
    {
        var prop = operatorGetProperty(ObjectProperties.OP_CALL);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "()");
        return prop.operatorCall(self, args);
    }
    
    @Override
    public final GSLValue operatorNew(GSLVarargs args)
    {
        GSLValue obj;
        var prop = operatorGetProperty(ObjectProperties.OP_NEW);
        if(prop == NULL)
            obj = new GSLObject(new LinkedHashMap<>());
        else obj = prop.operatorCall(this);
        
        prop = obj.operatorGetProperty(ObjectProperties.CONSTRUCTOR);
        if(prop != NULL)
            prop.operatorCall(obj, args);
        
        return obj;
    }
    
    @Override public final GSLValue operatorReferenceGet() { throw new NotPointerException(this); }
    @Override public final void     operatorReferenceSet(GSLValue value) { throw new NotPointerException(this); }
    
    @Override
    public final GSLValue operatorIterator()
    {
        var prop = operatorGetProperty(ObjectProperties.OP_ITER);
        return prop == null ? (Utils.oneIter(this)) : prop.operatorCall(this);
    }
    @Override public final boolean hasNext()
    {
        var prop = operatorGetProperty(ObjectProperties.OP_HASNEXT);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "hasNext");
        return prop.operatorCall(this, NO_ARGS).boolValue();
    }
    @Override public final GSLValue next()
    {
        var prop = operatorGetProperty(ObjectProperties.OP_NEXT);
        if(prop == NULL)
            throw new UnsupportedOperatorException(this, "next");
        return prop.operatorCall(this, NO_ARGS);
    }
    
    

    @Override public final boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(o instanceof GSLValue)
        {
            var prop = operatorGetProperty(ObjectProperties.OP_EQ);
            return prop != NULL && prop.operatorCall(this, (GSLVarargs) o).boolValue();
        }
        return false;
    }
    @Override public final int hashCode()
    {
        var prop = operatorGetProperty(ObjectProperties.OP_HASHCODE);
        return prop == null ? superHashCode() : prop.operatorCall(this, NO_ARGS).intValue();
    }

    
    
    public static final class Property
    {
        final String name;
        boolean frozen;
        GSLValue value;
        
        Property(String name, boolean frozen)
        {
            this.name = Objects.requireNonNull(name);
            this.frozen = frozen;
            this.value = NULL;
        }
        Property(String name) { this(name, false); }
        Property(String name, boolean frozen, GSLValue value)
        {
            this(name, frozen);
            setValue(value);
        }
        
        public final String getName() { return name; }
        
        public final void setFrozen(boolean flag) { this.frozen = flag; }
        public final boolean isFrozen() { return frozen; }
        
        public final void setValue(GSLValue value)
        {
            if(frozen && this.value != NULL)
                throw new GSLRuntimeException("Struct field " + name + " is frozen");
            this.value = value == null ? NULL : value;
        }
        public final GSLValue getValue() { return value; }
        
        final Property copy()
        {
            var copy = new Property(name, frozen);
            copy.value = value;
            return copy;
        }
        
        @Override
        public final String toString() { return (frozen ? "const " : "") + name + ": " + value; }
    }
    
    private static final class InnerIter implements Iterator<GSLValue>
    {
        private final GSLValue it;
        
        private InnerIter(GSLValue it)
        {
            if(it == null)
                throw new NullPointerException();
            this.it = it;
        }
        @Override public final boolean hasNext() { return it.hasNext(); }
        @Override public final GSLValue next() { return it.next(); }
    }
}
