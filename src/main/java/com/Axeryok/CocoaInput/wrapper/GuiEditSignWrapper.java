package com.Axeryok.CocoaInput.wrapper;

import com.Axeryok.CocoaInput.CocoaInput;
import com.Axeryok.CocoaInput.Rect;
import com.Axeryok.CocoaInput.plugin.IMEOperator;
import com.Axeryok.CocoaInput.plugin.IMEReceiver;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.init.Blocks;
import net.minecraft.util.text.TextComponentString;

public class GuiEditSignWrapper implements IMEReceiver {
	private GuiEditSign owner;
	private IMEOperator myIME;
	private int length=0;
	private boolean hasMarkedText=false;
    private int lengthBeforeMarkedText;
    
    public GuiEditSignWrapper(GuiEditSign field){
    	owner=field;
    	myIME=CocoaInput.controller.generateIMEOperator(this);
    	myIME.setFocused(true);
    }
	
	@Override
	public void insertText(String aString, int position1, int length1){
		String text = owner.tileSign.signText[owner.editLine]
		        .getUnformattedText();
		        owner.tileSign.signText[owner.editLine] = new TextComponentString(
		                                                                        new StringBuffer(text).replace(lengthBeforeMarkedText, lengthBeforeMarkedText + length, aString)
		                                                                        .toString());
		        hasMarkedText = false;
		        length = 0;
	}

	@Override
	public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
		String str=CocoaInput.formatMarkedText(aString, position1, length1)._1();
        String text = owner.tileSign.signText[owner.editLine].getUnformattedText();
        if (hasMarkedText == false) {
            hasMarkedText = true;
            lengthBeforeMarkedText = text.length();
        }
        owner.tileSign.signText[owner.editLine] = new TextComponentString(
                                                                        new StringBuffer(text).replace(lengthBeforeMarkedText, lengthBeforeMarkedText + length, str)
                                                                        .toString());
        length = str.length();
	}

	@Override
	public Rect getRect() {
		FontRenderer fontRendererObj=null;
		try {
		    fontRendererObj = WrapperUtil.makeFontRenderer(owner);
		}catch(Exception e) {
			e.printStackTrace();
		}
		float y=91+(owner.editLine-1)*(10);
		if(owner.tileSign.getBlockType()!=Blocks.STANDING_SIGN){
			y+=30;
		}
		return new Rect(owner.width/2+fontRendererObj.getStringWidth(owner.tileSign.signText[owner.editLine].getUnformattedText())/2, y, 0, 0);
	}

}
