/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kp.gsl.script.bytecode;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import kp.gsl.exception.CompilerError;
import kp.gsl.script.compiler.RuntimeStack;

/**
 *
 * @author Asus
 */
public final class BytecodeList implements Iterable<BytecodeList.BytecodeLocation>
{
    private final RuntimeStack stack;
    private final BytecodeLocation top = new BytecodeLocation();
    private BytecodeLocation bottom = new BytecodeLocation();
    private int size = 0;
    
    public BytecodeList(RuntimeStack stack)
    {
        this.stack = Objects.requireNonNull(stack);
        top.next = bottom;
        bottom.previous = top;
    }

    public final int size() { return size; }

    public final boolean isEmpty() { return top.next == bottom; }

    public final boolean contains(Bytecode b)
    {
        if(isEmpty())
            return false;
        for(BytecodeLocation it : this)
            if(it.bytecode.equals(b))
                return true;
        return false;
    }

    @Override
    public final Iterator<BytecodeLocation> iterator()
    {
        return new Iterator<BytecodeLocation>()
        {
            private BytecodeLocation it = top;
            
            @Override public final boolean hasNext() { return it.next != bottom; }
            @Override public final BytecodeLocation next() { return it = it.next(); }
        };
    }
    public final void forEachLocation(Consumer<BytecodeLocation> consumer)
    {
        for(BytecodeLocation it : this)
            consumer.accept(it);
    }

    public final BytecodeLocation[] toArray()
    {
        BytecodeLocation[] array = new BytecodeLocation[size];
        int count = 0;
        for(BytecodeLocation it : this)
            array[count++] = it;
        return array;
    }
    
    public final BytecodeLocation append(Bytecode e) throws CompilerError
    {
        BytecodeLocation last = bottom;
        bottom = new BytecodeLocation();
        last.bytecode = e;
        last.next = bottom;
        bottom.previous = last;
        stack.modify(e);
        size++;
        return last;
    }
    
    public final BytecodeLocation append(Bytecode... opcodes) throws CompilerError
    {
        BytecodeLocation loc = bottom.previous;
        for (Bytecode opcode : opcodes)
            loc = append(opcode);
        return loc;
    }

    public final BytecodeLocation append(Collection<? extends Bytecode> c) throws CompilerError
    {
        BytecodeLocation loc = bottom.previous;
        for(Bytecode op : c)
            loc = append(op);
        return loc;
    }
    
    public final BytecodeLocation insert(BytecodeLocation loc, Bytecode element) throws CompilerError
    {
        loc.checkParentList(this);
        if(loc == top)
            if(isEmpty())
                return append(element);
            else throw new IllegalArgumentException("Cannot replace header opcode location.");
        if(loc == bottom)
            return append(element);
        BytecodeLocation newloc = new BytecodeLocation(element);
        newloc.previous = loc.previous;
        newloc.next = loc;
        newloc.previous.next = newloc;
        loc.previous = newloc;
        stack.modify(element);
        size++;
        return newloc;
    }
    public final BytecodeLocation insertBefore(BytecodeLocation loc, Bytecode element) throws CompilerError { return insert(loc.previous, element); }
    public final BytecodeLocation insertAfter(BytecodeLocation loc, Bytecode element) throws CompilerError { return insert(loc.next, element); }
    
    public final BytecodeLocation insert(BytecodeLocation loc, Bytecode... opcodes) throws CompilerError
    {
        for (Bytecode opcode : opcodes)
            loc = insert(loc, opcode);
        return loc;
    }
    public final BytecodeLocation insertBefore(BytecodeLocation loc, Bytecode... opcodes) throws CompilerError { return insert(loc.previous, opcodes); }
    public final BytecodeLocation insertAfter(BytecodeLocation loc, Bytecode... opcodes) throws CompilerError { return insert(loc.next, opcodes); }

    public final BytecodeLocation insert(BytecodeLocation loc, Collection<? extends Bytecode> c) throws CompilerError
    {
        loc.checkParentList(this);
        for(Bytecode op : c)
            loc = insert(loc, op);
        return loc;
    }
    public final BytecodeLocation insertBefore(BytecodeLocation loc, Collection<? extends Bytecode> c) throws CompilerError { return insert(loc.previous, c); }
    public final BytecodeLocation insertAfter(BytecodeLocation loc, Collection<? extends Bytecode> c) throws CompilerError { return insert(loc.next, c); }
    
    public final void setBranchBytecodeTargetLocation(BytecodeLocation loc, BytecodeLocation branchTarget)
    {
        Bytecode b = get(loc);
        b.setTargetBranchPosition(branchTarget);
    }
    public final void setJumpBytecodeLocationToBottom(BytecodeLocation loc)
    {
        Bytecode b = get(loc);
        b.setTargetBranchPosition(bottom);
    }

    public final void clear()
    {
        BytecodeLocation loc = top.next;
        while(loc != bottom)
        {
            loc.previous = null;
            loc.bytecode = null;
            loc = loc.next;
            loc.previous.next = null;
        }
        bottom.previous = null;
        bottom = new BytecodeLocation();
        top.next = bottom;
        bottom.previous = top;
        size = 0;
    }

    public final Bytecode get(BytecodeLocation loc)
    {
        loc.checkParentList(this);
        if(loc == top || loc == bottom)
            throw new IllegalArgumentException("Cannot get opcode from top or bottom opcode location.");
        return loc.bytecode;
    }
    
    public final BytecodeLocation getLastLocation() { return bottom.previous; }
    public final BytecodeLocation getBottomLocation() { return bottom; }

    public final BytecodeLocation set(BytecodeLocation loc, Bytecode element) throws CompilerError
    {
        loc.checkParentList(this);
        if(loc == top || loc == bottom)
            throw new IllegalArgumentException("Cannot set top or bottom opcode location.");
        if(element == null)
            throw new NullPointerException();
        stack.modifyInverse(loc.bytecode);
        loc.bytecode = element;
        stack.modify(element);
        return loc;
    }
    
    public final int buildBytePositions(int offset)
    {
        int count = offset;
        for(BytecodeLocation loc : this)
        {
            loc.firstByte = count;
            count += loc.bytecode.getBytesCount();
        }
        return count - offset;
    }
    
    public final void buildBytecodes(byte[] bytecode)
    {
        for(BytecodeLocation loc : this)
        {
            loc.bytecode.build(bytecode, loc.firstByte);
            
        }
    }
    
    
    
    public final class BytecodeLocation implements BranchTarget
    {
        private Bytecode bytecode;
        private BytecodeLocation previous;
        private BytecodeLocation next;
        private int firstByte = -1;
        
        private BytecodeLocation(Bytecode opcode)
        {
            if(opcode == null)
                throw new NullPointerException();
            this.bytecode = opcode;
        }
        
        private BytecodeLocation() { this.bytecode = null; } //top or bottom
        
        public final boolean isTop() { return this == top; }
        public final boolean isBottom() { return this == bottom; }
        
        public final Bytecode getBytecode() { return bytecode; }
        
        public final boolean hasPrevious() { return previous != null; }
        public final boolean hasNext() { return next != null; }
        
        public final BytecodeLocation previous() { return previous; }
        public final BytecodeLocation next() { return next; }
        
        public final int getFirstByte() { return firstByte; }
        
        private void checkParentList(BytecodeList list)
        {
            if(list != BytecodeList.this)
                throw new IllegalStateException();
        }
        
        @Override
        public final PositionValue getPosition() { return new PositionValue(firstByte); }
    }
}
