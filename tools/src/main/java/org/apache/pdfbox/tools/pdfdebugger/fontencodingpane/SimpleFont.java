package org.apache.pdfbox.tools.pdfdebugger.fontencodingpane;

import javax.swing.JPanel;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;

/**
 * @author Khyrul Bashar
 */
class SimpleFont
{
    private FontEncodingView view;

    SimpleFont(PDSimpleFont font)
    {
        view = new FontEncodingView(getGlyphs(font.getEncoding()), new String[] {"Code", "Glyph"});
    }

    private Object[][] getGlyphs(Encoding encoding)
    {
        Object[][] glyphs = new Object[256][2];

        for (int index = 0; index <= 255; index++ )
        {
            glyphs[index][0] = index;
            glyphs[index][1] = encoding.getName(index);
        }
        return glyphs;
    }

    JPanel getPanel()
    {
        return view.getPanel();
    }
}
