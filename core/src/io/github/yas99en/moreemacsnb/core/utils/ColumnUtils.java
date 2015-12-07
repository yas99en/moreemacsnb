package io.github.yas99en.moreemacsnb.core.utils;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;


public final class ColumnUtils {
    private ColumnUtils() {}
    
    public static int getColumn(Document doc, int offset, int tabStop)
    throws BadLocationException {
        Element rootElem = doc.getDefaultRootElement();
        Element line = rootElem.getElement(rootElem.getElementIndex(offset));
        int column = 0;
        
        CharSequence seq = new DocumentCharSequence(doc, line.getStartOffset(), offset - line.getStartOffset());
        for(CodePointIterator itr = new CodePointIterator(seq); itr.hasNext(); ) {
            int codePoint = itr.next();
            column = getNextColumn(column, codePoint, tabStop);
        }
        
        return column;
    }
    
    public static int getNextColumn(int column, int codePoint, int tabStop) {
        if(codePoint == '\t') {
            return column - (column%tabStop) + tabStop;
        } else {
            return column + CharacterUtils.getWidth(codePoint);
        }
    }
}
