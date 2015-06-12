package org.apache.pdfbox.tools.pdfdebugger.colorpane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.color.PDIndexed;

/**
 * Author by Khyrul Bashar.
 */
public class CSIndexed
{
    private PDIndexed indexed;
    private JScrollPane panel;
    private int colorCount;

    public CSIndexed(COSArray array)
    {
        try
        {
            indexed = new PDIndexed(array);
            colorCount = getHival(array);
            initUI(getColorantData());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses the colorant data from the array and return.
     *
     * @return
     */
    private IndexedColorant[] getColorantData()
    {
        IndexedColorant[] colorants = new IndexedColorant[colorCount];
        for (int i = 0; i < colorCount; i++)
        {
            IndexedColorant colorant = new IndexedColorant();
            colorant.setIndex(i);

            float[] rgbValues = indexed.toRGB(new float[]{i});
            colorant.setRgbValues(rgbValues);
            colorants[i] = colorant;
        }
        return colorants;
    }

    private void initUI(IndexedColorant[] colorants)
    {
        IndexedTableModel tableModel = new IndexedTableModel(colorants);
        JTable table = new JTable(tableModel);
        table.setDefaultRenderer(Color.class, new ColorBarCellRenderer());
        table.setRowHeight(40);
        table.getColumnModel().getColumn(0).setMinWidth(30);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setMinWidth(100);
        table.getColumnModel().getColumn(1).setMaxWidth(100);

        panel = new JScrollPane();
        panel.setPreferredSize(new Dimension(300, 500));
        panel.setViewportView(table);
    }

    /**
     * return the main panel that hold all the UI elements.
     *
     * @return JPanel instance
     */
    public Component getPanel()
    {
        return panel;
    }

    private int getHival(COSArray array)
    {
        int hival = ((COSNumber) array.getObject(2).getCOSObject()).intValue();
        return Math.min(hival, 255);
    }
}
