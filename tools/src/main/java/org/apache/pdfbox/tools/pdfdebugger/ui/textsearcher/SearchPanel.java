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

package org.apache.pdfbox.tools.pdfdebugger.ui.textsearcher;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 * @author Khyrul Bashar
 */
class SearchPanel
{
    final static String NEXT = "Next";
    final static String PREVIOUS = "Previous";

    private JButton nextButton;
    private JButton previousButton;
    private JCheckBox caseSensitive;
    private JTextField searchField;
    private JPanel panel;

    SearchPanel(ActionListener buttonListener, DocumentListener documentListener, ChangeListener changeListener)
    {
        initUI(buttonListener, documentListener, changeListener);
    }

    private void initUI(ActionListener buttonListener, DocumentListener documentListener, ChangeListener changeListener)
    {
        nextButton = new JButton(NEXT);
        nextEnabled(false);
        nextButton.addActionListener(buttonListener);

        previousButton = new JButton(PREVIOUS);
        previousEnabled(false);
        previousButton.addActionListener(buttonListener);

        searchField = new JTextField();
        Action action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
               searchField.requestFocus();
            }
        };
        String key = "requestedFocus";
        searchField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F3"), key);
        searchField.getActionMap().put(key, action);

        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.getDocument().addDocumentListener(documentListener);

        caseSensitive = new JCheckBox("Case sens");
        caseSensitive.setSelected(false);
        caseSensitive.addChangeListener(changeListener);

        JPanel upperPanel = new JPanel(new FlowLayout());
        upperPanel.setLayout(new FlowLayout());
        upperPanel.add(searchField);
        upperPanel.add(nextButton);
        upperPanel.add(previousButton);

        JPanel lowerPanel = new JPanel();
        lowerPanel.add(caseSensitive);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(upperPanel);
        panel.add(lowerPanel);
    }

    void nextEnabled(boolean value)
    {
        nextButton.setEnabled(value);
    }

    void previousEnabled(boolean value)
    {
        previousButton.setEnabled(value);
    }

    boolean isCaseSensitive()
    {
        return caseSensitive.isSelected();
    }

    String getSearchWord()
    {
        return searchField.getText();
    }

    JPanel getPanel()
    {
        return panel;
    }
}
