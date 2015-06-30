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

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

/**
 * @author Khyrul Bashar
 */
class StreamTextView
{
    private final StyledDocument document;
    private JScrollPane scrollPane;

    StreamTextView(StyledDocument document)
    {
        this.document = document;
        initUI();
    }

    private void initUI()
    {
        JTextPane textPane = new JTextPane(document);
        scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(300, 400));
    }

    JComponent getView()
    {
        return scrollPane ;
    }
}
