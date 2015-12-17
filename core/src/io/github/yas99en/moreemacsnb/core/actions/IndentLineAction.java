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
import javax.swing.Action;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;
import org.netbeans.editor.BaseKit;
import org.netbeans.editor.Utilities;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-IndentLineAction")
public class IndentLineAction extends MoreEmacsAction {
    public IndentLineAction() {
        super("indent-line");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        if (!target.isEditable() || !target.isEnabled()) {
            target.getToolkit().beep();
            return;
        }

        BaseKit kit = Utilities.getKit(target);
        if(kit == null) {
            target.getToolkit().beep();
            return;
        }

        Action action = kit.getActionByName(BaseKit.reindentLineAction);
        if (action == null) {
            target.getToolkit().beep();
            return;
        }

        action.actionPerformed(e);
        moveCaret(target);
    }
    
    private static void moveCaret(JTextComponent target) {
        Caret caret = target.getCaret();
        int dot = caret.getDot();
        
        Document doc = target.getDocument();
        Element rootElem = doc.getDefaultRootElement();
        int row = rootElem.getElementIndex(dot);
        Element line = rootElem.getElement(row);
        
        int length = line.getEndOffset() - line.getStartOffset() - 1;
        DocumentCharSequence seq = new DocumentCharSequence(doc, line.getStartOffset(), length);
        CodePointIterator itr = new CodePointIterator(seq);
        for(; itr.hasNext(); ) {
            int index = itr.index();
            int cp = itr.next();
            if(Character.isWhitespace(cp)) {
                continue;
            }
            if(dot >= line.getStartOffset() + index) {
                return;
            }
            itr.previous();
            break;
        }
        caret.setDot(line.getStartOffset() + itr.index());
    }
}
