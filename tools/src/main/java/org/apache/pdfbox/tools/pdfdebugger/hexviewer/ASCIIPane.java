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
 */
class ASCIIPane extends JComponent implements MouseListener, HexModelChangeListener
{
    private HexModel model;

    static final int LINE_WIDTH = 130;

    private int selectedLine = -1;
    private int selectedIndex = -1;

    private ArrayList<BlankClickListener> blankClickListeners = new ArrayList<BlankClickListener>();
    private int selectedIndexInLine;
    private int count;

    ASCIIPane(HexModel model)
    {
        this.model = model;
        addMouseListener(this);
        setPreferredSize(new Dimension(LINE_WIDTH, (model.totalLine() + 1) * Util.CHAR_HEIGHT));
        model.addHexModelChangeListener(this);
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Rectangle bound = g.getClipBounds();
        int x = bound.x;
        int y = bound.y;

        System.out.println("Count: " + count++ + "---> Ascii pane " + "X: " + x + " Y: " + y);
        int firstLine = HexModel.lineForYValue(y);

        y += Util.CHAR_HEIGHT;

        for (int line = firstLine; line < firstLine + bound.getHeight()/Util.CHAR_HEIGHT; line++)
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
            x = 0;
            y += Util.CHAR_HEIGHT;
        }
    }

    private void paintInSelected(Graphics g, int x, int y)
    {
        g.setFont(Util.BOLD_FONT);
        char[] content = model.getLineChars(selectedLine);
        g.drawChars(content, 0, selectedIndexInLine - 0, x, y);

        g.setColor(Util.SELECTED_COLOR);
        x += g.getFontMetrics().charsWidth(content, 0, selectedIndexInLine-0);
        g.drawChars(content, selectedIndexInLine, 1, x, y);

        g.setColor(Color.black);
        x+= g.getFontMetrics().charWidth(content[selectedIndexInLine]);
        g.drawChars(content, selectedIndexInLine+1, (content.length-1)-selectedIndexInLine, x, y);
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

    @Override
    public void hexModelChanged(HexModelChangedEvent event)
    {
        repaint();
    }

    public void setSelected(int index)
    {
        if (index != selectedIndex)
        {
            selectedLine = HexModel.lineNumber(index);
            selectedIndexInLine = HexModel.elementIndexInLine(index);
            repaint();
        }
    }
}
