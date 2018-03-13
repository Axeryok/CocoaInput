package com.Axeryok.CocoaInput.wrapper;

import java.lang.reflect.Method;

import org.lwjgl.input.Keyboard;

import com.Axeryok.CocoaInput.CocoaInput;
import com.Axeryok.CocoaInput.DeobfuscationHelper;
import com.Axeryok.CocoaInput.ModLogger;
import com.Axeryok.CocoaInput.Rect;
import com.Axeryok.CocoaInput.plugin.IMEOperator;
import com.Axeryok.CocoaInput.plugin.IMEReceiver;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import scala.Tuple3;

public class GuiTextFieldWrapper implements IMEReceiver {
	private IMEOperator myIME;
	private GuiTextField owner;
	private int length=0;
	private boolean cursorVisible=true;
	private boolean preeditBegin=false;
	private int originalCursorPosition=0;//絶対的
	public GuiTextFieldWrapper(GuiTextField field){
		owner=field;
		myIME = CocoaInput.controller.generateIMEOperator(this);
	}

	public void setFocused(boolean newParam,boolean oldParam){
		if(newParam!=oldParam){
			myIME.setFocused(newParam);
		}
	}
	
	@Override
	public void insertText(String aString, int position1, int length1) {
		if(!preeditBegin){
			originalCursorPosition=owner.cursorPosition;
		}
		preeditBegin=false;
		cursorVisible=true;
		if (aString.length() == 0) {
			owner.text = (new StringBuffer(owner.text)).replace(originalCursorPosition, originalCursorPosition + length, "").toString();
			length = 0;
			owner.cursorPosition = originalCursorPosition;
			owner.selectionEnd = owner.cursorPosition;
			return;
		}
		owner.text = (new StringBuffer(owner.text))
				.replace(originalCursorPosition, originalCursorPosition + length, aString.substring(0, aString.length() - 1))
				.toString();
		length = 0;
		owner.cursorPosition = originalCursorPosition+aString.length() - 1;
		owner.selectionEnd = owner.cursorPosition;
		GuiScreen gui=Minecraft.getMinecraft().currentScreen;
		Method keytyped = null;
		try{
			keytyped= gui.getClass().getDeclaredMethod(DeobfuscationHelper.unmap("func_73869_a"), char.class,int.class);
		}catch(Exception e){
			try{
				keytyped= gui.getClass().getDeclaredMethod("func_73869_a", char.class,int.class);
			}catch(Exception e2){
				try{
					keytyped= gui.getClass().getDeclaredMethod("keyTyped", char.class,int.class);
				}catch(Exception e3){
					ModLogger.error("Can't find method:keyTyped class:"+gui.getClass().getName());
					owner.textboxKeyTyped(aString.charAt(aString.length()-1),Keyboard.KEY_UNDERLINE);
				}
			}
		}

		try{
			keytyped.setAccessible(true);
			keytyped.invoke(gui, aString.charAt(aString.length()-1),Keyboard.KEY_UNDERLINE);
		}catch(Exception e){

		}
	}

	@Override
	public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
		if(!preeditBegin){
			originalCursorPosition=owner.cursorPosition;
			preeditBegin=true;
		}
		Tuple3<String,Integer,Boolean> formattedText = CocoaInput.formatMarkedText(aString, position1, length1);
		String str = formattedText._1();
		int caretPosition=formattedText._2();//相対値
		boolean hasCaret=formattedText._3();
		if(hasCaret){
			this.cursorVisible=true;
			owner.cursorPosition=originalCursorPosition+caretPosition;
			owner.selectionEnd=owner.cursorPosition;
		}else{
			this.cursorVisible=false;
			owner.cursorCounter=6;
			owner.cursorPosition=originalCursorPosition;
			owner.selectionEnd=owner.cursorPosition;
		}
		owner.text = (new StringBuffer(owner.text)).replace(originalCursorPosition, originalCursorPosition + length, str).toString();
		length = str.length();
	}

	public void updateCursorCounter(){
		if(cursorVisible)owner.cursorCounter++;
	}
	@Override
	public Rect getRect() {
		return new Rect(//{x,y}
				(owner.fontRendererInstance.getStringWidth(owner.getText().substring(0, originalCursorPosition))+ (owner.enableBackgroundDrawing ? owner.xPosition + 4 : owner.xPosition)),
				(owner.fontRendererInstance.FONT_HEIGHT+(owner.enableBackgroundDrawing ? owner.yPosition + (owner.height - 8) / 2 : owner.yPosition)),
				owner.width,
				owner.height

		);
	}
	

}
