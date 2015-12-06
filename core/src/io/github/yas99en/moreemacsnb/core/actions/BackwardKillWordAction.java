package io.github.yas99en.moreemacsnb.core.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;
import org.netbeans.editor.BaseDocument;


@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-BackwardKillWordAction")
public final class BackwardKillWordAction extends MoreEmacsAction {

    public BackwardKillWordAction() {
        super("backward-kill-word");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        if (!target.isEditable()) {
            return;
        }

        Document doc = target.getDocument();
        Caret caret = target.getCaret();
        int current = caret.getDot();
        int prev = BackwardWordAction.getPreviousWordPosition(doc, current);

        modifyAtomicAsUser(target, () -> {
            caret.moveDot(prev);
            target.cut();
        });
    }
}
