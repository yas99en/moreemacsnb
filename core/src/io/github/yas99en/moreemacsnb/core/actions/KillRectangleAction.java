package io.github.yas99en.moreemacsnb.core.actions;

import io.github.yas99en.moreemacsnb.core.utils.CodePointIterator;
import io.github.yas99en.moreemacsnb.core.utils.ColumnUtils;
import io.github.yas99en.moreemacsnb.core.utils.DocumentCharSequence;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;


@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-KillRectangleAction")
public final class KillRectangleAction extends MoreEmacsAction {

    public KillRectangleAction() {
        super("kill-rectangle");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        try {
            doActionPerformed(e, target);
        } catch (BadLocationException ex) {
            throw new IndexOutOfBoundsException(ex.getMessage());
        }
    }
    public void doActionPerformed(ActionEvent e, JTextComponent target) throws BadLocationException {
        if (!target.isEditable() || !target.isEnabled()) {
            target.getToolkit().beep();
            return;
        }
        
        Document doc = target.getDocument();
        Caret caret = target.getCaret();
        
        int dot = caret.getDot();
        int mark = Mark.get(target);
        
        int start = (dot <= mark) ? dot : mark;
        Element rootElem = doc.getDefaultRootElement();
        int startRow = rootElem.getElementIndex(start);
        int startColumn = ColumnUtils.getColumn(doc, start, getTabStop());
        int end = (dot > mark) ? dot : mark;
        int endRow = rootElem.getElementIndex(end);
        int endColumn = ColumnUtils.getColumn(doc, end, getTabStop());
        
        int leftColumn  = (startColumn <  endColumn) ? startColumn : endColumn;
        int rightColumn = (startColumn >= endColumn) ? startColumn : endColumn;

        modifyAtomicAsUser(target, ()->{
            try {
                List<String> rectangle = new ArrayList<>();
                int offset = killRectangle(caret, doc, startRow, leftColumn, endRow, rightColumn, rectangle);
                RectangleStorage.setRectangle(rectangle);
                caret.setDot(offset);
            } catch (BadLocationException ex) {
                throw new IndexOutOfBoundsException(ex.getMessage());
            }
        });
    }

private int killRectangle(Caret caret, Document doc,
        int startRow, int startColumn,
        int endRow, int endColumn, List<String> rectangle)
        throws BadLocationException {
    
    
        int result = caret.getDot();
        for(int i = startRow; i <= endRow; i++) {
            result = killString(doc, i, startColumn, endColumn, rectangle);
        }
        return result;
    }
        
    private int killString(Document doc, int row,
            int startColumn, int endColumn, List<String> rectangle) throws BadLocationException {
        Element rootElem = doc.getDefaultRootElement();
        Element line = rootElem.getElement(row);

        StringBuilder builder = new StringBuilder();
        int column = 0;
        int cutOffset = 0;
        int cutLength = 0;
        
        CharSequence seq = new DocumentCharSequence(doc, line.getStartOffset(), line.getEndOffset()-line.getStartOffset()-1);
        
        for(CodePointIterator itr = new CodePointIterator(seq); itr.hasNext(); ) {
            if(column >= endColumn) {
                break;
            }
            int offset = line.getStartOffset() + itr.index();
            int codePoint = itr.next();

            int nextColumn = ColumnUtils.getNextColumn(column, codePoint, getTabStop());
            
            if(nextColumn < startColumn+1) {
                column = nextColumn;
                continue;
            }
            if(cutLength == 0) { 
                cutOffset = offset;
            }
            builder.appendCodePoint(codePoint);
            cutLength += Character.charCount(codePoint);
            column = nextColumn;
        }
        
        doc.remove(cutOffset, cutLength);

            
        for(int i = 0; i < endColumn-column; i++) {
            builder.append(' ');
        }
        
        rectangle.add(builder.toString());
        return cutOffset;
    }

    private int getTabStop() {
        return 4;
    }
}
