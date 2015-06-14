package org.apache.pdfbox.tools.pdfdebugger.colorpane;

import java.awt.Color;

/**
 * Author by Khyrul Bashar.
 */
public class IndexedColorant
{
    private int index;
    private float[] rgbValues;

    public IndexedColorant(){}

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public void setRgbValues(float[] rgbValues)
    {
        this.rgbValues = rgbValues;
    }

    public Color getColor()
    {
        return new Color(rgbValues[0], rgbValues[1], rgbValues[2]);
    }

    public String getRGBValuesString()
    {
        StringBuilder builder = new StringBuilder();
        for (float i: rgbValues)
        {
            builder.append((int)(i*255));
            builder.append(", ");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        return builder.toString();
    }
}
