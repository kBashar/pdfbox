/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.pdfbox.tools.pdfdebugger.streampane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.tools.util.FileOpenSaveDialog;

/**
 * @author Khyrul Bashar
 */
public class StreamPane implements ActionListener
{
    private StreamPaneView view;
    private Stream stream;
    private String currentFilter;
    private PDResources resources;

    public StreamPane(COSStream cosStream, COSDictionary pageForObject)
    {
        this.stream = new Stream(cosStream);
        this.resources = new PDPage(pageForObject).getResources();

        currentFilter = Stream.UNFILTERED;
        view = new StreamPaneView(stream.isImage(), stream.getFilterList(), currentFilter, this);

        requestStreamText(currentFilter);
    }

    public JPanel getPanel()
    {
        return view.getStreamPanel();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        String actionCommand = actionEvent.getActionCommand();

        if (actionCommand.equals("Save"))
        {
            saveRequested();
        }
        else if (actionCommand.equals("Image"))
        {
            requestImageShowing();
        }
        else if (actionCommand.equals("comboBoxChanged"))
        {
            JComboBox<String> comboBox = (JComboBox<String>) actionEvent.getSource();
            currentFilter = (String) comboBox.getSelectedItem();
            requestStreamText(currentFilter);
        }
    }

    private void saveRequested()
    {
        FileOpenSaveDialog saveDialog = new FileOpenSaveDialog(view, null);
            try
            {
                InputStream currentInputStream = stream.getStream(currentFilter);
                saveDialog.saveFile(IOUtils.toByteArray(currentInputStream));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
    }

    private void requestImageShowing()
    {
        if (stream.isImage() && resources != null)
        {
           view.showStreamImage(stream.getImage(resources));
        }
    }

    private void requestStreamText(String command)
    {
        new DocumentCreator(command).execute();
    }

    private class DocumentCreator extends SwingWorker<StyledDocument, Integer>
    {

        private final InputStream inputStream;

        private DocumentCreator(String filterKey)
        {
            this.inputStream = stream.getStream(filterKey);;
        }

        @Override
        protected StyledDocument doInBackground() throws Exception
        {
            String data = getStringOfStream(inputStream);
            return getDocument(data);
        }

        @Override
        protected void done()
        {
            try
            {
                view.showStreamText(get());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }

        private String getStringOfStream(InputStream ioStream)
        {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int amountRead;
            try
            {
                while( (amountRead = ioStream.read( buffer, 0, buffer.length ) ) != -1 )
                {
                    byteArray.write( buffer, 0, amountRead );
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return byteArray.toString();
        }


        private StyledDocument getDocument(String data)
        {
            StyledDocument document = new DefaultStyledDocument();
            try
            {
                document.insertString(0, data, null);
                return document;
            }
            catch (BadLocationException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}
