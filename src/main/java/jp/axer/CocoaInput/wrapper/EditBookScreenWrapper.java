package jp.axer.cocoainput.wrapper;

import java.util.List;


import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.PreeditFormatter;
import jp.axer.cocoainput.util.Rect;
import jp.axer.cocoainput.util.WrapperUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.EditBookScreen;

public class EditBookScreenWrapper implements IMEReceiver {
    private IMEOperator myIME;
    private EditBookScreen owner;
    private int length = 0;
    private boolean hasMarkedText = false;
    private int lengthBeforeMarkedText;

    public EditBookScreenWrapper(EditBookScreen field) {
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
        myIME.setFocused(true);
    }

    @Override
    public void insertText(String aString, int position1, int length1) {
        if (owner.field_214235_d) {
            owner.field_214239_h = new StringBuffer(owner.field_214239_h).replace(lengthBeforeMarkedText,
                    lengthBeforeMarkedText + length, aString).toString();
            hasMarkedText = false;
            length = 0;
        } else {
            owner.func_214217_j(new StringBuffer(owner.func_214193_h()).replace(
                    lengthBeforeMarkedText, lengthBeforeMarkedText + length, aString).toString());
            hasMarkedText = false;
            length = 0;
        }
    }

    @Override
    public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
        String str = PreeditFormatter.formatMarkedText(aString, position1, length1)._1();
        if (owner.field_214235_d) {
            if (hasMarkedText == false) {
                hasMarkedText = true;
                lengthBeforeMarkedText = owner.field_214239_h.length();
            }
            owner.field_214239_h = new StringBuffer(owner.field_214239_h).replace(lengthBeforeMarkedText,
                    lengthBeforeMarkedText + length, str).toString();
            length = str.length();
        } else {
            if (hasMarkedText == false) {
                hasMarkedText = true;
                lengthBeforeMarkedText = owner.func_214193_h().length();
            }
            owner.func_214217_j(new StringBuffer(owner.func_214193_h()).replace(
                    lengthBeforeMarkedText, lengthBeforeMarkedText + length, str).toString());
            length = str.length();
        }
    }

    @Override
    public Rect getRect() {
        FontRenderer fontRendererObj = null;
        try {
            fontRendererObj = WrapperUtil.makeFontRenderer(owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (owner.field_214235_d) {
            return new Rect(
                    (fontRendererObj.getStringWidth(owner.field_214239_h) / 2 + ((owner.width - 192) / 2) + 36 + (116 - 0) / 2),
                    (50 + fontRendererObj.FONT_HEIGHT),
                    0,
                    0
            );
        } else {
            List<String> lines = fontRendererObj.listFormattedStringToWidth(owner.func_214193_h(), 116);
            return new Rect(
                    (((owner.width - 192) / 2) + 36 + fontRendererObj.getStringWidth(lines.get(lines.size() - 1))),
                    (34 + lines.size() * fontRendererObj.FONT_HEIGHT),
                    0,
                    0
            );
        }
    }

}