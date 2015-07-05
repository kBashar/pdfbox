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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;

/**
 * @author Khyrul Bashar
 */
class StreamTextView
{
    private JScrollPane scrollPane;
    JTextComponent textComponent;

    StreamTextView()
    {
        initUI();
    }

    private void initUI()
    {
        textComponent = new JTextPane();
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
        return scrollPane ;
    }
}
