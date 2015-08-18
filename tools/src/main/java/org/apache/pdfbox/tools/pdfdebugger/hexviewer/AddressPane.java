package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JComponent;

/**
 * @author Khyrul Bashar
 *
 * This class shows the address of the currently selected byte.
 */
public class AddressPane extends JComponent implements MouseListener
{

    public static final int WIDTH = 90;
    private ArrayList<BlankClickListener> blankClickListeners = new ArrayList<BlankClickListener>();

    private int totalLine;
    private int selectedLine = -1;
    private int selectedIndex = -1;
    int count = 0;

    AddressPane(int total, HexModel model)
    {
        totalLine = total;
        setPreferredSize(new Dimension(WIDTH, (model.totalLine()+1)*Util.CHAR_HEIGHT));
        setFont(Util.FONT);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Rectangle bound = g.getClipBounds();
        int x = bound.x;
        int y = bound.y;
        System.out.println("Count: " +count++ + "---> Address pane " + "X: " + x + " Y: " + y);
        int firstLine = HexModel.lineForYValue(y);

        x+=10;
        y += Util.CHAR_HEIGHT;

        for (int line = firstLine; line < firstLine + bound.getHeight()/Util.CHAR_HEIGHT; line++)
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
            x = 10;
            y += Util.CHAR_HEIGHT;
        }
    }


    private void paintSelected(Graphics g, int x, int y)
    {
        g.setColor(Util.SELECTED_COLOR);
        g.setFont(Util.BOLD_FONT);

        g.drawString(String.format("%08X", selectedIndex), x, y);

        g.setColor(Color.black);
        g.setFont(Util.FONT);
    }

    public void addBlankClickListener(BlankClickListener listener)
    {
        blankClickListeners.add(listener);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent)
    {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent)
    {
        for (BlankClickListener listener: blankClickListeners)
        {
            listener.blankClick(mouseEvent.getPoint());
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent)
    {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent)
    {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent)
    {

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
