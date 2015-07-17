package org.apache.pdfbox.tools.pdfdebugger.fontencodingpane;

import java.io.IOException;
import javax.swing.JPanel;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;

/**
 * @author Khyrul Bashar
 */
class SimpleFont implements FontPane
{
    public static final String NO_GLYPH = "No glyph available";
    private int totalAvailableGlyph = 0;
    private FontEncodingView view;

    SimpleFont(PDSimpleFont font) throws IOException
    {
        String encodingName = getEncodingName(font);
        String fontName = font.getName();
        view = new FontEncodingView(getGlyphs(font), encodingName, fontName, totalAvailableGlyph, new String[] {"Code", "Glyph Name","Unicode Character"});
    }

    private Object[][] getGlyphs(PDSimpleFont font) throws IOException
    {
        Object[][] glyphs = new Object[256][3];

        for (int index = 0; index <= 255; index++)
        {
            glyphs[index][0] = index;
            if (font.getEncoding().contains(index))
            {
                glyphs[index][1] = font.getEncoding().getName(index);
                glyphs[index][2] = font.toUnicode(index);
                totalAvailableGlyph++;
            }
            else
            {
                glyphs[index][1] = NO_GLYPH;
                glyphs[index][2] = NO_GLYPH;
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

    public JPanel getPanel()
    {
        return view.getPanel();
    }
}
