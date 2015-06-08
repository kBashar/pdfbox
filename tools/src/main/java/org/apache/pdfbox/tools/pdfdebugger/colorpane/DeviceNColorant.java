package org.apache.pdfbox.tools.pdfdebugger.colorpane;

import java.awt.Color;

/**
 * Author by Khyrul Bashar.
 */
public class DeviceNColorant
{
    private String name;
    private Color maximum;
    private Color minimum;

    public DeviceNColorant()
    {
    }

    public DeviceNColorant(String colorantName, Color maxi, Color mini)
    {
        name = colorantName;
        maximum = maxi;
        minimum = mini;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Color getMaximum()
    {
        return maximum;
    }

    public void setMaximum(Color maximum)
    {
        this.maximum = maximum;
    }

    public Color getMinimum()
    {
        return minimum;
    }

    public void setMinimum(Color minimum)
    {
        this.minimum = minimum;
    }
}
