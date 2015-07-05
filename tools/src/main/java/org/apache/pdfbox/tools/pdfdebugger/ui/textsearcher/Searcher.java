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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

/**
 * @author Khyrul Bashar
 */
public class Searcher implements DocumentListener, ActionListener
{
    private int totalMatch = 0;
    private int currentMatch = 0;

    private SearchEngine searchEngine;
    private SearchPanel searchPanel;
    private JTextComponent textComponent;

    public Searcher()
    {
        searchPanel = new SearchPanel(this, this);
    }

    public void setTextComponent(JTextComponent textComponent)
    {
        if (textComponent != null)
        {
            this.textComponent = textComponent;
            searchEngine = new SearchEngine(textComponent.getDocument(), textComponent.getHighlighter());
        }
        else
        {
            throw new IllegalArgumentException("Null is not acceptable");
        }
    }

    public JPanel getSearchPanel()
    {
        return searchPanel.getPanel();
    }

    @Override
    public void insertUpdate(DocumentEvent documentEvent)
    {
        search(documentEvent);
    }

    @Override
    public void removeUpdate(DocumentEvent documentEvent)
    {
        search(documentEvent);
    }

    @Override
    public void changedUpdate(DocumentEvent documentEvent)
    {
        search(documentEvent);
    }

    private void search(DocumentEvent documentEvent)
    {
        try
        {
            String word = documentEvent.getDocument().getText(0, documentEvent.getDocument().getLength());
            search(word);
        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }

    private void search(String word)
    {
        int offset = searchEngine.search(word);
        if (offset != -1)
        {
            enableTraverse(offset);
        }
    }

    private void enableTraverse(int offset)
    {
        totalMatch = textComponent.getHighlighter().getHighlights().length;
        scrollToWord(offset);
        currentMatch = 1;
        searchPanel.nextEnabled(totalMatch > 1);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        String actionCommand = actionEvent.getActionCommand();

        if (actionCommand.equals(SearchPanel.NEXT))
        {
            currentMatch = currentMatch + 1;
            int offset = textComponent.getHighlighter().getHighlights()[currentMatch - 1].getStartOffset();
            scrollToWord(offset);

            if (currentMatch == totalMatch)
            {
                searchPanel.nextEnabled(false);
            }

            if (currentMatch >= 2)
            {
                searchPanel.previousEnabled(true);
            }
        }
        else if (actionCommand.equals(SearchPanel.PREVIOUS))
        {
            currentMatch = currentMatch - 1;
            int offset = textComponent.getHighlighter().getHighlights()[currentMatch - 1].getStartOffset();
            scrollToWord(offset);

            if (currentMatch == 1)
            {
                searchPanel.previousEnabled(false);
            }
            if (currentMatch < totalMatch)
            {
                searchPanel.nextEnabled(true);
            }
        }
    }

    private void scrollToWord(int offset)
    {
        try
        {
            textComponent.scrollRectToVisible(textComponent.modelToView(offset + 5));
        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }
}
