/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.*;
import org.netbeans.editor.BaseDocument;
import org.netbeans.lib.editor.util.swing.DocumentUtilities;


/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-KillRingSaveAction")
public class KillRingSaveAction extends MoreEmacsAction  {
    public KillRingSaveAction() {
        super("kill-ring-save");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        if (!target.isEditable()) {
            return;
        }
        Caret caret = target.getCaret();
        ActionMap actionMap = target.getActionMap();
        if(actionMap == null)  {
            return;
        }
        Action copyAction = actionMap.get(DefaultEditorKit.copyAction);
        
        if(caret.getDot() != caret.getMark()) {
            copyAction.actionPerformed(e);
            return;
        }
        
        BaseDocument doc = (BaseDocument)target.getDocument();
        doc.runAtomicAsUser (() -> {
            try {
                DocumentUtilities.setTypingModification(doc, true);
                int mark = Mark.get(target);
                int dot = caret.getDot();
                caret.setDot(mark);
                caret.moveDot(dot);
                target.copy();
                target.select(dot, dot);
            } finally {
                DocumentUtilities.setTypingModification(doc, false);
            }
        });
    }
}
