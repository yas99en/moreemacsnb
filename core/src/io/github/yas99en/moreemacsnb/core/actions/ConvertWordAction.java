/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Yasuhiro Endoh
 */
public abstract class ConvertWordAction extends MoreEmacsAction {
    public ConvertWordAction(String name) {
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        if(!target.isEditable()) {
            return;
        }
        
        Caret caret = target.getCaret();
        int current = caret.getDot();
        Document doc = target.getDocument();
        int next = ForwardWordAction.getNextWordPosition(doc, current);
        String word;
        try {
            word = doc.getText(current, next-current);
        } catch (BadLocationException ex) {
            throw new AssertionError(ex.getMessage(), ex);
        }
        String  convertedWord = convert(word);

        modifyAtomicAsUser(target, () -> {
            try {
                doc.remove(current, next-current);
                doc.insertString(current, convertedWord, null);
            } catch (BadLocationException ex) {
                throw new AssertionError(ex.getMessage(), ex);
            }
        });
    }
    
    protected abstract String convert(String word);
}
