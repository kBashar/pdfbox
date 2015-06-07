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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Hashtable;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.pdmodel.graphics.color.PDSeparation;

/**
 * @author Khyrul Bashar.
 */

/**
 *A class that provides the necessary UI and functionalities to show the Separation color space.
 */
public class CSSeparation implements ChangeListener, ActionListener
{
    private JSlider slider;
    private JTextField tintField;
    private JLabel colorBar;
    private JPanel panel;

    private PDSeparation separation;
    private float tintValue = 1;

    /**
     * Constructor
     * @param array COSArray instance of the separation color space.
     */
    public CSSeparation(COSArray array)
    {
        try
        {
            separation = new PDSeparation(array);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        initUI();
        initValues();
    }

    /**
     * initialize all the UI elements and arrange them.
     */
    private void initUI()
    {
        Font boldFont = new Font("Monospaced", Font.BOLD, 20);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        JPanel inputPanel = new JPanel(new GridBagLayout());

        slider = new JSlider(0, 100, 50);
        slider.setMajorTickSpacing(50);
        slider.setPaintTicks(true);

        Hashtable labelTable = new Hashtable();
        JLabel lightest = new JLabel("lightest");
        lightest.setFont(new Font("Monospaced", Font.BOLD, 10));
        JLabel darkest = new JLabel("darkest");
        darkest.setFont(new Font("Monospaced", Font.BOLD, 10));
        JLabel midPoint = new JLabel("0.5");
        midPoint.setFont(new Font("Monospaced", Font.BOLD, 10));
        labelTable.put(new Integer(0), lightest);
        labelTable.put(new Integer(50), midPoint);
        labelTable.put(new Integer(100), darkest);

        slider.setPaintLabels(true);
        slider.setLabelTable(labelTable);
        slider.addChangeListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 10;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(slider, gbc);

        JLabel tintLabel = new JLabel("Tint Value:");
        tintLabel.setFont(boldFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(tintLabel, gbc);

        tintField = new JTextField();
        tintField.addActionListener(this);
        tintField.setPreferredSize(new Dimension(10, 30));
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(tintField, gbc);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.gridwidth = 2;
        gbc2.weightx = 0.3;
        gbc2.weighty = 1;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(inputPanel, gbc2);

        colorBar = new JLabel();
        colorBar.setOpaque(true);
        gbc2.gridx = 2;
        gbc2.weightx = 0.7;
        gbc2.gridwidth = 4;
        gbc2.gridheight = 2;
        gbc2.fill = GridBagConstraints.BOTH;
        contentPanel.add(colorBar, gbc2);
        setColorBarBorder();

        panel = new JPanel(new GridBagLayout());

        JLabel colorantNameLabel = new JLabel("Colorant: " + separation.getColorantName());
        colorantNameLabel.setFont(boldFont);

        GridBagConstraints maingbc = new GridBagConstraints();
        maingbc.gridx = 0;
        maingbc.gridy = 0;
        maingbc.weightx = 1;
        maingbc.weighty = 0.03;
        maingbc.anchor = GridBagConstraints.FIRST_LINE_START;
        panel.add(colorantNameLabel, maingbc);

        maingbc.gridx = 0;
        maingbc.gridy = 1;
        maingbc.weighty = 0.97;
        maingbc.gridwidth = 10;
        maingbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(contentPanel, maingbc);

    }

    private void initValues()
    {
        slider.setValue(getIntRepresentation(tintValue));
        tintField.setText(Float.toString(tintValue));
    }

    /**
     * return the main panel that hold all the UI elements.
     * @return JPanel instance
     */
    public JPanel getPanel()
    {
        return panel;
    }

    /**.
     * input changed in slider.
     * @param changeEvent
     */
    @Override
    public void stateChanged(ChangeEvent changeEvent)
    {
        int value = slider.getValue();
        tintValue = getFloatRepresentation(value);
        tintField.setText(Float.toString(tintValue));
        updateColorBar();
    }

    /**
     * input changed in text field.
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        String input = tintField.getText();
        try
        {
            tintValue= Float.parseFloat(input);
            slider.setValue(getIntRepresentation(tintValue));
            updateColorBar();
        }
        catch (NumberFormatException e)
        {
            tintField.setText(Float.toString(tintValue));
        }
    }

    private void updateColorBar()
    {
        try
        {
            float[] rgbValues = separation.toRGB(new float[] {tintValue});
            colorBar.setBackground(new Color(rgbValues[0], rgbValues[1], rgbValues[2]));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set a little border around colorbar. color of the border is the darkest of the colorant.
     */
    private void setColorBarBorder()
    {
        try
        {
            float[] rgbValues = separation.toRGB(new float[] {1});
            Color darkest= new Color(rgbValues[0], rgbValues[1], rgbValues[2]);
            colorBar.setBorder(new BevelBorder(BevelBorder.LOWERED, darkest, darkest));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private float getFloatRepresentation(int value)
    {
        return (float) value/100;
    }

    private int getIntRepresentation(float value)
    {
        return (int) (value*100);
    }
}
