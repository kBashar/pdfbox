package org.apache.pdfbox.tools.pdfdebugger.ui;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

/**
 * @author Khyrul Bashar
 */
public class ZoomMenu
{
    public static final String ZOOM_50_PERCENT = "50%";
    public static final String ZOOM_100_PERCENT = "100%";
    public static final String ZOOM_200_PERCENT = "200%";

    private static ZoomMenu zoomMenu;
    private JMenu menu = null;
    private JRadioButtonMenuItem zoom50Item;
    private JRadioButtonMenuItem zoom100Item;
    private JRadioButtonMenuItem zoom200Item;

    private ZoomMenu()
    {
        menu = createZoomMenu();
    }

    public static ZoomMenu zoomMenuFactory()
    {
        if (zoomMenu == null)
        {
            zoomMenu = new ZoomMenu();
        }
        return zoomMenu;
    }

    public JMenu getMenu()
    {
        return this.menu;
    }

    public void setEnableMenu(boolean isEnable)
    {
        menu.setEnabled(isEnable);
    }

    public ZoomMenu menuListeners(ActionListener listener)
    {
        for (Component comp: menu.getMenuComponents())
        {
            JMenuItem menuItem = (JMenuItem) comp;
            removeActionListeners(menuItem);
            menuItem.addActionListener(listener);
        }
        return zoomMenu;
    }

    public void setZoomSelection(String selection)
    {
        if (ZOOM_50_PERCENT.equals(selection))
        {
            zoom50Item.setSelected(true);
        }
        else if (ZOOM_100_PERCENT.equals(selection))
        {
            zoom100Item.setSelected(true);
        }
        else if (ZOOM_200_PERCENT.equals(selection))
        {
            zoom200Item.setSelected(true);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    private void removeActionListeners(JMenuItem menuItem)
    {
        for (ActionListener listener: menuItem.getActionListeners())
        {
            menuItem.removeActionListener(listener);
        }
    }

    private JMenu createZoomMenu()
    {
        menu = new JMenu();
        menu.setText("Zoom");

        zoom50Item = new JRadioButtonMenuItem();
        zoom100Item = new JRadioButtonMenuItem();
        zoom200Item = new JRadioButtonMenuItem();
        zoom100Item.setSelected(true);

        ButtonGroup bg = new ButtonGroup();
        bg.add(zoom50Item);
        bg.add(zoom100Item);
        bg.add(zoom200Item);

        zoom50Item.setText(ZOOM_50_PERCENT);
        zoom100Item.setText(ZOOM_100_PERCENT);
        zoom200Item.setText(ZOOM_200_PERCENT);

        menu.add(zoom50Item);
        menu.add(zoom100Item);
        menu.add(zoom200Item);

        return menu;
    }
}
