package jp.axer.cocoainput.arch.darwin;

import jp.axer.cocoainput.arch.darwin.CallbackFunction.*;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface Handle extends Library{
    Handle INSTANCE=(Handle)Native.loadLibrary("cocoainput", Handle.class);


    void initialize(Callback log,Callback error,Callback debug);
    void refreshInstance();
    void addInstance(String uuid, Func_insertText insertText_p, Func_setMarkedText setMarkedText_p, Func_firstRectForCharacterRange firstRectForCharacterRange_p);
    void removeInstance(String uuid);
    void discardMarkedText(String uuid);
    void setIfReceiveEvent(String uuid,int yn);
    float invertYCoordinate(float y);
}