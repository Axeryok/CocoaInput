package jp.axer.cocoainput.wrapper;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;
import jp.axer.cocoainput.util.Rect;
import jp.axer.cocoainput.util.WrapperUtil;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.util.text.StringTextComponent;

public class EditSignScreenWrapper extends IMEReceiver {
    private EditSignScreen owner;
    private IMEOperator myIME;


    public EditSignScreenWrapper(EditSignScreen field) {
        ModLogger.debug("EditSignScreen init: " + field.hashCode());
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
        myIME.setFocused(true);
    }

    protected void setText(String text) {
    	owner.sign.setMessage(owner.line,new StringTextComponent(text));
    	String [] util = owner.messages;
    	util[owner.line]=text;
    }

	protected String getText() {
		return owner.sign.getMessage(owner.line).getString();
	}

	protected void setCursorInvisible() {} //TODO

	protected int getCursorPos() {
		return owner.signField.getCursorPos();
	}

	protected void setCursorPos(int p) {
		owner.signField.setCursorPos(p,true);
	}

	protected void setSelectionPos(int p) {
		owner.signField.setSelectionRange(p,p);
	}


    @Override
    public Rect getRect() {

        FontRenderer fontRendererObj = null;
        try {
            fontRendererObj = WrapperUtil.makeFontRenderer(owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        float y = 91 + (owner.line - 1) * (10);
        if (!(owner.sign.getBlockState().getBlock() instanceof StandingSignBlock)) {
            y += 30;
        }
        return new Rect(
        		owner.width/2+fontRendererObj.width(owner.sign.getMessage(owner.line).toString().substring(0, originalCursorPosition))/2,
//                owner.width / 2 + fontRendererObj.width(owner.sign.getMessage(owner.line).getString()) / 2,
                y,
                0,
                0
        );
    }

}
