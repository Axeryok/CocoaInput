package com.Axeryok.CocoaInput.arch.darwin;

import java.util.UUID;

import com.Axeryok.CocoaInput.CocoaInput;
import com.Axeryok.CocoaInput.ModLogger;
import com.Axeryok.CocoaInput.Rect;
import com.Axeryok.CocoaInput.arch.darwin.CallbackFunction.Func_firstRectForCharacterRange;
import com.Axeryok.CocoaInput.arch.darwin.CallbackFunction.Func_insertText;
import com.Axeryok.CocoaInput.arch.darwin.CallbackFunction.Func_setMarkedText;
import com.Axeryok.CocoaInput.impl.IMEOperator;
import com.Axeryok.CocoaInput.impl.IMEReceiver;
import com.Axeryok.CocoaInput.wrapper.GuiTextFieldWrapper;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;

public class DarwinIMEOperator implements IMEOperator{
	IMEReceiver owner;
	String uuid;
	Func_insertText insertText_p;
	Func_setMarkedText setMarkedText_p;
	Func_firstRectForCharacterRange firstRectForCharacterRange_p;
	public DarwinIMEOperator(IMEReceiver field){
		this.owner=field;
		uuid = UUID.randomUUID().toString();
		insertText_p=new Func_insertText(){
			@Override
			public void invoke(String str, int position, int length) {
				ModLogger.debug(3, "Textfield "+uuid+" received inserted text.");
				owner.insertText(str, position, length);
			}
		};
		setMarkedText_p=new Func_setMarkedText(){
			@Override
			public void invoke(String str, int position1, int length1,
					int position2, int length2) {
				ModLogger.debug(3, "MarkedText changed at "+uuid+".");
				owner.setMarkedText(str, position1, length1, position2, length2);;
			}
			
		};
		firstRectForCharacterRange_p=new Func_firstRectForCharacterRange(){

			@Override
			public Pointer invoke() {
				ModLogger.debug(3, "Called to determine where to draw.");
				Rect point=owner.getRect();
				float []buff;
				if(point==null){
					buff=new float[]{0,0,0,0};
				}
				else{
					buff=new float[]{point.getX(),point.getY(),point.getWidth(),point.getHeight()};
				}
				int factor=CocoaInput.getScreenScaledFactor();
				buff[0]*=factor;
				buff[1]*=factor;
				buff[2]*=factor;
				buff[3]*=factor;
				
				buff[0]+=org.lwjgl.opengl.Display.getX();
				buff[1]=Handle.INSTANCE.invertYCoordinate(org.lwjgl.opengl.Display.getY()+buff[1]);
				
				
				Pointer ret=new Memory(Float.BYTES*4);
				ret.write(0,buff,0,4);
				return ret;
			}
			
		};
		Handle.INSTANCE.addInstance(uuid, insertText_p, setMarkedText_p, firstRectForCharacterRange_p);
	}
	
	public void discardMarkedText(){
		Handle.INSTANCE.discardMarkedText(uuid);
	}
	
	public void removeInstance(){
		Handle.INSTANCE.removeInstance(uuid);
	}
	
	public void setFocused(boolean yn){
		Handle.INSTANCE.setIfReceiveEvent(uuid, yn==true?1:0);
	}
	
}
