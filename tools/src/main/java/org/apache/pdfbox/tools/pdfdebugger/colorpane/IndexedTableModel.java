package org.apache.pdfbox.tools.pdfdebugger.colorpane;

import java.awt.Color;
import javax.swing.table.AbstractTableModel;

/**
 * Author by Khyrul Bashar.
 */
public class IndexedTableModel extends AbstractTableModel
{

    private static final String[] columnsName = new String[] {"Index", "RGB value", "Color"};
    private IndexedColorant[] data;

    public IndexedTableModel(IndexedColorant[] colorants)
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
        return columnsName.length;
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        switch (column)
        {
            case 0:
                return data[row].getIndex();
            case 1:
                return data[row].getRGBValuesString();
            case 2:
                return data[row].getColor();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column)
    {
        return columnsName[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return Color.class;
            default:
                return null;
        }
    }
}
