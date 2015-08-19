package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

/**
 * @author Khyrul Bashar
 */
class HexChangedEvent
{
    private final byte newValue;
    private final int byteIndex;

    HexChangedEvent(byte newValue, int byteIndex)
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
