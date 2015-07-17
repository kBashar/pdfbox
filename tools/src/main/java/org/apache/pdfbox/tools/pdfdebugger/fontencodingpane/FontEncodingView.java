package org.apache.pdfbox.tools.pdfdebugger.fontencodingpane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 * @author Khyrul Bashar
 */
class FontEncodingView
{
    private JPanel panel;

    FontEncodingView(Object[][] tableData, String encodingName, String fontName,
                     int totalGlyph, String[] columnNames)
    {
        createView(getHeaderPanel(encodingName, fontName, totalGlyph), getTable(tableData, columnNames));
    }

    private void createView(JPanel headerPanel, JTable table)
    {
        panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(300, 500));

        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;

        panel.add(headerPanel, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.9;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.BELOW_BASELINE;

        panel.add(scrollPane, gbc);
    }

    private JTable getTable(Object[][] tableData, String[] columnNames)
    {
        JTable table = new JTable(tableData, columnNames);
        table.setDefaultRenderer(Object.class, new GlyphCellRenderer());
        return table;
    }

    private JPanel getHeaderPanel(String encodingName, String fontName, int glyphCount)
    {
        JPanel headerPanel = new JPanel(new GridBagLayout());

        JLabel encodingNameLabel = new JLabel("Encoding: " + encodingName);
        encodingNameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.LINE_START;

        headerPanel.add(encodingNameLabel, gbc);

        JLabel fontNameLabel = new JLabel("Font: " + fontName);
        fontNameLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.1;

        headerPanel.add(fontNameLabel, gbc);

        JLabel glyphCountLabel = new JLabel("Glyph count: " + glyphCount);
        glyphCountLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0.1;

        headerPanel.add(glyphCountLabel, gbc);


        return headerPanel;
    }

    JPanel getPanel()
    {
        return panel;
    }

    private final class GlyphCellRenderer implements TableCellRenderer
    {

        @Override
        public Component getTableCellRendererComponent(JTable jTable, Object o, boolean b, boolean b1, int i, int i1)
        {
            if (o != null)
            {
                    JLabel label = new JLabel(o.toString());
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
                    if (SimpleFont.NO_GLYPH.equals(o))
                    {
                        label.setText(SimpleFont.NO_GLYPH);
                        label.setForeground(Color.RED);
                    }
                    return label;
            }
            return new JLabel();
        }
    }
}


