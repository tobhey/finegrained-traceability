package unisa.gps.etour.gui.operatoragency.documents;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class LimitedDocument extends PlainDocument {
    private static final long serialVersionUID = 1L;
    private int maxLength;

    public LimitedDocument(int pMaxLength) {
        maxLength = pMaxLength;
    }

    public void insertString(int pOffset, String pString, AttributeSet pAttribute) throws BadLocationException {
        if (null == pString) {
            return;
        }
// Note: the content is always a newline at the end
        int capacity = maxLength + 1 - getContent().length();
// If the maximum length is greater than or equal to the String, the part.
        if (capacity >= pString.length()) {
            super.insertString(pOffset, pString, pAttribute);
        }
// Otherwise add what may
        else {
            if (capacity > 0) {
                super.insertString(pOffset, pString.substring(0, capacity), pAttribute);
            }
        }
    }
}
