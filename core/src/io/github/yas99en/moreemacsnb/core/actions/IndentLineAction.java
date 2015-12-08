/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import io.github.yas99en.moreemacsnb.core.utils.CodePointIterator;
import io.github.yas99en.moreemacsnb.core.utils.DocumentCharSequence;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;
import org.netbeans.editor.BaseKit;
import org.netbeans.editor.Utilities;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-IndentLineAction")
public class IndentLineAction extends MoreEmacsAction {
    public IndentLineAction() {
        super("indent-line");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        if (!target.isEditable() || !target.isEnabled()) {
            target.getToolkit().beep();
            return;
        }

        BaseKit kit = Utilities.getKit(target);
        if(kit == null) {
            target.getToolkit().beep();
            return;
        }

        Action action = kit.getActionByName(BaseKit.reindentLineAction);
        if (action == null) {
            target.getToolkit().beep();
            return;
        }

        action.actionPerformed(e);
        moveCaret(target);
    }
    
    private static void moveCaret(JTextComponent target) {
        Caret caret = target.getCaret();
        int dot = caret.getDot();
        
        Document doc = target.getDocument();
        Element rootElem = doc.getDefaultRootElement();
        int row = rootElem.getElementIndex(dot);
        Element line = rootElem.getElement(row);
        
        int length = line.getEndOffset() - line.getStartOffset() - 1;
        System.out.println("length:" +length);
        DocumentCharSequence seq = new DocumentCharSequence(doc, line.getStartOffset(), length);
        CodePointIterator itr = new CodePointIterator(seq);
        for(; itr.hasNext(); ) {
            int index = itr.index();
            int cp = itr.next();
            if(Character.isWhitespace(cp)) {
                continue;
            }
            if(dot >= line.getStartOffset() + index) {
                return;
            }
            itr.previous();
            break;
        }
        caret.setDot(line.getStartOffset() + itr.index());
    }
}
