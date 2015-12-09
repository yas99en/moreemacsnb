/*
 * Copyright (c) 2015, Yasuhiro Endoh
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of the authors nor the names of its contributors may be
 *     used to endorse or promote products derived from this software without
 *     specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
