package org.apache.pdfbox.tools.pdfdebugger.fontencodingpane;

import java.io.IOException;
import javax.swing.JPanel;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;

/**
 * @author Khyrul Bashar
 */
public class FontEncodingPane
{
    private JPanel panel;
    private SimpleFont fontPane;

    public FontEncodingPane(COSName fontName, COSDictionary dictionary)
    {
        PDResources resources = new PDResources(dictionary);
        try
        {
            PDFont font = resources.getFont(fontName);
            if (font instanceof PDSimpleFont)
            {
                fontPane = new SimpleFont((PDSimpleFont)font);
            }
            else
            {
                System.out.println("not");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public JPanel getPane()
    {
        if (fontPane != null)
        {
            return fontPane.getPanel();
        }
        return null;
    }
}
