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
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
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
    private boolean isContentStream = false;

    public StreamPane(COSStream cosStream, COSName streamKey)
    {
        if (COSName.CONTENTS.equals(streamKey))
        {
            isContentStream = true;
        }
        this.stream = new Stream(cosStream);

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

        if (actionCommand.equals("Image"))
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

        private InputStream inputStream;
        private final String filterKey;

        private DocumentCreator(String filterKey)
        {
            this.filterKey = filterKey;
            this.inputStream = stream.getStream(filterKey);
            ;
        }

        @Override
        protected StyledDocument doInBackground() throws Exception
        {
            if (isContentStream && Stream.UNFILTERED.equals(filterKey))
            {
                return getContentStreamDocument(inputStream);
            }

            return getDocument(inputStream);
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
                while ((amountRead = ioStream.read(buffer, 0, buffer.length)) != -1)
                {
                    byteArray.write(buffer, 0, amountRead);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return byteArray.toString();
        }


        private StyledDocument getDocument(InputStream inputStream)
        {
            String data = getStringOfStream(inputStream);
            StyledDocument document = new DefaultStyledDocument();
            try
            {
                document.insertString(0, data, null);
            }
            catch (BadLocationException e)
            {
                e.printStackTrace();
            }
            return document;
        }

        private StyledDocument getContentStreamDocument(InputStream inputStream)
        {
            StyledDocument docu = new DefaultStyledDocument();

            PDFStreamParser parser = null;
            try
            {
                parser = new PDFStreamParser(new RandomAccessBuffer(inputStream));
                parser.parse();
                for (Object obj : parser.getTokens())
                {
                    if (obj instanceof Operator)
                    {
                        docu.insertString(docu.getLength(), ((Operator) obj).getName() + "\n", null);
                    }
                    else
                    {
                        String str = "";
                        if (obj instanceof COSName)
                        {
                            str = "/" + ((COSName) obj).getName();
                        }
                        else if (obj instanceof COSArray)
                        {
                            StringBuilder builder = new StringBuilder("[ ");
                            for (COSBase base : (COSArray) obj)
                            {
                                builder.append(getCOSVlaue(base));
                                builder.append(", ");
                            }
                            if (((COSArray) obj).size() > 0)
                            {
                                builder.delete(builder.lastIndexOf(","), builder.length());
                            }
                            builder.append("]");
                            str = builder.toString();
                        }
                        else
                        {
                            str = getCOSVlaue(obj);
                        }
                        docu.insertString(docu.getLength(), str+"  ", null);
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (BadLocationException e1)
            {
                e1.printStackTrace();
            }
            return docu;
        }

        private String getCOSVlaue(Object obj)
        {
            String str = obj.toString();
            str = str.substring(str.indexOf("{")+1, str.length()-1);
            if (obj instanceof COSString)
            {
                str = "("+str+")";
            }
            return str;
        }

        }
    }
