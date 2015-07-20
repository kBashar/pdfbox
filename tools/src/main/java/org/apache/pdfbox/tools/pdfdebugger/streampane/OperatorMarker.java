package org.apache.pdfbox.tools.pdfdebugger.streampane;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * @author Khyrul Bashar
 */
final class OperatorMarker
{
    Map<String, Style> operatorStyleMap;

    OperatorMarker()
    {
        operatorStyleMap = new HashMap<String, Style>();
        initOperatorStyles();
    }

    private void initOperatorStyles()
    {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();

        Style common = styleContext.addStyle("common", null);
        StyleConstants.setBold(common, true);

        Style textObjectStyle = styleContext.addStyle("text_object", common);
        StyleConstants.setForeground(textObjectStyle, Color.GREEN);

        Style graphicsStyle = styleContext.addStyle("graphics", common);
        StyleConstants.setForeground(graphicsStyle, Color.RED);

        final String BEGIN_TEXT_OBJECT = "BT";
        final String END_TEXT_OBJECT = "ET";
        final String SAVE_GRAPHICS_STATE = "q";
        final String RESTORE_GRAPHICS_STATE = "Q";

        operatorStyleMap.put(BEGIN_TEXT_OBJECT, textObjectStyle);
        operatorStyleMap.put(END_TEXT_OBJECT, textObjectStyle);
        operatorStyleMap.put(SAVE_GRAPHICS_STATE, graphicsStyle);
        operatorStyleMap.put(RESTORE_GRAPHICS_STATE, graphicsStyle);
    }

    Style getStyleForOperator(String operator)
    {
        if (operatorStyleMap.containsKey(operator))
        {
            return operatorStyleMap.get(operator);
        }
        return null;
    }
}
