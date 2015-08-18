package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

/**
 * @author Khyrul Bashar
 */
public class HexChangedEvent
{
    private final byte newValue;
    private final int byteIndex;

    public HexChangedEvent(byte newValue, int byteIndex)
    {
        this.newValue = newValue;
        this.byteIndex = byteIndex;
    }

    public byte getNewValue()
    {
        return newValue;
    }

    public int getByteIndex()
    {
        return byteIndex;
    }
}
