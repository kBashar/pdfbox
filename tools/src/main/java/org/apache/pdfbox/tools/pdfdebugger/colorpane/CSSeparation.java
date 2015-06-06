package org.apache.pdfbox.tools.pdfdebugger.colorpane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.pdmodel.graphics.color.PDSeparation;

/**
 * Author by Khyrul Bashar.
 */
public class CSSeparation implements ChangeListener, ActionListener
{
    private JSlider slider;
    private JTextField inputField;
    private JLabel colorBar;
    private JPanel panel;

    private PDSeparation separation;
    private float tintValue = 0;

    public CSSeparation(COSArray array)
    {
        try
        {
            separation = new PDSeparation(array);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        initUI();
        initValues();
    }

    private void initUI()
    {
        panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;

        slider = new JSlider(0, 100, 50);
        //slider.setPreferredSize(new Dimension(200, 40));
        slider.addChangeListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(slider, gbc);

        inputField = new JTextField();
        //inputField.setPreferredSize(new Dimension(35, 25));
        inputField.addActionListener(this);

        gbc.gridx = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(inputField, gbc);

        colorBar = new JLabel();
        //colorBar.setPreferredSize(new Dimension(100, 100));
        colorBar.setForeground(Color.BLUE);
        colorBar.setBackground(Color.BLUE);
        colorBar.setOpaque(true);

        gbc.gridx = 5;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(colorBar, gbc);
    }

    private void initValues()
    {
        slider.setValue(getIntRepresentation(tintValue));
        inputField.setText(Float.toString(tintValue));
    }

    public JPanel getPanel()
    {
        return panel;
    }

    public static void main(String[] args)
    {
        CSSeparation s = new CSSeparation(new COSArray());
        JFrame frame  = new JFrame();
        frame.setSize(500, 300);
        frame.getContentPane().add(s.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    //input changed in slider
    @Override
    public void stateChanged(ChangeEvent changeEvent)
    {
        if (!slider.getValueIsAdjusting())
        {
            int value = slider.getValue();
            tintValue = getFloatRepresentation(value);
            inputField.setText(Float.toString(tintValue));
            updateColorBar();
        }
    }

    //input changed in text field.
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        String input = inputField.getText();
        try
        {
            tintValue= Float.parseFloat(input);
            slider.setValue(getIntRepresentation(tintValue));
            updateColorBar();
        }
        catch (NumberFormatException e)
        {
            inputField.setText(Float.toString(tintValue));
        }
    }

    private void updateColorBar()
    {
        try
        {
            float[] rgbValues = separation.toRGB(new float[] {tintValue});
            colorBar.setBackground(new Color(rgbValues[0], rgbValues[1], rgbValues[2]));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private float getFloatRepresentation(int value)
    {
        return (float) value/100;
    }

    private int getIntRepresentation(float value)
    {
        return (int) (value*100);
    }
}
