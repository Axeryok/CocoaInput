package com.Axeryok.CocoaInput.wrapper;

import java.io.IOException;
import java.lang.reflect.Method;

import org.lwjgl.input.Keyboard;

import com.Axeryok.CocoaInput.CocoaInput;
import com.Axeryok.CocoaInput.Rect;
import com.Axeryok.CocoaInput.plugin.IMEOperator;
import com.Axeryok.CocoaInput.plugin.IMEReceiver;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextFieldWrapper implements IMEReceiver {
	IMEOperator myIME;
	public GuiTextField owner;
	int length=0;
	public GuiTextFieldWrapper(GuiTextField field){
		myIME = CocoaInput.controller.generateIMEOperator(this);
		owner=field;
	}
	
	public void setFocused(boolean newParam,boolean oldParam){
		if(newParam!=oldParam){
			myIME.setFocused(newParam);
		}
	}
	
	@Override
	public void insertText(String aString, int position1, int length1) {
		if (aString.length() == 0) {
			owner.text = (new StringBuffer(owner.text)).replace(owner.cursorPosition, owner.cursorPosition + length, "").toString();
			length = 0;
			return;
		}
		owner.text = (new StringBuffer(owner.text))
				.replace(owner.cursorPosition, owner.cursorPosition + length, aString.substring(0, aString.length() - 1))
				.toString();
		length = 0;
		owner.cursorPosition += aString.length() - 1;
		owner.selectionEnd = owner.cursorPosition;
		GuiScreen gui=Minecraft.getMinecraft().currentScreen;
		Method keytyped = null;
		try{
			keytyped= gui.getClass().getDeclaredMethod("func_73869_a", char.class,int.class);
		}
		catch(Exception e){
			try{
				keytyped= gui.getClass().getDeclaredMethod("keyTyped", char.class,int.class);
			}
			catch(Exception e2){
				
			}
		}
		
		try{
			keytyped.setAccessible(true);
			keytyped.invoke(gui, aString.charAt(aString.length()-1),Keyboard.KEY_UNDERLINE);
		}
		catch(Exception e){
			
		}
	}

	@Override
	public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
		String str = CocoaInput.formatMarkedText(aString, position1, length1);
		owner.text = (new StringBuffer(owner.text)).replace(owner.cursorPosition, owner.cursorPosition + length, str).toString();
		length = str.length();
	}

	@Override
	public Rect getRect() {
		return new Rect(//{x,y}
				(owner.fontRendererInstance.getStringWidth(owner.getText().substring(0, owner.cursorPosition))+ (owner.enableBackgroundDrawing ? owner.xPosition + 4 : owner.xPosition)),
				(owner.fontRendererInstance.FONT_HEIGHT+(owner.enableBackgroundDrawing ? owner.yPosition + (owner.height - 8) / 2 : owner.yPosition)),
				owner.width,
				owner.height
		
		);
	}

}
