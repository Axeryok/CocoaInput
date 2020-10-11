package jp.axer.cocoainput.arch.darwin;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import jp.axer.cocoainput.util.ModLogger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class CallbackFunction {
    //used to interact Objective-C code
    interface Func_insertText extends Callback {
        void invoke(String str, int position, int length);
    }

    interface Func_setMarkedText extends Callback {
        void invoke(String str, int position1, int length1, int position2, int length2);
    }

    interface Func_firstRectForCharacterRange extends Callback {
        Pointer invoke();
    }

    //used to provide Objective-C with logging way
    public static Callback Func_log = new Callback() {
        public void invoke(String msg) {
            LogManager.getLogger("CocoaInput:ObjC").log(Level.INFO, msg);
        }
    };
    public static Callback Func_error = new Callback() {
        public void invoke(String msg) {
            LogManager.getLogger("CocoaInput:ObjC").log(Level.ERROR, msg);
        }
    };
    public static Callback Func_debug = new Callback() {
        public void invoke(String msg) {
            if (ModLogger.debugMode) {
                LogManager.getLogger("CocoaInput:ObjC").log(Level.DEBUG, msg);
            }
        }
    };
}