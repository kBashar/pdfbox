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

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

/**
 * @author Khyrul Bashar
 */
abstract class ColorToolTip implements ToolTip
{
    String markup;

    float[] extractColorValues(String rowtext)
    {
        ArrayList<String> words = ToolTipController.getWords(rowtext);
        words.remove(words.size()-1);
        float[] values = new float[words.size()];
        int index = 0;
        try
        {
            for (String word : words)
            {
                values[index++] = Float.parseFloat(word);
            }
        }
        catch (NumberFormatException e)
        {
            return null;
        }
        return values;
    }

    static String colorHexValue(Color color)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%02x", color.getRed()));
        builder.append(String.format("%02x", color.getGreen()));
        builder.append(String.format("%02x", color.getBlue()));
        return builder.toString();
    }

    String getMarkUp(String hexValue)
    {
         return  "<html>\n" +
                "<body bgcolor=#ffffff>\n" +
                "<div style=\"width:50px;height:20px;border:1px; background-color:#"+hexValue+";\"></div></body>\n" +
                "</html>";
    }

    @Override
    public String getToolTipText()
    {
        return markup;
    }
}
