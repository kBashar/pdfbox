package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

/**
 * @author Khyrul Bashar
 */
class SelectEvent
{
    static final String NEXT = "next";
    static final String PREVIOUS = "previous";
    static final String UP = "up";
    static final String DOWN= "down";
    static final String NONE = "none";
    static final String IN = "in";
    static final String EDIT = "edit";

    private final int hexIndex;
    private final String navigation;

    SelectEvent(int ind, String nav)
    {
        hexIndex = ind;
        navigation = nav;
    }

    public int getHexIndex()
    {
        return hexIndex;
    }

    public String getNavigation()
    {
        return navigation;
    }
}
