package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

import java.util.ArrayList;

/**
 * @author Khyrul Bashar
 *
 * A class that acts as a model for the hex viewer. It holds the data and provide the data as ncessary.
 * It'll let listen for any underlying data changes.
 */
class HexModel implements HexChangeListener
{
    private ArrayList<Byte> data;
    private ArrayList<HexModelChangeListener> modelChangeListeners;

    /**
     * Constructor
     * @param bytes Byte array.
     */
    public HexModel(byte[] bytes)
    {
        data = new ArrayList<Byte>();
        data.ensureCapacity(bytes.length);

        for (byte b: bytes)
        {
            data.add(b);
        }

        modelChangeListeners = new ArrayList<HexModelChangeListener>();
    }

    /**
     * provides the byte for a specific index of the byte array.
     * @param index int.
     * @return byte instance
     */
    public byte getByte(int index)
    {
        return data.get(index).byteValue();
    }

    /**
     * Provides a character array of 16 characters on availability.
     * @param lineNumber int. The line number of the characters. Line counting starts from 1.
     * @return A char array.
     */
    public char[] getLineChars(int lineNumber)
    {
        int start = (lineNumber-1) * 16;
        int length = data.size() - start < 16 ? data.size() - start:16;
        char[] chars = new char[length];

        for (int i = 0; i < chars.length; i++)
        {
            char c = Character.toChars(data.get(start) & 0XFF)[0];
            if (!isAsciiPrintable(c))
            {
                c = '.';
            }
            chars[i] = c;
            start++;
        }
        return chars;
    }

    public byte[] getBytesForLine(int lineNumber)
    {
        int index = (lineNumber-1) * 16;
        int length = Math.min(data.size() - index, 16);
        byte[] bytes = new byte[length];

        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = data.get(index);
            index++;
        }
        return bytes;
    }

    /**
     * Provides the size of the model i.e. size of the input.
     * @return int value.
     */
    public int size()
    {
        return data.size();
    }

    /**
     *
     * @return
     */
    public int totalLine()
    {
        return size() % 16 != 0 ? size()/16 + 1 : size()/16;
    }

    public static int lineNumber(int index)
    {
        index += 1;
        if (index == 0)
        {
            return 0;
        }
        else
        {
            return index % 16 != 0 ? index/16 + 1 : index/16;
        }
    }

    public static int elementIndexInLine(int index)
    {
        return index%16;
    }

    private static boolean isAsciiPrintable(char ch)
    {
        return ch >= 32 && ch < 127;
    }

    public void addHexModelChangeListener(HexModelChangeListener listener)
    {
        modelChangeListeners.add(listener);
    }

    public void updateModel(int index, byte value)
    {
        if (!data.get(index).equals(value))
        {
            data.set(index, value);
            for (HexModelChangeListener listener: modelChangeListeners)
            {
                listener.hexModelChanged(new HexModelChangedEvent(index, HexModelChangedEvent.SINGLE_CHANGE));
            }
        }
    }

    public static int lineForYValue(int y)
    {
        return (y / HexView.CHAR_HEIGHT) + 1;
    }

    private void fireModelChanged(int index)
    {
        for (HexModelChangeListener listener:modelChangeListeners)
        {
            listener.hexModelChanged(new HexModelChangedEvent(index, HexModelChangedEvent.SINGLE_CHANGE));
        }
    }

    @Override
    public void hexChanged(HexChangedEvent event)
    {
        int index = event.getByteIndex();
        if (index != -1 && getByte(index) != event.getNewValue())
        {
            data.set(index, event.getNewValue());
        }
        fireModelChanged(index);
    }
}
