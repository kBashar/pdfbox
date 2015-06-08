package org.apache.pdfbox.tools.pdfdebugger.colorpane;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Author by Khyrul Bashar.
 */
public class ColorBarCellRenderer implements TableCellRenderer
{
    @Override
    public Component getTableCellRendererComponent(
            JTable jTable, Object o, boolean b, boolean b2, int i, int i2)
    {
        JLabel colorBar = new JLabel();
        colorBar.setOpaque(true);
        Color color = (Color) o;
        colorBar.setBackground(color);
        return colorBar;
    }
}
