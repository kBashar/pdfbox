package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * @author Khyrul Bashar
 */
class UpperPane extends JPanel
{
    UpperPane()
    {
        setFont(HexView.BOLD_FONT);
        setPreferredSize(new Dimension(HexView.TOTAL_WIDTH, 20));
        setBorder(new BevelBorder(BevelBorder.RAISED));
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        int x = HexView.LINE_INSET-2;
        int y = 16;

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
