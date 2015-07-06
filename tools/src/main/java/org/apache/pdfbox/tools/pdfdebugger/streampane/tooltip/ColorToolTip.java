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
class ColorToolTip extends ToolTip
{
    ColorToolTip(PDResources resources, String colorSpaceName, String rowText)
    {
        super(resources);
        createToolTip(colorSpaceName, rowText);
    }

    private void createToolTip(String colorSpaceName, String rowText)
    {
        colorSpaceName = colorSpaceName.substring(1).trim();
        PDColorSpace colorSpace = null;
        for (COSName name : resources.getColorSpaceNames())
        {
            if (name.getName().equals(colorSpaceName))
            {
                try
                {
                    colorSpace = resources.getColorSpace(name);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (colorSpace != null)
        {
            try
            {
                float[] rgbValues = colorSpace.toRGB(extractColorValues(rowText));
                Color color = new Color(rgbValues[0], rgbValues[1], rgbValues[2]);
                markup = getMarkUp(colorHexValue(color));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /*private void createToolTipForPattern(String rowText)
    {
        String patternName = ToolTipController.getWords(rowText).get(0).substring(1);
        System.out.println(patternName);
        PDAbstractPattern pattern = null;
        for (COSName name : resources.getPatternNames())
        {
            if (name.getName().equals(patternName))
            {
                try
                {
                    pattern = resources.getPattern(name);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (pattern != null)
        {
            try
            {
                float rgbValues = pattern.;
                Color color = new Color(rgbValues[0], rgbValues[1], rgbValues[2]);
                markup = getMarkUp(colorHexValue(color));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }*/

    private float[] extractColorValues(String rowtext)
    {
        ArrayList<String> words = ToolTipController.getWords(rowtext);
        words.remove(words.size()-1);
        float[] values = new float[words.size()];
        int index = 0;
        for (String word : words)
        {
            values[index++] = Float.parseFloat(word);
        }
        return values;
    }

    private static String colorHexValue(Color color)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%02x", color.getRed()));
        builder.append(String.format("%02x", color.getGreen()));
        builder.append(String.format("%02x", color.getBlue()));
        return builder.toString();
    }

    private String getMarkUp(String hexValue)
    {
         return  "<html>\n" +
                "<body bgcolor=#ffffff>\n" +
                "<div style=\"width:50px;height:20px;border:1px; background-color:#"+hexValue+";\"></div></body>\n" +
                "</html>";
    }
}
