package org.apache.pdfbox.tools.pdfdebugger.hexviewer;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JFrame;

/**
 * @author Khyrul Bashar
 */
class Main
{
    public static void main(String[] args) throws IOException
    {
        FileInputStream stream = new FileInputStream("README.txt");
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(new HexView(toByteArray(stream)).getPane());
        //frame.setPreferredSize(new Dimension(690, 350));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static byte[] toByteArray(InputStream is) throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }
}
