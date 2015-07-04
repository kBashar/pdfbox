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

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
    private JTextField searchField;
    private JPanel panel;

    SearchPanel(ActionListener buttonListener, DocumentListener documentListener)
    {
        initUI(buttonListener, documentListener);
    }

    private void initUI(ActionListener buttonListener, DocumentListener documentListener)
    {
        nextButton = new JButton(NEXT);
        nextEnabled(false);
        nextButton.addActionListener(buttonListener);

        previousButton = new JButton(PREVIOUS);
        previousEnabled(false);
        previousButton.addActionListener(buttonListener);

        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(documentListener);

        panel = new JPanel(new FlowLayout());
        panel.setLayout(new FlowLayout());
        panel.add(searchField);
        panel.add(nextButton);
        panel.add(previousButton);
    }

    void nextEnabled(boolean value)
    {
        nextButton.setEnabled(value);
    }

    void previousEnabled(boolean value)
    {
        previousButton.setEnabled(value);
    }

    JPanel getPanel()
    {
        return panel;
    }
}
