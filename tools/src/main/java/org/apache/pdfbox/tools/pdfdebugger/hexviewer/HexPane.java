package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;

/**
 * @author Khyrul Bashar
 */
public class HexPane extends JComponent implements KeyListener, MouseListener, MouseMotionListener, HexModelChangeListener
{
    public static final int WIDTH = 400;
    private HexModel model;
    private static int selectedIndex = -1;
    private static final byte EDIT = 2;
    private static final byte SELECTED = 1;
    private static final byte NORMAL = 0;

    private byte state = NORMAL;
    public static final int CHAR_WIDTH = 25;
    private int selectedChar = 0;

    private List<HexChangeListener> hexChangeListeners = new ArrayList<HexChangeListener>();
    private List<SelectionChangeListener> selectionChangeListeners = new ArrayList<SelectionChangeListener>();
    private int count;

    HexPane(HexModel model)
    {
        this.model = model;
        model.addHexModelChangeListener(this);
        setPreferredSize(new Dimension(WIDTH, (model.totalLine() + 1) * Util.CHAR_HEIGHT));
        setBorder(new BevelBorder(BevelBorder.RAISED));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        setAutoscrolls(true);
        setFont(Util.FONT);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Rectangle bound = g.getClipBounds();
        g.setColor(Color.WHITE);
        g.fillRect(bound.x, bound.y, bound.width, bound.height);

        int x = bound.x;
        int y = bound.y;
        System.out.println("Count: " + count++ + "---> Hex Pane : " + "X: " + x + " Y: " + y + " Width: " + bound.width
                + " Height: " + bound.height);
        int firstLine = HexModel.lineForYValue(y);

        y += Util.CHAR_HEIGHT;

        g.setColor(Color.BLACK);
        for (int i = firstLine; i <= firstLine + bound.height/Util.CHAR_HEIGHT; i++)
        {
            if (i > model.totalLine())
            {
                break;
            }
            byte[] bytes = model.getBytesForLine(i);
            int index = (i - 1) * 16;
            for (byte by : bytes)
            {
                String str = String.format("%02X", by);
                if (selectedIndex == index && state == SELECTED)
                {
                    g.drawString(getSelectedString(str).getIterator(), x, y);
                }
                else if (selectedIndex == index && state == EDIT)
                {
                    paintInEdit(g, by, x, y);
                }
                else
                {
                    g.drawString(str, x, y);
                }
                x += CHAR_WIDTH;
                index++;
            }
            x = 0;
            y += Util.CHAR_HEIGHT;
        }
    }

    private void paintInEdit(Graphics g, byte content, int x, int y)
    {
        g.setFont(Util.BOLD_FONT);
        g.setColor(Color.white);

        char[] chars = getChars(content);

        if (selectedChar == 0)
        {
            g.setColor(new Color(98, 134, 198));
            g.drawChars(chars, 0, 1, x, y);

            g.setColor(Color.black);
            g.drawChars(chars, 1, 1, x + g.getFontMetrics().charWidth(chars[0]), y);
        }
        else
        {
            g.setColor(Color.black);
            g.drawChars(chars, 0, 1, x, y);

            g.setColor(new Color(98, 134, 198));
            g.drawChars(chars, 1, 1,x + g.getFontMetrics().charWidth(chars[0]), y);
        }
        setDefault(g);
    }

    private AttributedString getSelectedString(String str)
    {
        AttributedString string = new AttributedString(str);
        string.addAttribute(TextAttribute.FONT, Util.BOLD_FONT);
        string.addAttribute(TextAttribute.FOREGROUND, new Color(98, 134, 198));
        return string;
    }

    private void setDefault(Graphics g)
    {
        g.setColor(Color.black);
        g.setFont(this.getFont());
    }

    private int getIndexForPoint(Point point)
    {
        if (point.x > 16 * CHAR_WIDTH)
        {
            return -1;
        }
        int y = point.y;
        int lineNumber = (y+ (Util.CHAR_HEIGHT -(y % Util.CHAR_HEIGHT)))/15;
        int x = point.x;
        int elementNumber = (x / CHAR_WIDTH);
        int index = (lineNumber-1) * 16 + elementNumber;
        System.out.println("X: " + point.x + "\n" + "Y: " + point.y + "\n"
                + "Line Number:" + lineNumber + "\n" + "Item number" + elementNumber);
        return index;
    }

