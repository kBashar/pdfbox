package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.tools.pdfdebugger.streampane.Stream;

/**
 * @author Khyrul Bashar
 */
public class HexView implements ActionListener
{
    private JComponent mainPane;
    private Stream stream;

    static final Font FONT = new Font("monospaced", Font.PLAIN, 15);
    static final int CHAR_HEIGHT = 20;
    static final int CHAR_WIDTH = 35;
    static final int LINE_INSET = 20;
    static final Color SELECTED_COLOR = new Color(98, 134, 198);
    static final Font BOLD_FONT = new Font(Font.MONOSPACED, Font.BOLD, 15);
    static final int HEX_PANE_WIDTH = 600;
    static final int ADDRESS_PANE_WIDTH = 120;
    static final int ASCII_PANE_WIDTH = 270;
    static final int TOTAL_WIDTH = HEX_PANE_WIDTH + ADDRESS_PANE_WIDTH +ASCII_PANE_WIDTH;
    static int TOTAL_HEIGHT;

    public HexView(byte[] bytes)
    {
        createView();
        mainPane.add(createHexEditor(bytes));
    }

    public HexView(Stream stream) throws IOException
    {
        this.stream = stream;
        createView();
        JPanel panel = createHeaderPanel(stream.getFilterList());
        InputStream is = stream.getStream(stream.getFilterList().get(0));
        mainPane.add(panel);
        mainPane.add(createHexEditor(IOUtils.toByteArray(is)));
    }


    private void createView()
    {
        mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
    }

    private HexEditor createHexEditor(byte[] bytes)
    {
        HexModel model = new HexModel(bytes);
        TOTAL_HEIGHT = CHAR_HEIGHT * (model.totalLine()+1);
        return new HexEditor(model);
    }

    public JComponent getPane()
    {
        return mainPane;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if (actionEvent.getActionCommand().equals("comboBoxChanged"))
        {
            JComboBox comboBox = (JComboBox) actionEvent.getSource();
            String currentFilter = (String) comboBox.getSelectedItem();
            try
            {
                HexEditor editor = createHexEditor(IOUtils.toByteArray(stream.getStream(currentFilter)));
                mainPane.remove(1);
                mainPane.add(editor);
                mainPane.revalidate();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private JPanel createHeaderPanel(List<String> availableFilters)
    {
        if (availableFilters.contains("Image"))
        {
            availableFilters.remove("Image");
        }
        JComboBox filters = new JComboBox(availableFilters.toArray());
        filters.setSelectedItem(0);
        filters.addActionListener(this);

        JPanel panel = new JPanel()
        {
            @Override
            public Dimension getMaximumSize()
            {
                return new Dimension(HexView.TOTAL_WIDTH, 45);
            }
        };
        panel.setLayout(new FlowLayout());
        panel.add(filters);
        panel.setPreferredSize(new Dimension(HexView.TOTAL_WIDTH, 45));
        return panel;
    }
}
