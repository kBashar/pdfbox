package org.apache.pdfbox.tools.pdfdebugger.stringpane;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.tools.pdfdebugger.hexviewer.HexView;
import org.apache.pdfbox.tools.util.Util;

/**
 * @author Khyrul Bashar
 */
public class StringPane
{
    private static final String TEXT_TAB = "Text View";
    private static final String HEX_TAB = "Hex view";

    JTabbedPane tabbedPane;

    public StringPane(COSString cosString)
    {
        tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(Util.RIGHT_PANE_PREFERRED_DIMENSION);
        tabbedPane.addTab(TEXT_TAB, createTextView(cosString));
        tabbedPane.addTab(HEX_TAB, createHexView(cosString));
    }

    private JTextPane createTextView(COSString cosString)
    {
        JTextPane textPane = new JTextPane();
        textPane.setText(getTextString(cosString));
        textPane.setEditable(false);

        return textPane;
    }

    private JComponent createHexView(COSString cosString)
    {
        HexView hexView = new HexView(cosString.getBytes());
        return hexView.getPane();
    }

    private String getTextString(COSString cosString)
    {
        String text = cosString.getString();
        for (char c : text.toCharArray())
        {
            if (Character.isISOControl(c))
            {
                text = "<" + cosString.toHexString() + ">";
                break;
            }
        }
        return  "" + text;
    }

    public JTabbedPane getPane()
    {
        return tabbedPane;
    }
}
