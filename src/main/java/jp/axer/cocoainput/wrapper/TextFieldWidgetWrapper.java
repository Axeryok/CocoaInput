
package jp.axer.cocoainput.wrapper;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;
import jp.axer.cocoainput.util.Rect;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

public class TextFieldWidgetWrapper extends IMEReceiver {
    private IMEOperator myIME;
    private TextFieldWidget owner;

    public TextFieldWidgetWrapper(TextFieldWidget field) {
        ModLogger.debug("TextFieldWidget init: " + field.hashCode());
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
    }

    public void setCanLoseFocus(boolean newParam) {
        if (!newParam) setFocused(true);
    }

    public void setFocused(boolean newParam) {
    	owner.setFormatter( ((abc,def) -> new StringTextComponent(abc).getVisualOrderText()     ));
        myIME.setFocused(newParam);
    }


    public void updateCursorCounter() {
        if (cursorVisible) owner.frame++;
    }

    protected void setText(String text) {
    	owner.value=text;
    }

	protected String getText() {
		return owner.value;
	}

	protected void setCursorInvisible() {
		owner.frame=6;
	}

	protected int getCursorPos() {
		return owner.getCursorPosition();
	}

	protected void setCursorPos(int p) {
		owner.moveCursorTo(p);
	}

	protected void setSelectionPos(int p) {
		owner.setHighlightPos(p);
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

	protected void notifyParent(String text) {
		// TODO 自動生成されたメソッド・スタブ
    	owner.onValueChange(text);

	}


}
