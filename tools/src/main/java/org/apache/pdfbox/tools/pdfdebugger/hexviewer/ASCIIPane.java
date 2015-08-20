package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;

/**
 * @author Khyrul Bashar
 */
class ASCIIPane extends JComponent implements HexModelChangeListener
{
    private final HexModel model;

    private int selectedLine = -1;
    private int selectedIndexInLine;


    ASCIIPane(HexModel model)
    {
        this.model = model;
        setPreferredSize(new Dimension(HexView.ASCII_PANE_WIDTH, HexView.TOTAL_HEIGHT));
        model.addHexModelChangeListener(this);
        setFont(HexView.FONT);
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Rectangle bound = getVisibleRect();

        int x = HexView.LINE_INSET;
        int y = bound.y;

        if (y == 0 || y%HexView.CHAR_HEIGHT != 0)
        {
            y += HexView.CHAR_HEIGHT - y%HexView.CHAR_HEIGHT;
        }
        int firstLine = y/HexView.CHAR_HEIGHT;

        for (int line = firstLine; line < firstLine + bound.getHeight()/HexView.CHAR_HEIGHT; line++)
        {
            if (line > model.totalLine())
            {
                break;
            }
            if (line == selectedLine)
            {
                paintInSelected(g, x, y);
            }
            else
            {
                char[] chars = model.getLineChars(line);
                g.drawChars(chars, 0, chars.length, x, y);
            }
            x = HexView.LINE_INSET;
            y += HexView.CHAR_HEIGHT;
        }
    }

    private void paintInSelected(Graphics g, int x, int y)
    {
        g.setFont(HexView.BOLD_FONT);
        char[] content = model.getLineChars(selectedLine);
        g.drawChars(content, 0, selectedIndexInLine - 0, x, y);

        g.setColor(HexView.SELECTED_COLOR);
        x += g.getFontMetrics().charsWidth(content, 0, selectedIndexInLine-0);
        g.drawChars(content, selectedIndexInLine, 1, x, y);

        g.setColor(Color.black);
        x+= g.getFontMetrics().charWidth(content[selectedIndexInLine]);
        g.drawChars(content, selectedIndexInLine+1, (content.length-1)-selectedIndexInLine, x, y);
        g.setFont(HexView.FONT);
    }
    
    @Override
    public void hexModelChanged(HexModelChangedEvent event)
    {
        repaint();
    }

    public void setSelected(int index)
    {
            selectedLine = HexModel.lineNumber(index);
            selectedIndexInLine = HexModel.elementIndexInLine(index);
            repaint();
    }
}