    private void putInSelected(int index)
    {
        state = SELECTED;
        selectedChar = 0;
        System.out.println("selected Line Number : " + HexModel.lineNumber(index));
        //byte unit right below selected
        if (index - selectedIndex >= 16)
        {
            int y = (HexModel.lineNumber(index)) * Util.CHAR_HEIGHT;
            scrollRectToVisible(new Rectangle(0, y, 16 * CHAR_WIDTH, Util.CHAR_HEIGHT));
        }
        //byte unit right up selected
        else if (index - selectedIndex <= -16)
        {
            int y = (HexModel.lineNumber(index)-1) * Util.CHAR_HEIGHT;
            scrollRectToVisible(new Rectangle(0, y, 16 * CHAR_WIDTH, Util.CHAR_HEIGHT));
        }
        selectedIndex = index;
        repaint();
        requestFocusInWindow();
    }

    private void fireSelectionChanged(SelectEvent event)
    {
        for (SelectionChangeListener listener:selectionChangeListeners)
        {
            listener.selectionChanged(event);
        }
    }

    private void fireHexValueChanged(byte value, int index)
    {
        for (HexChangeListener listener:hexChangeListeners)
        {
            listener.hexChanged(new HexChangedEvent(value, selectedIndex));
        }
    }

    public void addSelectionChangeListener(SelectionChangeListener listener)
    {
        selectionChangeListeners.add(listener);
    }

    public void addHexChangeListeners(HexChangeListener listener)
    {
        hexChangeListeners.add(listener);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent)
    {
        if (selectedIndex != -1)
        {
            char c = keyEvent.getKeyChar();
            if (isHexChar(c))
            {
                byte previousByte = model.getByte(selectedIndex);
                char[] chars = getChars(previousByte);
                chars[selectedChar] = c;
                byte editByte = getByte(chars);
                if (selectedChar == 0)
                {
                    state = EDIT;
                    selectedChar = 1;
                    fireHexValueChanged(editByte, selectedIndex);
                }
                else
                {
                    fireHexValueChanged(editByte, selectedIndex);
                    fireSelectionChanged(new SelectEvent(selectedIndex, SelectEvent.NEXT));
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent)
    {
        if (state == SELECTED || state == EDIT)
        {
            System.out.println(keyEvent.getKeyCode());
            switch (keyEvent.getKeyCode())
            {
                case 37:
                    if (state == EDIT && selectedChar == 1)
                    {
                        selectedChar = 0;
                        repaint();
                    }
                    else
                    {
                        fireSelectionChanged(new SelectEvent(selectedIndex, SelectEvent.PREVIOUS));
                    }
                    break;
                case 39:
                    fireSelectionChanged(new SelectEvent(selectedIndex, SelectEvent.NEXT));
                    break;
                case 38:
                    fireSelectionChanged(new SelectEvent(selectedIndex, SelectEvent.UP));
                    break;
                case 40:
                    fireSelectionChanged(new SelectEvent(selectedIndex, SelectEvent.DOWN));
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void keyReleased(KeyEvent keyEvent)
    {

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent)
    {
        int index = getIndexForPoint(mouseEvent.getPoint());
        if (index == -1)
        {
            fireSelectionChanged(new SelectEvent(-1, SelectEvent.NONE));
            return;
        }
        fireSelectionChanged(new SelectEvent(index, SelectEvent.IN));
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent)
    {

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
    public void mouseDragged(MouseEvent mouseEvent)
    {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent)
    {

    }

    private static boolean isHexChar(char c)
    {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    private char[] getChars(byte b)
    {
        return String.format("%02X", b & 0XFF).toCharArray();
    }

    private byte getByte(char[] chars)
    {
        return (byte) (Integer.parseInt(new String(chars), 16) & 0XFF);
    }

    public void setSelected(int index)
    {
        if (index != selectedIndex)
        {
            putInSelected(index);
        }
    }

    @Override
    public void hexModelChanged(HexModelChangedEvent event)
    {
        repaint();
    }
}
