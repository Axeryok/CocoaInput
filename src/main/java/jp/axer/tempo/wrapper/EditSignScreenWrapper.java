package jp.axer.cocoainput.wrapper;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;
import jp.axer.cocoainput.util.PreeditFormatter;
import jp.axer.cocoainput.util.Rect;
import jp.axer.cocoainput.util.Tuple3;
import jp.axer.cocoainput.util.WrapperUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.util.text.StringTextComponent;

public class EditSignScreenWrapper implements IMEReceiver {
    private EditSignScreen owner;
    private IMEOperator myIME;
    private int length = 0;
    private boolean hasMarkedText = false;
    private int lengthBeforeMarkedText;
    boolean cursorVisible = true;
    int originalCursorPosition=0;

    public EditSignScreenWrapper(EditSignScreen field) {
        ModLogger.debug("EditSignScreen init: " + field.hashCode());
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
        myIME.setFocused(true);
    }

    @Override
    public void insertText(String aString, int position1, int length1) {
    	if(!hasMarkedText) {
    		originalCursorPosition=owner.signField.getCursorPos();
    	}
    	hasMarkedText=false;
    	cursorVisible=true;
    	String text = owner.sign.getMessage(owner.line).getString();
    	if(aString.length()==0) {
    		String newEditLine = new StringBuffer(text).replace(originalCursorPosition, originalCursorPosition + length, "").toString();
    		owner.sign.setMessage(owner.line, new StringTextComponent(newEditLine));
    		length=0;
    		owner.signField.setCursorPos(originalCursorPosition,true);
    		owner.signField.setSelectionRange(originalCursorPosition,originalCursorPosition);
    		String[] util = owner.messages;
            util[owner.line] = newEditLine;
    		return;
    	}
    	String newEditLine = new StringBuffer(text).replace(originalCursorPosition, originalCursorPosition + length, aString.substring(0,aString.length())).toString();
    	owner.sign.setMessage(owner.line, new StringTextComponent(newEditLine));
    	String[] util = owner.messages;
        util[owner.line] = newEditLine;
    	length = 0;
		owner.signField.setCursorPos(originalCursorPosition+aString.length(),true);
		
		

    }

    @Override
    public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
    	if(!this.hasMarkedText) {
    		this.originalCursorPosition=owner.signField.getCursorPos();
    		this.hasMarkedText=true;
    	}
    	Tuple3<String,Integer,Boolean> formattedText=PreeditFormatter.formatMarkedText(aString, position1, length1);
    	String str = formattedText._1();
    	int caretPosition=formattedText._2()+4;
    	boolean hasCaret = formattedText._3();
    	String text = owner.sign.getMessage(owner.line).getString();
    	String newEditLine = new StringBuffer(text).replace(originalCursorPosition, originalCursorPosition + length, str).toString();
    	owner.sign.setMessage(owner.line, new StringTextComponent(newEditLine));
    	String[] util = owner.messages;
        util[owner.line] = newEditLine;
    	length=str.length();
    	if(hasCaret) {
    		this.cursorVisible=true;
    		owner.signField.setCursorPos(originalCursorPosition+caretPosition,true);
    		owner.signField.setSelectionRange(originalCursorPosition+caretPosition,originalCursorPosition+caretPosition);
    	}
    	else {
    		this.cursorVisible=false;
    		/*TODO need to enable -> owner.frame=6*/
    		owner.signField.setCursorPos(originalCursorPosition,true);
    		owner.signField.setSelectionRange(originalCursorPosition,originalCursorPosition);
    		
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
        float y = 91 + (owner.line - 1) * (10);
        if (owner.sign.getBlockState().getBlock().getRegistryName().toString().contains("wall")) {
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
