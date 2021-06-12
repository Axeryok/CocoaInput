
package jp.axer.cocoainput.wrapper;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;
import jp.axer.cocoainput.util.PreeditFormatter;
import jp.axer.cocoainput.util.Rect;
import jp.axer.cocoainput.util.Tuple3;
import net.minecraft.client.gui.widget.TextFieldWidget;
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
            owner.value = (new StringBuffer(owner.getValue())).replace(originalCursorPosition, originalCursorPosition + length, "").toString();
            length = 0;
            owner.moveCursorTo(originalCursorPosition);
            owner.setHighlightPos(originalCursorPosition);
            return;
        }
        owner.value = (new StringBuffer(owner.getValue()))
                .replace(originalCursorPosition, originalCursorPosition + length, aString.substring(0, aString.length()))
                .toString();
        length = 0;
        owner.moveCursorTo(originalCursorPosition + aString.length());
        //owner.selectionEnd = owner.cursorPosition;
    }

    @Override
    public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
        if (!preeditBegin) {
            originalCursorPosition = owner.getCursorPosition();
            preeditBegin = true;
        }
        owner.setFormatter( ((abc,def) -> new StringTextComponent(abc).getVisualOrderText()     ));
        Tuple3<String, Integer, Boolean> formattedText = PreeditFormatter.formatMarkedText(aString, position1, length1);
        String str = formattedText._1();
        int caretPosition = formattedText._2()+4;//相対値
        boolean hasCaret = formattedText._3();
        owner.value= (new StringBuffer(owner.getValue())).replace(originalCursorPosition, originalCursorPosition + length, str).toString();
        length = str.length();
        if (hasCaret) {
            this.cursorVisible = true;
            owner.moveCursorTo(originalCursorPosition + caretPosition);
            owner.setHighlightPos(originalCursorPosition + caretPosition);
        } else {
            this.cursorVisible = false;
            owner.frame = 6;
            owner.moveCursorTo(originalCursorPosition);
            //owner.selectionEnd=owner.cursorPosition;
        }

    }

    public void updateCursorCounter() {
        if (cursorVisible) owner.frame++;
    }

    @Override
    public Rect getRect() {
        return new Rect(//{x,y}
                (owner.font.width(owner.getValue().substring(0, originalCursorPosition)) + (owner.bordered ? owner.x + 4 : owner.x)),
                (owner.font.lineHeight + (owner.bordered ? owner.y + (owner.getHeight() - 8) / 2 : owner.y)),
                owner.getWidth(),
                owner.getHeight()

        );
    }


}
