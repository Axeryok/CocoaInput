package com.Axeryok.CocoaInput.wrapper;

import java.lang.reflect.Field;

import com.Axeryok.CocoaInput.CocoaInput;
import com.Axeryok.CocoaInput.Rect;
import com.Axeryok.CocoaInput.impl.IMEOperator;
import com.Axeryok.CocoaInput.impl.IMEReceiver;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.init.Blocks;
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
		float y=91+(owner.editLine-1)*(10);
		if(owner.tileSign.getBlockType()!=Blocks.STANDING_SIGN){
			y+=30;
		}
		return new Rect(
				owner.width/2+fontRendererObj.getStringWidth(owner.tileSign.signText[owner.editLine].getUnformattedText())/2,
				y,
				0,
				0);
	}

}
