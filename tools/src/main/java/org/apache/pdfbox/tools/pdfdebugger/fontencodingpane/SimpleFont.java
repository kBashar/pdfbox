package org.apache.pdfbox.tools.pdfdebugger.fontencodingpane;

import javax.swing.JPanel;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;

/**
 * @author Khyrul Bashar
 */
class SimpleFont
{
    public static final String NO_GLYPH = "No glyph available";
    private int totalAvailableGlyph = 0;
    private FontEncodingView view;

    SimpleFont(PDSimpleFont font)
    {
        String encodingName = getEncodingName(font);
        String fontName = font.getName();
        view = new FontEncodingView(getGlyphs(font.getEncoding()), encodingName, fontName, totalAvailableGlyph);
    }

    private Object[][] getGlyphs(Encoding encoding)
    {
        Object[][] glyphs = new Object[256][2];

        for (int index = 0; index <= 255; index++ )
        {
            glyphs[index][0] = index;
            if (encoding.contains(index))
            {
                glyphs[index][1] = encoding.getName(index);
                totalAvailableGlyph++;
            }
            else
            {
                glyphs[index][1] = NO_GLYPH;
            }
        }
        return glyphs;
    }

    private String getEncodingName(PDSimpleFont font)
    {
        String name = font.getCOSObject().getNameAsString(COSName.ENCODING);
        if (name == null)
        {
            name = "DictionaryEncoding";
        }
        return name;
    }

    JPanel getPanel()
    {
        return view.getPanel();
    }
}
