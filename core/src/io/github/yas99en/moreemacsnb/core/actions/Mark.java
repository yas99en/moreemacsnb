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
class Mark {
    private Mark() {}
    private static final String MARK_KEY = "io.github.yas99en.moreemacsnb.mark";

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
        target.putClientProperty(MARK_KEY, mark);
    }

    public static int get(JTextComponent target) {
        Object markObj = target.getClientProperty(MARK_KEY);
        if(markObj == null) {
            return 0;
        }
        int mark = (Integer) markObj;
        int length = target.getDocument().getLength();
        
        return (mark <= length) ? mark : length;
    }
}
