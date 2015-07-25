/*
 * Copyright 2015 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.pdfbox.tools.pdfdebugger.fontencodingpane;

import java.io.IOException;
import javax.swing.JPanel;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDCIDFontType2;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

interface FontPane
{
    JPanel getPanel();
}

/**
 * @author Khyrul Bashar
 */
public class FontEncodingPaneController
{
    private FontPane fontPane;

    public FontEncodingPaneController(COSName fontName, COSDictionary dictionary)
    {
        PDResources resources = new PDResources(dictionary);
        try
        {
            PDFont font = resources.getFont(fontName);
            if (font instanceof PDSimpleFont)
            {
                fontPane = new SimpleFont((PDSimpleFont)font);
            }
            else if (font instanceof PDType0Font && ((PDType0Font) font).getDescendantFont() instanceof PDCIDFontType2)
            {
                fontPane = new CIDFontType2((PDCIDFontType2) ((PDType0Font) font).getDescendantFont());
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
