package com.Axeryok.CocoaInput.wrapper;

import com.Axeryok.CocoaInput.CocoaInput;
import com.Axeryok.CocoaInput.impl.IMEOperator;
import com.Axeryok.CocoaInput.impl.IMEReceiver;

import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.util.text.TextComponentString;

public class GuiEditSignWrapper implements IMEReceiver {
	IMEOperator myIME;
	public GuiEditSign owner;
	int length=0;
	boolean hasMarkedText=false;
    int lengthBeforeMarkedText;
    
    public GuiEditSignWrapper(GuiEditSign field){
    	owner=field;
    	myIME=CocoaInput.controller.generateIMEOperator(this);
    	myIME.setFocused(true);
    }
	
	
	@Override
	public void insertText(String aString, int position1, int length1) {
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
		String str=CocoaInput.formatMarkedText(aString, position1, length1);
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
	public float[] getRectPoint() {
		return null;
	}

}
