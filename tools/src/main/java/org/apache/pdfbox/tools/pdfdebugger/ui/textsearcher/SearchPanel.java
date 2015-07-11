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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 * @author Khyrul Bashar
 */
class SearchPanel
{
    private JCheckBox caseSensitive;
    private JTextField searchField;
    private JLabel counterLabel;
    private JPanel panel;

    SearchPanel(DocumentListener documentListener, ChangeListener changeListener,
                ComponentListener compListener, Action nextButtonAction, Action previousButtonAction)
    {
        initUI(documentListener, changeListener, compListener, nextButtonAction, previousButtonAction);
    }

    private void initUI(DocumentListener documentListener, ChangeListener changeListener,
                        ComponentListener compListener, Action nextButtonAction, Action previousButtonAction)
    {
        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(documentListener);

        counterLabel = new JLabel();
        counterLabel.setVisible(false);
        counterLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));

        JButton nextButton = new JButton();
        nextButton.setAction(nextButtonAction);

        JButton previousButton = new JButton();
        previousButton.setAction(previousButtonAction);

        caseSensitive = new JCheckBox("Match case");
        caseSensitive.setSelected(false);
        caseSensitive.addChangeListener(changeListener);

        JButton cross = new JButton();
        cross.setAction(crossAction);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(searchField);
        panel.add(counterLabel);
        panel.add(nextButton);
        panel.add(previousButton);
        panel.add(caseSensitive);
        panel.add(cross);

        final String SEARCH_NEXT = "showNext";
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F3"), SEARCH_NEXT);
        panel.getActionMap().put(SEARCH_NEXT, nextButtonAction);

        final String SEARCH_PREVIOUS = "showPrevious";
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.SHIFT_DOWN_MASK), SEARCH_PREVIOUS);
        panel.getActionMap().put(SEARCH_PREVIOUS, previousButtonAction);

        panel.addComponentListener(compListener);
    }

    Action crossAction = new AbstractAction("X")
    {
        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            panel.setVisible(false);
        }
    };

    boolean isCaseSensitive()
    {
        return caseSensitive.isSelected();
    }

    String getSearchWord()
    {
        return searchField.getText();
    }

    void reset()
    {
        counterLabel.setVisible(false);
    }

    void updateCounterLabel(int now, int total)
    {
        if (!counterLabel.isVisible())
        {
            counterLabel.setVisible(true);
        }
        if (total == 0)
        {
            counterLabel.setText("No match found");
            return;
        }
        counterLabel.setText(" " + now + " of " + total + " ");
    }

    JPanel getPanel()
    {
        return panel;
    }

    public void reFocus()
    {
        searchField.requestFocus();
        String searchKey = searchField.getText();
        searchField.setText(searchKey);
        searchField.setSelectionStart(0);
        searchField.setSelectionEnd(searchField.getText().length());
    }
}
