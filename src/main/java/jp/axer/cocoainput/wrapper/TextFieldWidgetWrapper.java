
package jp.axer.cocoainput.wrapper;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;
import jp.axer.cocoainput.util.PreeditFormatter;
import jp.axer.cocoainput.util.Rect;
import jp.axer.cocoainput.util.Tuple3;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.StringTextComponent;

public class TextFieldWidgetWrapper implements IMEReceiver {
    private IMEOperator myIME;
    private TextFieldWidget owner;
    private int length = 0;
    private boolean cursorVisible = true;
    private boolean preeditBegin = false;
    private int originalCursorPosition = 0;//絶対的
    private boolean canLoseFocus = false;

    public TextFieldWidgetWrapper(TextFieldWidget field) {
        ModLogger.debug("TextFieldWidget init: " + field.hashCode());
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
    }

    public void setCanLoseFocus(boolean newParam) {
        if (!newParam) setFocused(true);
    }

    public void setFocused(boolean newParam) {
        myIME.setFocused(newParam);
    }

    @Override
    public void insertText(String aString, int position1, int length1) {
        if (!preeditBegin) {
            originalCursorPosition = owner.getCursorPosition();
        }
        preeditBegin = false;
        cursorVisible = true;
        if (aString.length() == 0) {
            owner.text = (new StringBuffer(owner.getText())).replace(originalCursorPosition, originalCursorPosition + length, "").toString();
            length = 0;
            owner.setCursorPosition(originalCursorPosition);
            owner.setSelectionPos(originalCursorPosition);
            return;
        }
        owner.text = (new StringBuffer(owner.getText()))
                .replace(originalCursorPosition, originalCursorPosition + length, aString.substring(0, aString.length()))
                .toString();
        length = 0;
        owner.setCursorPosition(originalCursorPosition + aString.length());
        //owner.selectionEnd = owner.cursorPosition;
    }

    @Override
    public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
        if (!preeditBegin) {
            originalCursorPosition = owner.getCursorPosition();
            preeditBegin = true;
        }
        owner.setTextFormatter ( ((abc,def) -> new StringTextComponent(abc).func_241878_f()     ));
        Tuple3<String, Integer, Boolean> formattedText = PreeditFormatter.formatMarkedText(aString, position1, length1);
        String str = formattedText._1();
        int caretPosition = formattedText._2()+4;//相対値
        boolean hasCaret = formattedText._3();
        owner.text = (new StringBuffer(owner.getText())).replace(originalCursorPosition, originalCursorPosition + length, str).toString();
        length = str.length();
        if (hasCaret) {
            this.cursorVisible = true;
            owner.setCursorPosition(originalCursorPosition + caretPosition);
            owner.setSelectionPos(originalCursorPosition + caretPosition);
        } else {
            this.cursorVisible = false;
            owner.cursorCounter = 6;
            owner.setCursorPosition(originalCursorPosition);
            //owner.selectionEnd=owner.cursorPosition;
        }

    }

    public void updateCursorCounter() {
        if (cursorVisible) owner.cursorCounter++;
    }

    @Override
    public Rect getRect() {
        return new Rect(//{x,y}
                (owner.fontRenderer.getStringWidth(owner.getText().substring(0, originalCursorPosition)) + (owner.enableBackgroundDrawing ? owner.x + 4 : owner.x)),
                (owner.fontRenderer.FONT_HEIGHT + (owner.enableBackgroundDrawing ? owner.y + (owner.getHeightRealms() - 8) / 2 : owner.y)),
                owner.getWidth(),
                owner.getHeightRealms()

        );
    }


}
