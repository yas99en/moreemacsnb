package io.github.yas99en.moreemacsnb.core.utils;

import java.util.Iterator;

public final class CodePointIterator implements Iterator<Integer> {
    private final CharSequence seq;
    private int index;

    public CodePointIterator(CharSequence seq) {
        this(seq, 0);
    }
    
    public CodePointIterator(CharSequence seq, int index) {
        if(seq == null) {
            throw new NullPointerException("seq is null");
        }

        this.seq = seq;
        setIndex(index);
    }
    
    public void setIndex(int index) {
        if(index < 0 || index > seq.length()) {
            throw new IndexOutOfBoundsException();
        }
        this.index = index;
    }
    
    @Override
    public boolean hasNext() {       
        return index < seq.length();       
    }
    
    public boolean hasPrevious() {       
        return index > 0;
    }
    
    public int index() {
        return index;
    }
    
    @Override
    public Integer next() {      
        int codePoint = Character.codePointAt(seq, index);        
        index += Character.charCount(codePoint);     
        return codePoint;        
    }

    public Integer previous() {      
        int codePoint = Character.codePointBefore(seq, index);        
        index -= Character.charCount(codePoint);     
        return codePoint;        
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("unsupported");
    }

    public static Iterable<Integer> each(final CharSequence seq) {
        return new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return new CodePointIterator(seq);
            }
            
        };
    }
}
