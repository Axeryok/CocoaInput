package com.Axeryok.CocoaInput;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface Handle extends Library{
	 Handle INSTANCE=(Handle)Native.loadLibrary("cocoainput", Handle.class);
	interface Func_insertText extends Callback{
		void invoke(String str,int position,int length);
	}
	interface Func_setMarkedText extends Callback{
		void invoke(String str,int position1,int length1,int position2,int length2);
	}
	interface Func_firstRectForCharacterRange extends Callback{
		Pointer invoke();
	}
	
	void initialize();
	void refreshInstance();
	void addInstance(String uuid,Func_insertText insertText_p,Func_setMarkedText setMarkedText_p,Func_firstRectForCharacterRange firstRectForCharacterRange_p);
	void removeInstance(String uuid);
	void discardMarkedText(String uuid);
	void setIfReceiveEvent(String uuid,int yn);
	float invertYCoordinate(float y);
	void issueKeyEvent(String str);
}
