/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import org.netbeans.api.editor.EditorActionRegistration;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-DowncaseWordAction")
public class DowncaseWordAction extends ConvertWordAction {
    public DowncaseWordAction() {
        super("downcase-word");
    }

    @Override
    protected String convert(String word) {
        return word.toLowerCase();
    }
}
