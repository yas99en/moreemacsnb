/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
