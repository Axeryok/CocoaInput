package com.Axeryok.CocoaInput;

import com.Axeryok.CocoaInput.CallbackFunction.*;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public class IMEOperator {
	IME owner;
	Func_insertText insertText_p;
	Func_setMarkedText setMarkedText_p;
	Func_firstRectForCharacterRange firstRectForCharacterRange_p;
	public IMEOperator(IME field){
		this.owner=field;
		insertText_p=new Func_insertText(){
			@Override
			public void invoke(String str, int position, int length) {
				// TODO 自動生成されたメソッド・スタブ
				ModLogger.debug(3, "Textfield "+owner.getUUID()+" received inserted text.");
				owner.insertText(str, position, length);
			}
		};
		setMarkedText_p=new Func_setMarkedText(){

			@Override
			public void invoke(String str, int position1, int length1,
					int position2, int length2) {
				// TODO 自動生成されたメソッド・スタブ
				ModLogger.debug(3, "MarkedText changed at "+owner.getUUID()+".");
				owner.setMarkedText(str, position1, length1, position2, length2);;
			}
			
		};
		firstRectForCharacterRange_p=new Func_firstRectForCharacterRange(){

			@Override
			public Pointer invoke() {
				// TODO 自動生成されたメソッド・スタブ
				ModLogger.debug(3, "Called to determine where to draw.");
				float []point=owner.firstRectForCharacterRange();
				Pointer ret=new Memory(Float.BYTES*4);
				ret.write(0,point,0,4);
				return ret;
			}
			
		};
		Handle.INSTANCE.addInstance(owner.getUUID(), insertText_p, setMarkedText_p, firstRectForCharacterRange_p);
	}
	
	public void discardMarkedText(){
		Handle.INSTANCE.discardMarkedText(owner.getUUID());
	}
	
	public void removeInstance(){
		Handle.INSTANCE.removeInstance(owner.getUUID());
	}
	
	public void setIfReceiveEvent(boolean yn){
		Handle.INSTANCE.setIfReceiveEvent(owner.getUUID(), yn==true?1:0);
	}
	
	public static String formatMarkedText(String aString,int position1,int length1){
		StringBuilder builder=new StringBuilder(aString);
		if(length1!=0){
			builder.insert(position1+length1, "§r§n");
			builder.insert(position1,"§l");
		}
		builder.insert(0, "§n");
		builder.append("§r");
		
		return new String(builder);
	}
	
}
