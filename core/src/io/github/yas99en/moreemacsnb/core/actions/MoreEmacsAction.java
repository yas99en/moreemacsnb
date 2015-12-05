/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

/**
 *
 * @author yendoh
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
}
