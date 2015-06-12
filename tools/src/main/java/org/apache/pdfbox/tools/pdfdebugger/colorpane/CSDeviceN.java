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
package org.apache.pdfbox.tools.pdfdebugger.colorpane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Arrays;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.io.IOException;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN;


/**
 *@author  Khyrul Bashar.
 */

/**
 * A class that provides the necessary UI and functionalities to show the DeviceN color space.
 */
public class CSDeviceN
{
    private PDDeviceN deviceN;
    private JScrollPane panel;

    /**
     * Constructor
     * @param array COSArray instance that holds DeviceN color space
     */
    public CSDeviceN(COSArray array)
    {
        try
        {
            deviceN = new PDDeviceN(array);
            IndexdColorant[] colorants = getColorantData();
            initUI(colorants);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses the colorant data from the array and return.
     * @return
     */
    private IndexdColorant[] getColorantData()
    {
        int componentCount = deviceN.getNumberOfComponents();
        IndexdColorant[] colorants = new IndexdColorant[componentCount];
        for (int i=0; i<componentCount; i++)
        {
            IndexdColorant colorant = new IndexdColorant();

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

    private void initUI(IndexdColorant[] colorants)
    {
        DeviceNTableModel tableModel = new DeviceNTableModel(colorants);
        JTable table = new JTable(tableModel);
        table.setDefaultRenderer(Color.class, new ColorBarCellRenderer());
        table.setRowHeight(60);
        panel = new JScrollPane();
        panel.setPreferredSize(new Dimension(300, 500));
        panel.setViewportView(table);
    }

    /**
     * return the main panel that hold all the UI elements.
     * @return JPanel instance
     */
    public Component getPanel()
    {
        return panel;
    }

    private Color getColorObj(float[] rgbValues)
    {
        return new Color(rgbValues[0], rgbValues[1], rgbValues[2]);
    }
}
