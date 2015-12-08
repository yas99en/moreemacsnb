/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import io.github.yas99en.moreemacsnb.core.utils.CodePointIterator;
import io.github.yas99en.moreemacsnb.core.utils.DocumentCharSequence;
import java.awt.event.ActionEvent;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-ForwardWordAction")
public class ForwardWordAction extends MoreEmacsAction {
    public ForwardWordAction() {
        super("forward-word");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        Caret caret = target.getCaret();
        int next = getNextWordPosition(target.getDocument(), caret.getDot());
        caret.setDot(next);
    }
    
    public static int getNextWordPosition(Document doc, int offset) {
        DocumentCharSequence seq = new DocumentCharSequence(doc, offset, doc.getLength()-offset);
        CodePointIterator itr = new CodePointIterator(seq);
        
        for(; itr.hasNext(); ) {
            int codePoint = itr.next();
            if (Character.isLetterOrDigit(codePoint)) {
                itr.previous();
                break;
            }
        }
        for(; itr.hasNext(); ) {
            if (!Character.isLetterOrDigit(itr.next())) {
                itr.previous();
                break;
            }
        }
        
        return offset + itr.index();
    }
}
