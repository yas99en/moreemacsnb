/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import org.netbeans.editor.BaseDocument;
import org.netbeans.lib.editor.util.swing.DocumentUtilities;

/**
 *
 * @author Yasuhiro Endoh
 */
public abstract class MoreEmacsAction extends TextAction {

    public MoreEmacsAction(String name) {
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextComponent target = getTextComponent(e);
        if(target == null) {
            return;
        }
        Mark.update(target);
        actionPerformed(e, target);
    }

    public abstract void actionPerformed(ActionEvent e, JTextComponent target);
    
    public static void modifyAtomicAsUser(JTextComponent target, Runnable runnable) {
        BaseDocument doc = (BaseDocument)target.getDocument();
        doc.runAtomicAsUser (() -> {
            try {
                DocumentUtilities.setTypingModification(doc, true);
                runnable.run();
            } finally {
                DocumentUtilities.setTypingModification(doc, false);
            }
        });
        
    }
}
