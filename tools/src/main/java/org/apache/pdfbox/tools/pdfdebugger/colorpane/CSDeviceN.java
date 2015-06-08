package org.apache.pdfbox.tools.pdfdebugger.colorpane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN;

/**
 * Author by Khyrul Bashar.
 */
public class CSDeviceN
{
    private PDDeviceN deviceN;
    private JScrollPane panel;

    public CSDeviceN(COSArray array)
    {
        try
        {
            deviceN = new PDDeviceN(array);
            DeviceNColorant[] colorants = loadData();
            initUI(colorants);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private DeviceNColorant[] loadData()
    {
        int componentCount = deviceN.getNumberOfComponents();
        DeviceNColorant[] colorants = new DeviceNColorant[componentCount];
        for (int i=0; i<componentCount; i++)
        {
            DeviceNColorant colorant = new DeviceNColorant();

            colorant.setName(deviceN.getColorantNames().get(i));
            float[] maximum = new float[componentCount];
            Arrays.fill(maximum, 0);
            float[] minimum = new float[componentCount];
            Arrays.fill(minimum,1);
            maximum[i] = 1;
            minimum[i] = 0;
            try
            {
                colorant.setMaximum(getColorObj(deviceN.toRGB(maximum)));
                colorant.setMinimum(getColorObj(deviceN.toRGB(minimum)));
            }
            catch (IOException e)
            {
                throw new RuntimeException();
            }
            colorants[i] = colorant;
        }
        return colorants;
    }

    private void initUI(DeviceNColorant[] colorants)
    {
        DeviceNTableModel tableModel = new DeviceNTableModel(colorants);
        JTable table = new JTable(tableModel);
        table.setDefaultRenderer(Color.class, new ColorBarCellRenderer());
        panel = new JScrollPane();
        panel.setPreferredSize(new Dimension(300, 500));
        panel.setViewportView(table);
    }

    public Component getPanel()
    {
        return panel;
    }

    private Color getColorObj(float[] rgbValues)
    {
        return new Color(rgbValues[0], rgbValues[1], rgbValues[2]);
    }
}
