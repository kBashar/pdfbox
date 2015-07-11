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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolTip;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;
import org.apache.pdfbox.tools.pdfdebugger.streampane.tooltip.ToolTipController;
import org.apache.pdfbox.tools.pdfdebugger.ui.textsearcher.Searcher;

/**
 * @author Khyrul Bashar
 */
class StreamTextView implements MouseMotionListener
{
    private ToolTipController tTController;

    private JPanel mainPanel;
    private JTextComponent textComponent;
    private Searcher searcher;

    StreamTextView(ToolTipController controller)
    {
        tTController = controller;
        searcher = new Searcher();
        initUI();
    }

    private void initUI()
    {
        mainPanel = new JPanel();

        textComponent = new JTextPane();
        textComponent.addMouseMotionListener(this);

        JScrollPane scrollPane = new JScrollPane(textComponent);

        BoxLayout boxLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

        mainPanel.setLayout(boxLayout);

        mainPanel.add(searcher.getSearchPanel());
        mainPanel.add(scrollPane);

        searcher.getSearchPanel().setVisible(false);

        final String SHOW_PANEL= "showPanel";
        KeyStroke showStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK);
        mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(showStroke, SHOW_PANEL);
        mainPanel.getActionMap().put(SHOW_PANEL, showAction);

        final String CLOSE_PANEL= "closePanel";
        KeyStroke closeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
        mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(closeStroke, CLOSE_PANEL);
        mainPanel.getActionMap().put(CLOSE_PANEL, closeAction);

    }

    public void setDocument(StyledDocument document)
    {
        textComponent.setDocument(document);
        searcher.setTextComponent(textComponent);
    }

    JComponent getView()
    {
        return mainPanel;
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent)
    {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent)
    {
        if (tTController != null)
        {
            int offset = textComponent.viewToModel(mouseEvent.getPoint());
            textComponent.setToolTipText(tTController.getToolTip(offset, textComponent));
        }
    }

    Action showAction = new AbstractAction()
    {
        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            searcher.getSearchPanel().setVisible(true);
            mainPanel.validate();
        }
    };

    Action closeAction = new AbstractAction()
    {
        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            searcher.getSearchPanel().setVisible(false);
        }
    };
}