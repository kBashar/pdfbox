package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

/**
 * @author Khyrul Bashar
 */
class SelectEvent
{
    public static final String NEXT = "next";
    public static final String PREVIOUS = "previous";
    public static final String UP = "up";
    public static final String DOWN= "down";
    public static final String NONE = "none";
    public static final String IN = "in";
    public static final String EDIT = "edit";

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
