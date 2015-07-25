package org.apache.pdfbox.tools.pdfdebugger.fontencodingpane;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JPanel;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;

/**
 * @author Khyrul Bashar
 */
class SimpleFont implements FontPane
{
    public static final String NO_GLYPH = "No glyph";
    private int totalAvailableGlyph = 0;
    private FontEncodingView view;

    SimpleFont(PDSimpleFont font) throws IOException
    {
        Object[][] tableData = getGlyphs(font);

        Map<String, String> attributes = new LinkedHashMap<String, String>();
        attributes.put("Encoding", getEncodingName(font));
        attributes.put("Font", font.getName());
        attributes.put("Glyph count", Integer.toString(totalAvailableGlyph));

        view = new FontEncodingView(tableData, attributes, new String[] {"Code", "Glyph Name","Unicode Character"});
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
        return font.getEncoding().getClass().getSimpleName();
    }

    public JPanel getPanel()
    {
        return view.getPanel();
    }
}
