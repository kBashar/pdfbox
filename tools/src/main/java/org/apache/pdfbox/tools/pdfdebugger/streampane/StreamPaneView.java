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
package org.apache.pdfbox.tools.pdfdebugger.streampane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridBagLayoutInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.print.Doc;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.tools.pdfdebugger.streampane.tooltip.ToolTipController;
import org.apache.pdfbox.tools.pdfdebugger.ui.textsearcher.Searcher;

/**
 * @author Khyrul Bashar
 */
class StreamPaneView extends JPanel
{
    private JPanel headerPanel;
    private JPanel contentPanel;
    private Searcher searcher;

    StreamPaneView(boolean isImage, String[] filterTypes, String i, ActionListener listener)
    {;
        searcher = new Searcher();

        headerPanel = createHeaderPanel(filterTypes, i, listener);
        contentPanel = new JPanel(new BorderLayout());
        initUI();
    }

    void showStreamText(StyledDocument document, ToolTipController toolTipController)
    {
        contentPanel.removeAll();

        //TODO change StreamTextView Constructor implementation
        StreamTextView textView = new StreamTextView(toolTipController);
        textView.setDocument(document);
        searcher.setTextComponent(textView.textComponent);
        contentPanel.add(textView.getView(), BorderLayout.CENTER);

        this.validate();
    }

    void showStreamImage(BufferedImage image)
    {
        contentPanel.removeAll();
        contentPanel.add(new StreamImageView(image).getView(), BorderLayout.CENTER);

        this.validate();
    }

    private JPanel createHeaderPanel(String[] availableFilters, String i, ActionListener actionListener)
    {
        JComboBox filters = new JComboBox<String>(availableFilters);
        filters.setSelectedItem(i);
        filters.addActionListener(actionListener);

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(searcher.getSearchPanel());
        panel.add(filters);

        return panel;
    }

    public JPanel getStreamPanel()
    {
        return this;
    }

    private void initUI()
    {
        this.setPreferredSize(new Dimension(300, 500));
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(headerPanel, gbc);

        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 0.9;
        contentPanel.setBorder(new BevelBorder(BevelBorder.RAISED, Color.DARK_GRAY, Color.DARK_GRAY));
        this.add(contentPanel, gbc);
    }
}
