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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;

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
    
}
