/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Timer;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;
import org.netbeans.editor.BaseKit;
import org.netbeans.editor.Utilities;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-KeyboardQuitAction")
public class KeyboardQuitAction extends MoreEmacsAction {
    public KeyboardQuitAction() {
        super("keyboard-quit");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        System.out.println("KeyboardQuitAction.actionPerformed()");
        
        if(escape(e, target)) {
            return;
        }

        try {
            Robot robot = new java.awt.Robot();
            Timer timer = new Timer(500, ev -> {
                robot.keyPress(KeyEvent.VK_ESCAPE);
                robot.keyRelease(KeyEvent.VK_ESCAPE);
            });
            timer.setRepeats(false);
            timer.start();
        } catch (AWTException ex) {
            Logger.getLogger(KeyboardQuitAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean escape(ActionEvent e, JTextComponent target) {
        BaseKit kit = Utilities.getKit(target);
        if(kit == null) {
            return false;
        }

        Action action = kit.getActionByName("escape");
        if (action != null) {
            action.actionPerformed(e);
            return true;
        }
        action = kit.getActionByName("Escape");
        if (action != null) {
            action.actionPerformed(e);
            return true;
        }
        
        ActionMap actionMap = target.getActionMap();

        System.out.println("actionMap: " + Arrays.asList(actionMap.keys()));

        action = actionMap.get("escape");
        if (action != null) {
            action.actionPerformed(e);
            return true;
        }

        action = actionMap.get("Escape");
        if (action != null) {
            action.actionPerformed(e);
            return true;
        }

        System.out.println("action not found");
        return false;
    }
    
}
