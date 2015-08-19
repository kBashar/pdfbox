package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * @author Khyrul Bashar
 */
class HexEditor extends JPanel implements SelectionChangeListener, BlankClickListener
{
    private HexModel model;
    private HexPane hexPane;
    private ASCIIPane asciiPane;
    private AddressPane addressPane;
    private StatusPane statusPane;

    private JScrollBar verticalScrollBar;

    private Action jumpToIndex = new AbstractAction()
    {
        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            createJumpDialog().setVisible(true);
        }
    };

    public static int selectedIndex = -1;

    HexEditor(HexModel model)
    {
        super();
        this.model = model;
        createView();
    }

    private void createView()
    {
        setLayout(new GridBagLayout());

        addressPane = new AddressPane(model.totalLine(), model);
        hexPane = new HexPane(model);
        hexPane.addHexChangeListeners(model);
        asciiPane = new ASCIIPane(model);
        UpperPane upperPane = new UpperPane();
        statusPane = new StatusPane();

        model.addHexModelChangeListener(hexPane);
        model.addHexModelChangeListener(asciiPane);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setPreferredSize(new Dimension(HexView.TOTAL_WIDTH, HexView.TOTAL_HEIGHT));
        panel.add(addressPane);
        panel.add(hexPane);
        panel.add(asciiPane);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(panel);

        scrollPane.getActionMap().put("unitScrollDown", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            }
        });

        scrollPane.getActionMap().put("unitScrollUp", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            }
        });

        verticalScrollBar = scrollPane.createVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(HexView.CHAR_HEIGHT);
        verticalScrollBar.setBlockIncrement(HexView.CHAR_HEIGHT * 20);
        verticalScrollBar.setValues(0, 1, 0, HexView.TOTAL_HEIGHT);
        scrollPane.setVerticalScrollBar(verticalScrollBar);

        scrollPane.setViewportView(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.02;
        add(upperPane, gbc);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.LAST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(statusPane, gbc);

        hexPane.addSelectionChangeListener(this);

        KeyStroke jumpKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK);
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(jumpKeyStroke, "jump");
        this.getActionMap().put("jump", jumpToIndex);
    }

    @Override
    public void selectionChanged(SelectEvent event)
    {
        int index = event.getHexIndex();

        if (event.getNavigation().equals(SelectEvent.NEXT))
        {
            index += 1;
        }
        else if (event.getNavigation().equals(SelectEvent.PREVIOUS))
        {
            index -= 1;
        }
        else if (event.getNavigation().equals(SelectEvent.UP))
        {
            index -= 16;

        }
        else if (event.getNavigation().equals(SelectEvent.DOWN))
        {
            index += 16;
        }
        if (index >= 0 && index <= model.size() - 1)
        {
            hexPane.setSelected(index);
            addressPane.setSelected(index);
            asciiPane.setSelected(index);
            statusPane.updateStatus(index);
            selectedIndex = index;
        }
    }

    @Override
    public void blankClick(Point point)
    {
    }

    private JDialog createJumpDialog()
    {
        final JDialog dialog = new JDialog(SwingUtilities.windowForComponent(this), "Jump to index");
        dialog.setLocationRelativeTo(this);
        final JLabel nowLabel = new JLabel("Present index: " + selectedIndex);
        final JLabel label = new JLabel("Index to go:");
        final JTextField field = new JFormattedTextField(NumberFormat.getIntegerInstance());
        field.setPreferredSize(new Dimension(100, 20));

        field.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                int index = Integer.parseInt(field.getText(), 10);
                if (index >= 0 && index <= model.size() - 1)
                {
                    selectionChanged(new SelectEvent(index, SelectEvent.IN));
                    dialog.dispose();
                }
            }
        });

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(nowLabel);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(label);
        inputPanel.add(field);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(panel);
        contentPanel.add(inputPanel);
        dialog.getContentPane().add(contentPanel);
        dialog.pack();
        return dialog;
    }

}
