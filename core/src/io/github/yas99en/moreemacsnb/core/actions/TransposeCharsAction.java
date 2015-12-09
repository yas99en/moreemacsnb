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

import io.github.yas99en.moreemacsnb.core.utils.DocumentCharSequence;
import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;
import org.netbeans.editor.BaseDocument;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-TransposeCharsAction")
public class TransposeCharsAction extends MoreEmacsAction {
    public TransposeCharsAction() {
        super("transpose-chars");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        try {
            doActionPerformed(e, target);
        } catch (BadLocationException ex) {
            throw new AssertionError(ex.getMessage(), ex);
        }
    }

    public void doActionPerformed(ActionEvent e, JTextComponent target) throws BadLocationException {
        if(!target.isEditable() || !target.isEnabled()) {
            target.getToolkit().beep();
            return;
        }

        Caret caret = target.getCaret();
        int current = caret.getDot();
        if(current == 0) {
            // beginning of document
            return;
        }

        BaseDocument doc = (BaseDocument)target.getDocument();
        Element rootElem = doc.getDefaultRootElement();
        int linePos = rootElem.getElementIndex(current);
        Element line = rootElem.getElement(linePos);

        DocumentCharSequence seq = new DocumentCharSequence(doc);
        boolean onLineStart = (line.getStartOffset() == current);
        boolean onLineEnd   = (line.getEndOffset()-1 == current);

        if(line.getEndOffset()-1 == current) {
            // if end of line, adjust current position
            current = (line.getStartOffset() == current)
                    ? current - 1 : seq.previousCodePointIndex(current);
            
            linePos = rootElem.getElementIndex(current);
            line = rootElem.getElement(linePos);
        }

        if(current == 0) {
            // beginning of document again
            return;
        }

        int nextIndex = seq.nextCodePointIndex(current);
        String forwardChars = (line.getEndOffset()-1 == current)
                ? "\n" : doc.getText(current, nextIndex - current);
        int prevIndex = seq.previousCodePointIndex(current);
        String backwardChars = (line.getStartOffset() == current)
                ? "\n" : doc.getText(prevIndex, current-prevIndex);

        int pos = current-backwardChars.length();
        int len = backwardChars.length() + forwardChars.length();
        
        modifyAtomicAsUser(target, () -> {
            try {
                doc.remove(pos, len);
                doc.insertString(pos, forwardChars+backwardChars, null);
                if(!onLineStart && !onLineEnd) {
                    caret.setDot(pos + forwardChars.length());
                }
            } catch (BadLocationException ex) {
                throw new AssertionError(ex.getMessage(), ex);
            }
        });
    }
    
}
