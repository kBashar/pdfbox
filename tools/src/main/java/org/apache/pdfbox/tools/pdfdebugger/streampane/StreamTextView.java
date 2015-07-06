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
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolTip;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;
import org.apache.pdfbox.tools.pdfdebugger.streampane.tooltip.ToolTipController;

/**
 * @author Khyrul Bashar
 */
class StreamTextView implements MouseMotionListener
{
    private JComponent toolTipContent;
    private ToolTipController tTController;

    private JScrollPane scrollPane;
    JTextComponent textComponent;

    StreamTextView(ToolTipController controller)
    {
        tTController = controller;
        initUI();
    }

    private void initUI()
    {
        textComponent = new JTextPane();
        textComponent.addMouseMotionListener(this);
        scrollPane = new JScrollPane(textComponent);
        scrollPane.setPreferredSize(new Dimension(300, 400));
    }

    public void setDocument(StyledDocument document)
    {
        textComponent.setDocument(document);
    }

    JTextComponent getTextComponent()
    {
        return textComponent;
    }

    JComponent getView()
    {
        return scrollPane;
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
}