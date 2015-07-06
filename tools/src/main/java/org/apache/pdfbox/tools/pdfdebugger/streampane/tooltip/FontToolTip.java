/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.pdfbox.tools.pdfdebugger.streampane.tooltip;

import java.io.IOException;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 * @author Khyrul Bashar
 */
public class FontToolTip extends ToolTip
{
    private String fontReferenceName;

    FontToolTip(PDResources resources, String rowText)
    {
        super(resources);
        fontReferenceName = extractFontReference(rowText);
        initUI();
    }

    private void initUI()
    {
        PDFont font = null;
        for (COSName name: resources.getFontNames())
        {
            if (name.getName().equals(fontReferenceName))
            {
                try
                {
                    font = resources.getFont(name);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        markup = "<html>"+font.getName()+"</html>";
    }

    private String extractFontReference(String rowText)
    {
        return rowText.split(" ")[0].substring(1);
    }
}
