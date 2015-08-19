package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 * @author Khyrul Bashar
 */
class UpperPane extends JComponent
{
    UpperPane()
    {
        setFont(HexView.BOLD_FONT);
        setPreferredSize(new Dimension(HexView.TOTAL_WIDTH, HexView.CHAR_HEIGHT));
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        int x = HexView.LINE_INSET;
        int y = HexView.CHAR_HEIGHT-10;

        g.drawString("Offset", x, y);

        x += HexView.ADDRESS_PANE_WIDTH;

        for (int i = 0; i <= 15; i++)
        {
            g.drawString(String.format("%02X", i), x, y);
            x += HexView.CHAR_WIDTH;
        }

        x+=HexView.LINE_INSET*2;
        g.drawString("Dump", x, y);
    }

}
