/*
 * Copyright (c) 2015, Yasuhiro Endoh
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of the authors nor the names of its contributors may be
 *     used to endorse or promote products derived from this software without
 *     specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package io.github.yas99en.moreemacsnb.core.utils;

import java.util.Objects;
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
        Objects.requireNonNull(doc, "doc is null");

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
