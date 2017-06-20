package com.Axeryok.CocoaInput.wrapper;

import com.Axeryok.CocoaInput.CocoaInput;
import com.Axeryok.CocoaInput.impl.IMEOperator;
import com.Axeryok.CocoaInput.impl.IMEReceiver;

import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiTextField;

public class GuiScreenBookWrapper implements IMEReceiver {

	IMEOperator myIME;
	public GuiScreenBook owner;
	int length=0;
	boolean hasMarkedText=false;
    int lengthBeforeMarkedText;
    
	public GuiScreenBookWrapper(GuiScreenBook field){
		if(field.bookIsUnsigned){
			owner=field;
			myIME = CocoaInput.controller.generateIMEOperator(this);
			myIME.setFocused(true);
		}
	}
	
	
	@Override
	public void insertText(String aString, int position1, int length1) {
		if (owner.bookGettingSigned) {
            owner.bookTitle = new StringBuffer(owner.bookTitle).replace(lengthBeforeMarkedText,
                                                            lengthBeforeMarkedText + length, aString).toString();
            hasMarkedText = false;
            length = 0;
        } else {
            owner.pageSetCurrent(new StringBuffer(owner.pageGetCurrent()).replace(
                                                                           lengthBeforeMarkedText, lengthBeforeMarkedText + length, aString).toString());
            hasMarkedText = false;
            length = 0;
        }
	}

	@Override
	public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
		String str=CocoaInput.formatMarkedText(aString, position1, length1);
        if (owner.bookGettingSigned) {
            if (hasMarkedText == false) {
                hasMarkedText = true;
                lengthBeforeMarkedText = owner.bookTitle.length();
            }
            owner.bookTitle = new StringBuffer(owner.bookTitle).replace(lengthBeforeMarkedText,
                                                            lengthBeforeMarkedText + length, str).toString();
            length = str.length();
        } else {
            if (hasMarkedText == false) {
                hasMarkedText = true;
                lengthBeforeMarkedText = owner.pageGetCurrent().length();
            }
            owner.pageSetCurrent(new StringBuffer(owner.pageGetCurrent()).replace(
                                                                           lengthBeforeMarkedText, lengthBeforeMarkedText + length, str).toString());
            length = str.length();
        }
	}

	@Override
	public float[] getRectPoint() {
		return null;
	}

}
