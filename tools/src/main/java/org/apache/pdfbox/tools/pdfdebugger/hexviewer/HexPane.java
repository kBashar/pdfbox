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
import javax.swing.JPanel;

/**
 * @author Khyrul Bashar
 */
class HexPane extends JPanel implements KeyListener, MouseListener, MouseMotionListener, HexModelChangeListener
{
    private HexModel model;
    private static int selectedIndex = -1;
    private static final byte EDIT = 2;
    private static final byte SELECTED = 1;
    private static final byte NORMAL = 0;

    private byte state = NORMAL;
    private int selectedChar = 0;

    private List<HexChangeListener> hexChangeListeners = new ArrayList<HexChangeListener>();
    private List<SelectionChangeListener> selectionChangeListeners = new ArrayList<SelectionChangeListener>();
    private int count;

    HexPane(HexModel model)
    {
        this.model = model;
        model.addHexModelChangeListener(this);
        setPreferredSize(new Dimension(HexView.HEX_PANE_WIDTH, HexView.TOTAL_HEIGHT));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        setAutoscrolls(true);
        setFont(HexView.FONT);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Rectangle bound = getVisibleRect();
        g.clearRect(bound.x, bound.y, bound.width, bound.height);
        g.setColor(Color.WHITE);
        g.fillRect(bound.x, bound.y, bound.width, bound.height);

        int x = HexView.LINE_INSET;
        int y = bound.y;
        System.out.println("Count: " + count++ + "---> Hex Pane : " + "X: " + x + " Y: " + y + " Width: " + bound.width
                + " Height: " + bound.height);
        if (y == 0 || y%HexView.CHAR_HEIGHT != 0)
        {
            y += HexView.CHAR_HEIGHT - y%HexView.CHAR_HEIGHT;
        }
        int firstLine = y/HexView.CHAR_HEIGHT;

        g.setColor(Color.BLACK);
        for (int i = firstLine; i <= firstLine + bound.height/HexView.CHAR_HEIGHT; i++)
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
                x += HexView.CHAR_WIDTH;
                index++;
            }
            x = HexView.LINE_INSET;
            y += HexView.CHAR_HEIGHT;
        }
    }

    private void paintInEdit(Graphics g, byte content, int x, int y)
    {
        g.setFont(HexView.BOLD_FONT);
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
        string.addAttribute(TextAttribute.FONT, HexView.BOLD_FONT);
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
        if (point.x <= 20 || point.x >= (16 * HexView.CHAR_WIDTH)+20 )
        {
            return -1;
        }
        System.out.println(point);
        int y = point.y;
        int lineNumber = (y+ (HexView.CHAR_HEIGHT -(y % HexView.CHAR_HEIGHT)))/HexView.CHAR_HEIGHT;
        int x = point.x - 20;
        int elementNumber = (x / HexView.CHAR_WIDTH);
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
            int y = (HexModel.lineNumber(index)) * HexView.CHAR_HEIGHT;
            scrollRectToVisible(new Rectangle(0, y, 16 * HexView.CHAR_WIDTH, HexView.CHAR_HEIGHT));
        }
        //byte unit right up selected
        else if (index - selectedIndex <= -16)
        {
            int y = (HexModel.lineNumber(index)-1) * HexView.CHAR_HEIGHT;
            scrollRectToVisible(new Rectangle(0, y, 16 * HexView.CHAR_WIDTH, HexView.CHAR_HEIGHT));
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
