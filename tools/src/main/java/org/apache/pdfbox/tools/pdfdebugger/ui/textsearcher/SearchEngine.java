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

import java.util.ArrayList;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;

/**
 * @author Khyrul Bashar
 */
class SearchEngine
{
    private Document document;
    private Highlighter highlighter;

    private Highlighter.HighlightPainter painter;

    public SearchEngine(Document document, Highlighter highlighter, Highlighter.HighlightPainter painter)
    {
        this.document = document;
        this.highlighter = highlighter;
        this.painter = painter;
    }

    public ArrayList<Highlighter.Highlight> search(String searchKey, boolean isCaseSensitive)
    {
        ArrayList<Highlighter.Highlight> highlights = new ArrayList<Highlighter.Highlight>();

        if (searchKey != null)
        {
            highlighter.removeAllHighlights();

            if ("".equals(searchKey))
            {
                return highlights;
            }

            String textContent;

            try
            {
                textContent = document.getText(0, document.getLength());
            }
            catch (BadLocationException e)
            {
                e.printStackTrace();
                return highlights;
            }
            if (!isCaseSensitive)
            {
                textContent = textContent.toLowerCase();
                searchKey = searchKey.toLowerCase();
            }

            int searchKeyLength = searchKey.length();
            int startAt = 0;
            int resultantOffset;
            int indexOfHighLight = 0;

            while ((resultantOffset = textContent.indexOf(searchKey, startAt)) != -1)
            {
                try
                {
                    highlighter.addHighlight(resultantOffset, resultantOffset + searchKeyLength, painter);
                    highlights.add(highlighter.getHighlights()[indexOfHighLight++]);
                    startAt = resultantOffset + searchKeyLength;
                }
                catch (BadLocationException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return highlights;
    }
}
