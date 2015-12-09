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

import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-TransposeWordsAction")
public class TransposeWordsAction extends MoreEmacsAction {
    public TransposeWordsAction() {
        super("transpose-words");
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
        Document doc = target.getDocument();
        int previousBegin = BackwardWordAction.getPreviousWordPosition(doc, current);
        int previousEnd = ForwardWordAction.getNextWordPosition(doc, previousBegin);
        int nextEnd = ForwardWordAction.getNextWordPosition(doc, current);
        int nextBegin = BackwardWordAction.getPreviousWordPosition(doc, nextEnd);
        
        if(nextBegin <= previousEnd) {
            return;
        }
        
        String previous = doc.getText(previousBegin, previousEnd-previousBegin);
        String simbols = doc.getText(previousEnd, nextBegin-previousEnd);
        String next = doc.getText(nextBegin, nextEnd-nextBegin);

        modifyAtomicAsUser(target, () -> {
            try {
                doc.remove(previousBegin, nextEnd-previousBegin);
                doc.insertString(previousBegin, next+simbols+previous, null);
            } catch (BadLocationException ex) {
                throw new AssertionError(ex.getMessage(), ex);
            }
        });
        
    }
}
