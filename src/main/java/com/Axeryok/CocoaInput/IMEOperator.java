package com.Axeryok.CocoaInput;

import com.Axeryok.CocoaInput.Handle.Func_firstRectForCharacterRange;
import com.Axeryok.CocoaInput.Handle.Func_insertText;
import com.Axeryok.CocoaInput.Handle.Func_setMarkedText;
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
				owner.insertText(str, position, length);
			}
		};
		setMarkedText_p=new Func_setMarkedText(){

			@Override
			public void invoke(String str, int position1, int length1,
					int position2, int length2) {
				// TODO 自動生成されたメソッド・スタブ
				owner.setMarkedText(str, position1, length1, position2, length2);;
			}
			
		};
		firstRectForCharacterRange_p=new Func_firstRectForCharacterRange(){

			@Override
			public Pointer invoke() {
				// TODO 自動生成されたメソッド・スタブ
				float []point=owner.firstRectForCharacterRange();
				Pointer ret=new Memory(Float.BYTES*4);
				ret.write(0,point,0,4);
				return ret;
			}
			
		};
		System.out.println("add");
		Handle.INSTANCE.addInstance(owner.getUUID(), insertText_p, setMarkedText_p, firstRectForCharacterRange_p);
		System.out.println("added");
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
	
}
