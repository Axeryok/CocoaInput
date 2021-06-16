package jp.axer.cocoainput.arch.win;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

public interface Handle extends Library{
	Handle INSTANCE = (Handle)Native.loadLibrary("libwincocoainput", Handle.class);

	void set_focus(int flag);
	void initialize(
			long window,
			PreeditCallback paramDrawCallback,
			DoneCallback paramDoneCallback,
			RectCallback paramRectCallback,
			Callback log,
			Callback error,
			Callback debug
			);

	public static interface PreeditCallback extends Callback{
		void invoke(WString str,int cursor,int length);
	}
	/*public static interface DrawCallback extends Callback {
	    Pointer invoke(int param1Int1, int param1Int2, int param1Int3, short param1Short, boolean param1Boolean, String param1String, WString param1WString, int param1Int4, int param1Int5, int param1Int6);
	}*/
	public static interface DoneCallback extends Callback {
	    void invoke(WString str);
	}
	
	public static interface RectCallback extends Callback{
		int invoke(Pointer p);
	}


}
