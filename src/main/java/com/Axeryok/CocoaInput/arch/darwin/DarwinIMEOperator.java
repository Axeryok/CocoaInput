package com.Axeryok.CocoaInput.arch.darwin;

import java.util.UUID;

import com.Axeryok.CocoaInput.ModLogger;
import com.Axeryok.CocoaInput.arch.darwin.CallbackFunction.Func_firstRectForCharacterRange;
import com.Axeryok.CocoaInput.arch.darwin.CallbackFunction.Func_insertText;
import com.Axeryok.CocoaInput.arch.darwin.CallbackFunction.Func_setMarkedText;
import com.Axeryok.CocoaInput.impl.IMEOperator;
import com.Axeryok.CocoaInput.impl.IMEReceiver;
import com.Axeryok.CocoaInput.wrapper.GuiTextFieldWrapper;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

import net.minecraft.client.gui.GuiTextField;

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
				float []point;
				float []noSelection={
			            org.lwjgl.opengl.Display.getX(),
			            Handle.INSTANCE.invertYCoordinate(org.lwjgl.opengl.Display.getY()),
			            0,
			            0
			        };//TODO 描画位置改善
				

				if((point=owner.getRectPoint())==null){
					point=noSelection;
				}
				if(owner instanceof GuiTextFieldWrapper){
					GuiTextField textField=((GuiTextFieldWrapper) owner).owner;
					float x = org.lwjgl.opengl.Display.getX()
							+ (textField.fontRendererInstance.getStringWidth(textField.getText().substring(0, textField.cursorPosition)) * 2
									+ (textField.enableBackgroundDrawing ? textField.xPosition + 4 : textField.xPosition) * 2);
					float y = org.lwjgl.opengl.Display.getY()
							+ (textField.enableBackgroundDrawing ? textField.yPosition + (textField.height - 8) / 2 : textField.yPosition) * 2
							+ textField.fontRendererInstance.FONT_HEIGHT * 2;
					point[0]=x;
					point[1]=Handle.INSTANCE.invertYCoordinate(y);
					point[2]=textField.width;
					point[3]=textField.height;
				}
				Pointer ret=new Memory(Float.BYTES*4);
				ret.write(0,point,0,4);
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
