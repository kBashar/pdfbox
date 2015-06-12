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
package org.apache.pdfbox.tools.pdfdebugger.colorpane;

import java.awt.Color;

/**
 * @author Khyrul Bashar.
 */
public class IndexdColorant
{
    private String name;
    private Color maximum;
    private Color minimum;

    /**
     * Constructor
     */
    public IndexdColorant()
    {
    }

    /**
     * Constructor
     * @param colorantName String instance
     * @param maxi maximum color
     * @param mini minimum color
     */
    public IndexdColorant(String colorantName, Color maxi, Color mini)
    {
        name = colorantName;
        maximum = maxi;
        minimum = mini;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Color getMaximum()
    {
        return maximum;
    }

    public void setMaximum(Color maximum)
    {
        this.maximum = maximum;
    }

    public Color getMinimum()
    {
        return minimum;
    }

    public void setMinimum(Color minimum)
    {
        this.minimum = minimum;
    }
}
