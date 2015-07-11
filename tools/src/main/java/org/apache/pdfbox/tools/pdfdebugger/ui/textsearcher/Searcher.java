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
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 * @author Khyrul Bashar
 */
public class Searcher implements DocumentListener, ChangeListener, ComponentListener
{
    private int totalMatch = 0;
    private int currentMatch = -1;
    private ArrayList<Highlighter.Highlight> highlights;

    private SearchEngine searchEngine;
    private SearchPanel searchPanel;
    private JTextComponent textComponent;
    private Action nextAction = new AbstractAction("Next")
    {
        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            if (totalMatch != 0)
            {
                currentMatch = currentMatch + 1;
                int offset = highlights.get(currentMatch).getStartOffset();
                scrollToWord(offset);

                updateHighLighter(currentMatch, currentMatch - 1);
                updateNavigationButtons();
            }
        }
    };
    private Action previousAction = new AbstractAction("Previous")
    {
        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            if (totalMatch != 0)
            {
                currentMatch = currentMatch - 1;
                int offset = highlights.get(currentMatch).getStartOffset();
                scrollToWord(offset);

                updateHighLighter(currentMatch, currentMatch + 1);
                updateNavigationButtons();
            }
        }
    };

    private final static Highlighter.HighlightPainter painter =
            new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
    private final Highlighter.HighlightPainter selectionPainter =
            new DefaultHighlighter.DefaultHighlightPainter(new Color(109, 216, 26));

    public Searcher()
    {
        nextAction.setEnabled(false);
        previousAction.setEnabled(false);
        searchPanel = new SearchPanel(this,this, this, nextAction, previousAction);
    }

    public void setTextComponent(JTextComponent textComponent)
    {
        if (textComponent != null)
        {
            this.textComponent = textComponent;
            searchEngine = new SearchEngine(textComponent.getDocument(), textComponent.getHighlighter(), painter);
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
            if (word.isEmpty())
            {
                nextAction.setEnabled(false);
                previousAction.setEnabled(false);
                searchPanel.reset();
                textComponent.getHighlighter().removeAllHighlights();
                return;
            }
            search(word);
        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }

    private void search(String word)
    {
        highlights = searchEngine.search(word, searchPanel.isCaseSensitive());
        if (highlights.size() != 0)
        {
            totalMatch = highlights.size();
            currentMatch = 0;

            scrollToWord(highlights.get(0).getStartOffset());

            updateHighLighter(currentMatch, currentMatch - 1);
            updateNavigationButtons();
        }
        else
        {
            searchPanel.updateCounterLabel(0 , 0);
        }
    }

    private void updateNavigationButtons()
    {
        if (currentMatch == 0)
        {
            previousAction.setEnabled(false);
        }
        else if (currentMatch >= 1 && currentMatch <= (totalMatch - 1 ))
        {
            previousAction.setEnabled(true);
        }
        if (currentMatch == (totalMatch - 1))
        {
            nextAction.setEnabled(false);
        }
        else if (currentMatch < (totalMatch - 1))
        {
            nextAction.setEnabled(true);
        }

        searchPanel.updateCounterLabel(currentMatch + 1, totalMatch);
    }

    private void scrollToWord(int offset)
    {
        try
        {
            textComponent.scrollRectToVisible(textComponent.modelToView(offset));
        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }

    private void updateHighLighter(final int presentIndex,final int previousIndex)
    {
        if (previousIndex != -1)
        {
            changeHighlighter(previousIndex, painter);
        }
        changeHighlighter(presentIndex, selectionPainter);
    }

    private void changeHighlighter(int index, Highlighter.HighlightPainter newPainter)
    {
        Highlighter highlighter = textComponent.getHighlighter();

        Highlighter.Highlight highLight = highlights.get(index);
        highlighter.removeHighlight(highLight);
        try
        {
            highlighter.addHighlight(highLight.getStartOffset(), highLight.getEndOffset(), newPainter);
            highlights.set(index, highlighter.getHighlights()[highlighter.getHighlights().length-1]);
        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent)
    {
        if (changeEvent.getSource() instanceof JCheckBox)
        {
            search(searchPanel.getSearchWord());
        }
    }

    @Override
    public void componentResized(ComponentEvent componentEvent)
    {

    }

    @Override
    public void componentMoved(ComponentEvent componentEvent)
    {

    }

    @Override
    public void componentShown(ComponentEvent componentEvent)
    {
        searchPanel.reFocus();
    }

    @Override
    public void componentHidden(ComponentEvent componentEvent)
    {
        textComponent.getHighlighter().removeAllHighlights();
    }

    public void takeFocus()
    {
        searchPanel.reFocus();
    }
}
