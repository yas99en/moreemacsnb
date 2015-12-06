package io.github.yas99en.moreemacsnb.core.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;


@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-KillWordAction")
public final class KillWordAction extends MoreEmacsAction {

    public KillWordAction() {
        super("kill-word");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        if (!target.isEditable()) {
            return;
        }

        Document doc = target.getDocument();
        Caret caret = target.getCaret();
        int current = caret.getDot();
        int next = ForwardWordAction.getNextWordPosition(doc, current);
        caret.moveDot(next);
        target.cut();
    }
}
