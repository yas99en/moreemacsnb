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

import io.github.yas99en.moreemacsnb.core.utils.CodePointIterator;
import io.github.yas99en.moreemacsnb.core.utils.ColumnUtils;
import io.github.yas99en.moreemacsnb.core.utils.DocumentCharSequence;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;


@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-YankRectangleAction")
public final class YankRectangleAction extends MoreEmacsAction {

    public YankRectangleAction() {
        super("yank-rectangle");
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

        List<String> rectangle = RectangleStorage.getRectangle();
        if(rectangle == null) {
            return;
        }
        int current = caret.getDot();
        Element rootElem = doc.getDefaultRootElement();
        int row = rootElem.getElementIndex(current);
        int column = ColumnUtils.getColumn(doc, current, getTabStop());

        modifyAtomicAsUser(target, () -> {
            try {
                ensureLines(doc, row + rectangle.size());
                int offset = yankRectangle(caret, doc, row, column, rectangle);
                caret.setDot(offset);
            } catch (BadLocationException ex) {
                throw new IndexOutOfBoundsException(ex.getMessage());
            }
        });
    }

    private void ensureLines(Document doc, int lines) throws BadLocationException {
        Element rootElem = doc.getDefaultRootElement();
        int n = lines - rootElem.getElementCount();
        if(n <= 0) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        String delim = "\n";
        for(int i = 0; i < n; i++) {
            builder.append(delim);
        }
        doc.insertString(doc.getLength(), builder.toString(), null);
    }

    private int yankRectangle(Caret caret, Document doc,
            int row, int column, List<String> rectangle)
            throws BadLocationException {
        int offset = caret.getDot();
        for(int i = 0; i < rectangle.size(); i++) {
            offset = yankString(doc, row+i, column, rectangle.get(i));
        }
        return offset;
    }

    private int yankString(Document doc, int row, int column, String str)
    throws BadLocationException
    {
        Element rootElem = doc.getDefaultRootElement();
        Element line = rootElem.getElement(row);
        int col = 0;

        CharSequence seq = new DocumentCharSequence(doc, line.getStartOffset(), line.getEndOffset() - line.getStartOffset() - 1);
        
        for(CodePointIterator itr = new CodePointIterator(seq); itr.hasNext(); ) {
            int offset = line.getStartOffset() + itr.index();
            int codePoint = itr.next();
            if(col >= column) {
                doc.insertString(offset, str, null);
                return offset+str.length();
            }
            col = ColumnUtils.getNextColumn(col, codePoint, getTabStop());
        }
        

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < column-col; i++) {
            builder.append(" ");
        }
        builder.append(str);
        doc.insertString(line.getEndOffset()-1, builder.toString(), null);
        return line.getEndOffset()-1+builder.length();
    }

    private int getTabStop() {
        return 4;
    }
}
