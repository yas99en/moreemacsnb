/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import org.netbeans.editor.BaseDocument;

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
