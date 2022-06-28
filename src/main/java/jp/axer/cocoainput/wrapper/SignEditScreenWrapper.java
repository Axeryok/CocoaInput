package jp.axer.cocoainput.wrapper;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;
import jp.axer.cocoainput.util.Rect;
import jp.axer.cocoainput.util.WrapperUtil;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.network.chat.Component;

public class SignEditScreenWrapper extends IMEReceiver {
    private SignEditScreen owner;
    private IMEOperator myIME;


    public SignEditScreenWrapper(SignEditScreen field) {
        ModLogger.debug("SignEditScreen init: " + field.hashCode());
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
        myIME.setFocused(true);
    }

    protected void setText(String text) {
    	owner.sign.setMessage(owner.line,Component.literal(text));
    	String [] util = owner.messages;
    	util[owner.line]=text;
    }

	protected String getText() {
		return owner.sign.getMessage(owner.line,false).getString();
	}

	protected void setCursorInvisible() {
		owner.frame=6;
	} //TODO

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

        Font fontRendererObj = null;
        try {
            fontRendererObj = WrapperUtil.makeFont(owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        float y = 91 + (owner.line - 1) * (10);
        if (!(owner.sign.getBlockState().getBlock() instanceof StandingSignBlock)) {
            y += 30;
        }
        return new Rect(
        		owner.width/2+fontRendererObj.width(owner.sign.getMessage(owner.line,false).toString().substring(0, originalCursorPosition))/2,
//                owner.width / 2 + fontRendererObj.width(owner.sign.getMessage(owner.line,false).getString()) / 2,
                y,
                0,
                0
        );
    }
    public int renewCursorCounter() {
        return owner.frame+(cursorVisible?1:0);
    }

}
