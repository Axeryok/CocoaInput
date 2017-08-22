package com.Axeryok.CocoaInput.wrapper;

import java.lang.reflect.Field;
import java.util.List;

import com.Axeryok.CocoaInput.CocoaInput;
import com.Axeryok.CocoaInput.Rect;
import com.Axeryok.CocoaInput.impl.IMEOperator;
import com.Axeryok.CocoaInput.impl.IMEReceiver;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
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
	public Rect getRect() {
		FontRenderer fontRendererObj=null;
		try {
			Field font=GuiScreen.class.getDeclaredField("field_146289_q");
			font.setAccessible(true);
			fontRendererObj=(FontRenderer) font.get(owner);
		} catch (Exception e) {
			try {
				Field font = GuiScreen.class.getDeclaredField("fontRendererObj");
				font.setAccessible(true);
				fontRendererObj=(FontRenderer) font.get(owner);
			}
			catch (Exception e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			
		}
		if(owner.bookGettingSigned){
			return new Rect(
					( fontRendererObj.getStringWidth(owner.bookTitle)/2+((owner.width - 192) / 2)+36+(116-0)/2 ),
					(50+fontRendererObj.FONT_HEIGHT),
					0,
					0
					);
		}
		else{
			List<String> lines=fontRendererObj.listFormattedStringToWidth(owner.pageGetCurrent(),116 );
			return new Rect(
					(((owner.width - 192) / 2)+36+fontRendererObj.getStringWidth(lines.get(lines.size()-1))),
					(34+lines.size()*fontRendererObj.FONT_HEIGHT),
					0,
					0
					);
		}
	}

}
