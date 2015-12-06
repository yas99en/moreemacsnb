/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-ExchangePointAndMarkAction")
public class ExchangePointAndMarkAction extends MoreEmacsAction {
    public ExchangePointAndMarkAction() {
        super("set-mark");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        Caret caret = target.getCaret();
        int mark = Mark.get(target);
        Mark.set(target, caret.getDot());
        caret.setDot(mark);
    }
    
}
