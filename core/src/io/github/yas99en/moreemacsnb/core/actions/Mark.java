/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;

/**
 *
 * @author yendoh
 */
public class Mark {
    public static void update(JTextComponent target) {
        Caret caret = target.getCaret();
        if(caret.getDot() != caret.getMark()) {
            set(target, caret.getMark());
        }
    }
    public static void set(JTextComponent target, int mark) {
        int length = target.getDocument().getLength();
        if(mark < 0) {
            mark = 0;
        }
        if(mark > length) {
            mark = length;
        }
        target.putClientProperty("io.github.yas99en.moreemacsnb.mark", mark);
    }

    public static int get(JTextComponent target) {
        Object markObj = target.getClientProperty("io.github.yas99en.moreemacsnb.mark");
        if(markObj == null) {
            return 0;
        }
        int mark = (Integer) markObj;
        int length = target.getDocument().getLength();
        
        return (mark <= length) ? mark : length;
    }
}
