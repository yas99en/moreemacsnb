/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-TransposeWordAction")
public class TransposeWordAction extends MoreEmacsAction {
    public TransposeWordAction() {
        super("transpose-word");
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
