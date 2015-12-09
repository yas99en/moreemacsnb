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
package io.github.yas99en.moreemacsnb.core.actions;

import io.github.yas99en.moreemacsnb.core.utils.CodePointIterator;
import io.github.yas99en.moreemacsnb.core.utils.DocumentCharSequence;
import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-DeleteHorizontalSpaceAction")
public class DeleteHorizontalSpaceAction extends MoreEmacsAction {
    public DeleteHorizontalSpaceAction() {
        super("delete-horizontal-space");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        Document doc = target.getDocument();

        if (!target.isEditable() || !target.isEnabled()) {
            target.getToolkit().beep();
            return;
        }
        
        Caret caret = target.getCaret();
        int current = caret.getDot();
        int start;
        try {
            start = skipBackwardSpaces(doc, current);
            int end = skipForwardSpaces(doc, current);
            doc.remove(start, end-start);
        } catch (BadLocationException ex) {
            throw new AssertionError(ex.getMessage(), ex);
        }
    }
    
    int skipBackwardSpaces(Document doc, int offset) throws BadLocationException {
        Element rootElem = doc.getDefaultRootElement();
        int linePos = rootElem.getElementIndex(offset);
        Element line = rootElem.getElement(linePos);

        
        CharSequence seq = new DocumentCharSequence(doc, 
                line.getStartOffset(), offset-line.getStartOffset());
        
        int result = offset;
        for(CodePointIterator itr = new CodePointIterator(seq, seq.length()); itr.hasPrevious(); ) {
            int codePoint = itr.previous();
            if (!Character.isWhitespace(codePoint)) {
                break;
            }
            result = line.getStartOffset() + itr.index();
        }
        return result;
    }
    
    int skipForwardSpaces(Document doc, int offset) throws BadLocationException {
        Element rootElem = doc.getDefaultRootElement();
        int linePos = rootElem.getElementIndex(offset);
        Element line = rootElem.getElement(linePos);

        CharSequence seq = new DocumentCharSequence(doc, 
                offset,line.getEndOffset()-1-offset);
        int result = offset;
        for(CodePointIterator itr = new CodePointIterator(seq); itr.hasNext(); ) {
            int codePoint = itr.next();
            if (!Character.isWhitespace(codePoint)) {
                break;
            }
            result = offset + itr.index();
        }
        return result;
    }
}
