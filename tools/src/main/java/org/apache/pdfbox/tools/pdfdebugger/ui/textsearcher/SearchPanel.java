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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
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
    private JButton nextButton;
    private JButton previousButton;
    private JCheckBox caseSensitive;
    private JTextField searchField;
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

        nextButton = new JButton();
        nextButton.setAction(nextButtonAction);

        previousButton = new JButton();
        previousButton.setAction(previousButtonAction);

        caseSensitive = new JCheckBox("Match case");
        caseSensitive.setSelected(false);
        caseSensitive.addChangeListener(changeListener);

        JButton cross = new JButton();
        cross.setAction(crossAction);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.add(searchField);
        panel.add(nextButton);
        panel.add(previousButton);
        panel.add(caseSensitive);
        panel.add(cross);

        final String SEARCH_NEXT = "showNext";
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F3"), SEARCH_NEXT);
        panel.getActionMap().put(SEARCH_NEXT, nextButtonAction);

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

    JPanel getPanel()
    {
        return panel;
    }

    void reset()
    {
        searchField.setText("");
    }
}
