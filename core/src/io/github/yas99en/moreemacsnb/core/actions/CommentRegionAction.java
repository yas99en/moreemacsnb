/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;
import org.netbeans.editor.BaseKit;
import org.netbeans.editor.Utilities;
import org.netbeans.editor.ext.ExtKit.ToggleCommentAction;
import static org.netbeans.editor.ext.ExtKit.toggleCommentAction;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-CommentRegionAction")
public class CommentRegionAction extends MoreEmacsAction {
    public CommentRegionAction() {
        super("comment-region");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        System.out.println("CommentRegionAction.actionPerformed()");

        BaseKit kit = Utilities.getKit(target);
        if(kit == null) {
            target.getToolkit().beep();
            return;
        }
        
        Action action = kit.getActionByName(toggleCommentAction);
        if (action == null) {
            target.getToolkit().beep();
            return;
        }
        
        Caret caret = target.getCaret();
        int dot = caret.getDot();
        if(dot != caret.getMark()) {
            action.actionPerformed(e);
            return;
        }
        
        modifyAtomicAsUser(target, () -> {
            caret.setDot(Mark.get(target));
            caret.moveDot(dot);
            action.actionPerformed(e);
            int newDot = caret.getDot();
            target.select(newDot, newDot);
        });
    }
    
}
