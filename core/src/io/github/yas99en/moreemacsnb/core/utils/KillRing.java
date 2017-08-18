/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.utils;

import java.util.Stack;

/**
 *
 * @author tim
 */
public class KillRing {

    private static final KillRing INSTANCE = new KillRing();
    private final Stack<String> killRing = new Stack<>();
    private int idx = 0;
    private static final int MAX_KILL_RING_SIZE = 25;

    private KillRing() {
    }

    public static void kill(String str) {
        INSTANCE.kill_(str);
    }

    public static String yank() {
        return INSTANCE.yank_();
    }

    public static String yankAgain() {
        return INSTANCE.yankAgain_();
    }

    public static String lastYank() {
        return INSTANCE.lastYank_();
    }

    private void kill_(String str) {
        killRing.push(str);
        if (killRing.size() > MAX_KILL_RING_SIZE) {
            killRing.pop();
        }
    }

    private String yank_() {
        idx = 0;
        if (killRing.empty()) {
            return "";
        } else {
            return killRing.peek();
        }
    }

    private String lastYank_() {
        if (killRing.empty()) {
            return "";
        }
        int len = killRing.size();
        return killRing.get(len - 1 - idx);
    }

    private String yankAgain_() {
        idx = (idx + 1) % killRing.size();
        return lastYank_();
    }
}
