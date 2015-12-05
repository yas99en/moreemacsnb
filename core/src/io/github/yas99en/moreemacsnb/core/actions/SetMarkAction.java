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
 * @author yendoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-SetMarkAction")
public class SetMarkAction extends MoreEmacsAction {
    static int nextId = 0;
    int id = nextId++;

    public SetMarkAction() {
        super("set-mark");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        Caret caret = target.getCaret();
        Mark.set(target, caret.getDot());
    }
    
}
