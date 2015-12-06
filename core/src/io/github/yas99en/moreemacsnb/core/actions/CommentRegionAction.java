/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.ActionMap;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;

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
        ActionMap actionMap = target.getActionMap();
        System.out.println("actions: " + Arrays.asList(actionMap.keys()));
    }
    
}
