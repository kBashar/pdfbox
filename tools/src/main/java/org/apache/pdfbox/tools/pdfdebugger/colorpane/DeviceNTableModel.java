package org.apache.pdfbox.tools.pdfdebugger.colorpane;

import java.awt.Color;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Author by Khyrul Bashar.
 */
public class DeviceNTableModel extends AbstractTableModel
{
    private String[] columnNames = new String[]{"Colorant", "Maximum", "MiniMum"};
    private DeviceNColorant[] data;

    public DeviceNTableModel(DeviceNColorant[] colorants)
    {
        data = colorants;
    }

    @Override
    public int getRowCount()
    {
        return data.length;
    }

    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        switch (column)
        {
            case 0:
                return data[row].getName();
            case 1:
                return data[row].getMaximum();
            case 2:
                return data[row].getMinimum();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                return String.class;
            case 1:
            case 2:
                return Color.class;
            default:
                return null;
        }
    }
}
