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

import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JPanel;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.font.PDCIDFontType2;

/**
 * @author Khyrul Bashar
 */
class CIDFontType2 implements FontPane
{
    FontEncodingView view;

    CIDFontType2(PDCIDFontType2 font) throws IOException
    {
        Object[][] cidtogid = readCIDToGIDMap(font);
        if (cidtogid != null)
        {
            Map<String, String> attributes = new LinkedHashMap<String, String>();
            attributes.put("Font", font.getName());
            attributes.put("CID count", Integer.toString(cidtogid.length));

            view = new FontEncodingView(cidtogid, attributes, new String[]{"CID", "GID"});
        }
    }

    private Object[][] readCIDToGIDMap(PDCIDFontType2 font) throws IOException
    {
        Object[][] cid2gid = null;
        COSDictionary dict = font.getCOSObject();
        COSBase map = dict.getDictionaryObject(COSName.CID_TO_GID_MAP);
        if (map instanceof COSStream)
        {
            COSStream stream = (COSStream) map;

            InputStream is = stream.getUnfilteredStream();
            byte[] mapAsBytes = IOUtils.toByteArray(is);
            IOUtils.closeQuietly(is);
            int numberOfInts = mapAsBytes.length / 2;
            cid2gid = new Object[numberOfInts][2];
            int offset = 0;
            for (int index = 0; index < numberOfInts; index++)
            {
                int gid = (mapAsBytes[offset] & 0xff) << 8 | mapAsBytes[offset + 1] & 0xff;
                cid2gid[index][0] = index;
                cid2gid[index][1] = gid;
                offset += 2;
            }
        }
        return cid2gid;
    }



    @Override
    public JPanel getPanel()
    {
        if (view != null)
        {
           return view.getPanel();
        }
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 500));
        return panel;
    }
}
