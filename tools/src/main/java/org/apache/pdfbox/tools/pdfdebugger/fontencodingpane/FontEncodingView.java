package org.apache.pdfbox.tools.pdfdebugger.fontencodingpane;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * @author Khyrul Bashar
 */
class FontEncodingView
{
    JPanel panel;

    FontEncodingView(Object[][] tableData, String[] columnNames)
    {
        createView(tableData, columnNames);
    }

    private void createView(Object[][] tableData, String[] columnNames)
    {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(300, 500));

        JLabel flagLabel = new JLabel("XXXXX");
        flagLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        flagLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        JPanel flagLabelPanel = new JPanel();
        flagLabelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        flagLabelPanel.add(flagLabel);

        JLabel flagValueLabel = new JLabel("XXXXX");
        flagValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        flagValueLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

        JTable table = new JTable(tableData, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        Box box = Box.createVerticalBox();
        box.add(flagValueLabel);
        box.add(scrollPane);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;

        panel.add(flagLabelPanel, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.9;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.BELOW_BASELINE;

        panel.add(box, gbc);
    }

    JPanel getPanel()
    {
        return panel;
    }
}
