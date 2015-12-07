package io.github.yas99en.moreemacsnb.core.utils;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public final class DocumentCharSequence implements CharSequence {
    private final Document doc;
    private final int offset;
    private final int length;

    public DocumentCharSequence(Document doc) {
        this(doc, 0, doc.getLength());
    }
    
    public DocumentCharSequence(Document doc, int offset, int length) {
        if(doc == null) {
            throw new NullPointerException("doc is null");
        }

        this.doc = doc;
        this.offset = offset;
        this.length = length;

        validate(offset, length, doc.getLength());
    }
    
    private static void validate(int offset, int length, int capacity) {
        if(offset < 0 || length < 0 || offset+length > capacity) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public char charAt(int index) {
        if(index < 0 || index >= length) {
            throw new IndexOutOfBoundsException();
        }
        try {
            return doc.getText(offset+index, 1).charAt(0);
        } catch (BadLocationException e) {
            throw new IndexOutOfBoundsException(e.getMessage());
        }
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        validate(start, end-start, length);
        return new DocumentCharSequence(doc, offset+start, end - start);
    }

    public int nextCodePointIndex(int index) {
        int codePoint = Character.codePointAt(this, index);        
        return index + Character.charCount(codePoint);     
    }
    public int previousCodePointIndex(int index) {
        int codePoint = Character.codePointBefore(this, index);        
        return index - Character.charCount(codePoint);     
    }
}
