package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Khyrul Bashar
 */
public class StatusPane extends JPanel
{
    private int height = 20;
    private JLabel lineLabel;
    private JLabel colLabel;
    private JLabel indexLabel;

    StatusPane()
    {
        setPreferredSize(new Dimension(AddressPane.WIDTH + HexPane.WIDTH + ASCIIPane.LINE_WIDTH, height));
        setLayout(new FlowLayout(FlowLayout.LEFT));
        createView();
    }

    private void createView()
    {
        JLabel line = new JLabel("Line:");
        JLabel column = new JLabel("Column:");
        lineLabel = new JLabel("-1");
        lineLabel.setPreferredSize(new Dimension(100, height));
        colLabel = new JLabel("-1");
        colLabel.setPreferredSize(new Dimension(100, height));
        JLabel index = new JLabel("Index:");
        indexLabel = new JLabel("-1");

        add(line);
        add(lineLabel);
        add(column);
        add(colLabel);
        add(index);
        add(indexLabel);
    }

    public void updateStatus(int index)
    {
        lineLabel.setText(String.valueOf(HexModel.lineNumber(index)));
        colLabel.setText(String.valueOf(HexModel.elementIndexInLine(index)+1));
        indexLabel.setText(String.valueOf(index));
    }
}
