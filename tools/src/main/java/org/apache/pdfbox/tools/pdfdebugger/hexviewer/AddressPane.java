package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;

/**
 * @author Khyrul Bashar
 *
 * This class shows the address of the currently selected byte.
 */
class AddressPane extends JComponent
{
    private int totalLine;
    private int selectedLine = -1;
    private int selectedIndex = -1;

    AddressPane(int total, HexModel model)
    {
        totalLine = total;
        setPreferredSize(new Dimension(HexView.ADDRESS_PANE_WIDTH, HexView.TOTAL_HEIGHT));
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
            if (line > totalLine)
            {
                break;
            }
            if (line == selectedLine)
            {
                paintSelected(g, x, y);
            }
            else
            {
                g.drawString(String.format("%08X", (line - 1)*16), x, y);
            }
            x = HexView.LINE_INSET;
            y += HexView.CHAR_HEIGHT;
        }
    }

    private void paintSelected(Graphics g, int x, int y)
    {
        g.setColor(HexView.SELECTED_COLOR);
        g.setFont(HexView.BOLD_FONT);

        g.drawString(String.format("%08X", selectedIndex), x, y);

        g.setColor(Color.black);
        g.setFont(HexView.FONT);
    }

    public void setSelected(int index)
    {
        if (index != selectedIndex)
        {
            selectedLine = HexModel.lineNumber(index);
            selectedIndex = index;
            repaint();
        }
    }
}
