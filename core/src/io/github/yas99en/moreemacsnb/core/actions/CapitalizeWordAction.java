/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import io.github.yas99en.moreemacsnb.core.utils.CodePointIterator;
import org.netbeans.api.editor.EditorActionRegistration;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-CapitalizeWordAction")
public class CapitalizeWordAction extends ConvertWordAction {
    public CapitalizeWordAction() {
        super("capitalize-word");
    }

    @Override
    protected String convert(String word) {
        StringBuilder builder = new StringBuilder();
        for(CodePointIterator itr = new CodePointIterator(word); itr.hasNext(); ) {
            int cp = itr.next();
            
            if(!Character.isLetter(cp)) {
                builder.appendCodePoint(cp);
                continue;
            }

            builder.appendCodePoint(Character.toUpperCase(cp));
            if(itr.hasNext()) {
                builder.append(word.substring(itr.index()).toLowerCase());
            }
            break;
        }
        return builder.toString();
    }
}
