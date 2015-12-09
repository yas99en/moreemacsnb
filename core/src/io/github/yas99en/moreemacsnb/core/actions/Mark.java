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
