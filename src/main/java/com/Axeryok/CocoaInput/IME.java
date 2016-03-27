package com.Axeryok.CocoaInput;

public interface IME{
	String getUUID();
	IMEOperator getIMEOperator();
	void insertText(String aString,int position1,int length1);//確定文字列
	void setMarkedText(String aString,int position1,int length1,int position2,int length2);//途中変換文字列
	float[] firstRectForCharacterRange();//テキストフィールドの位置形状を返す{x,y,width,height}(x,y)からwidth,heightの大きさのボックス
}
