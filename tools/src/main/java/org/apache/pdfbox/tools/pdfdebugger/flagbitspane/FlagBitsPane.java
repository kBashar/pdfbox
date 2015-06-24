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

package org.apache.pdfbox.tools.pdfdebugger.flagbitspane;

/**
 * @author Khyrul Bashar
 */

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;

/**
 * A class that displays flag bits found in many Flags entry in PDF document's dictionaries
 * detail whether a particular bit is set or unset.
 */
public class FlagBitsPane
{
    private final static String[] columnNames = {"Bit\nPosition", "Name", "Status"};
    private JPanel panel;

    public FlagBitsPane(COSDictionary dictionary)
    {
        initUI(dictionary);
    }

    private void initUI(final COSDictionary dictionary)
    {
        Object[][] flagBits = getFlagBits(dictionary);

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(300, 500));

        JLabel flagLabel = new JLabel(getFlagType(dictionary) + " Flags");
        flagLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        flagLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        JPanel flagLabelPanel = new JPanel();
        flagLabelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        flagLabelPanel.add(flagLabel);

        JLabel flagValueLabel = new JLabel("Flags: " + dictionary.getInt(COSName.FLAGS));
        flagValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        flagValueLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        JTable table = new JTable(flagBits,columnNames );
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        Box box = Box.createVerticalBox();
        box.add(flagValueLabel);
        box.add(scrollPane);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;

        panel.add(flagLabelPanel, gbc);

        gbc.gridy = 2;
        gbc.weighty=0.9;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.BELOW_BASELINE;

        panel.add(box, gbc);
    }

    private String getFlagType(final COSDictionary dictionary)
    {
        if (dictionary.getCOSName(COSName.TYPE).equals(COSName.FONT_DESC))
        {
            return "Font";
        }
        //TODO Type key is not Required field in the dictionary. So we need a better way to Identify.
        if (dictionary.getCOSName(COSName.TYPE).equals(COSName.ANNOT))
        {
            return "Annot:"+dictionary.getCOSName(COSName.SUBTYPE).getName();
        }
        return null;
    }

    private Object[][] getFlagBits(final COSDictionary dictionary)
    {
        if (dictionary.getCOSName(COSName.TYPE).equals(COSName.FONT_DESC))
        {
            return getFontFlagBits(dictionary);
        }
        if (dictionary.getCOSName(COSName.TYPE).equals(COSName.ANNOT))
        {
            return getAnnotFlagBits(dictionary);
        }
        return null;
    }

    private Object[][] getAnnotFlagBits(COSDictionary dictionary)
    {
        PDAnnotation annotation = new PDAnnotation(dictionary){};
        return new Object[][]{
                new Object[] {1, "Invisible", annotation.isInvisible()},
                new Object[] {2,"Hidden", annotation.isHidden()},
                new Object[] {3, "Print", annotation.isPrinted()},
                new Object[] {4, "NoZoom", annotation.isNoZoom()},
                new Object[] {5, "NoRotate", annotation.isNoRotate()},
                new Object[] {6, "NoView", annotation.isNoView()},
                new Object[] {7, "ReadOnly", annotation.isReadOnly()},
                new Object[] {8, "Locked", annotation.isLocked()},
                new Object[] {9, "ToggleNoView", annotation.isToggleNoView()},
                new Object[] {10, "LockedContents", annotation.isLocked()}
        };
    }

    private Object[][] getFontFlagBits(final COSDictionary dictionary)
    {
        PDFontDescriptor fontDesc = new PDFontDescriptor(dictionary);
        return new Object[][]{
                new Object[] {1, "FixedPitch", fontDesc.isFixedPitch()},
                new Object[] {2, "Serif", fontDesc.isSerif()},
                new Object[] {3, "Symbolic", fontDesc.isSymbolic()},
                new Object[] {4, "Script", fontDesc.isScript()},
                new Object[] {6, "NonSymbolic", fontDesc.isNonSymbolic()},
                new Object[] {7, "Italic", fontDesc.isItalic()},
                new Object[] {17, "AllCap", fontDesc.isAllCap()},
                new Object[] {18, "SmallCap", fontDesc.isSmallCap()},
                new Object[] {19, "ForceBold", fontDesc.isForceBold()}
        };
    }

    public JPanel getPanel()
    {
        return panel;
    }

    /*private class FlagBit
    {
        private final int bitPosition;
        private final String name;
        private final boolean state;

        FlagBit(int bp, String name,boolean state)
        {
            this.bitPosition = bp;
            this.name = name;
            this.state = state;
        }

        public int getBitPosition()
        {
            return bitPosition;
        }

        public String getName()
        {
            return name;
        }

        public boolean isState()
        {
            return state;
        }
    }*/
}
