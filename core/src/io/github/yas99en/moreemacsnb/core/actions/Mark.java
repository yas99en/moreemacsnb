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
            put(target, caret.getMark());
            return;
        }

        Object markObj = target.getClientProperty(MARK_KEY);
        if(markObj == null || !(markObj instanceof Integer)) {
            put(target, 0);
            return;
        }

        int mark = (Integer) markObj;
        if(mark < 0) {
            put(target, 0);
            return;
        }

        int length = target.getDocument().getLength();
        if(mark > length) {
            put(target, length);
            return;
        }
    }

    private static void put(JTextComponent target, int mark) {
        target.putClientProperty(MARK_KEY, mark);
    }

    public static void set(JTextComponent target, int mark) {
        put(target, mark);
        update(target);
    }

    public static int get(JTextComponent target) {
        update(target);
        return (Integer) target.getClientProperty(MARK_KEY);
    }
}
