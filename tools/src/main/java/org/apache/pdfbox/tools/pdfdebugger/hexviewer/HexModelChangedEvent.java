package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

/**
 * @author Khyrul Bashar
 */
class HexModelChangedEvent
{
    public static int BULK_CHANGE = 1;
    public static int SINGLE_CHANGE = 2;

    private final int startIndex;
    private final int changeType;

    public HexModelChangedEvent(int startIndex, int changeType)
    {
        this.startIndex = startIndex;
        this.changeType = changeType;
    }

    public int getStartIndex()
    {
        return startIndex;
    }

    public int getChangeType()
    {
        return changeType;
    }
}
