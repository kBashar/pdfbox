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
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 * @author Khyrul Bashar
 */
class SearchEngine
{
    private Document document;
    private Highlighter highlighter;

    private final static Highlighter.HighlightPainter painter =
            new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);

    public SearchEngine(Document document, Highlighter highlighter)
    {
        this.document = document;
        this.highlighter = highlighter;
    }

    public int search(String searchKey)
    {
        if (searchKey != null)
        {
            highlighter.removeAllHighlights();

            if ("".equals(searchKey))
            {
                return -1;
            }

            String textContent = null;

            try
            {
                textContent = document.getText(0, document.getLength()).toLowerCase();
            }
            catch (BadLocationException e)
            {
                e.printStackTrace();
                return -1;
            }
            searchKey = searchKey.toLowerCase();

            int firstOffset = -1;
            int searchKeyLength = searchKey.length();
            int startAt = 0;
            int resultantOffset = -1;

            while ((resultantOffset = textContent.indexOf(searchKey, startAt)) != -1)
            {
                try
                {
                    highlighter.addHighlight(resultantOffset, resultantOffset + searchKeyLength, painter);
                    startAt = resultantOffset + searchKeyLength;
                    if (firstOffset == -1)
                    {
                        firstOffset = resultantOffset;
                    }
                }
                catch (BadLocationException e)
                {
                    e.printStackTrace();
                }
            }
            return firstOffset;
        }
        return -1;
    }
}
