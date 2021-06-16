package jp.axer.cocoainput.arch.x11;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.sun.jna.Callback;

public class Logger {
	
	public static void log(String msg,Object...data){
        LogManager.getLogger("CocoaInputX11:Java").log( Level.INFO,msg, data);
    }

    public static void error(String msg,Object...data){
        LogManager.getLogger("CocoaInputX11:Java").log( Level.ERROR,msg, data);
    }

    public static void debug(String msg,Object...data){
            LogManager.getLogger("CocoaInputX11:Java").log( Level.DEBUG,msg, data);

    }
	
	public static Callback clangLog = new Callback() {
        public void invoke(String msg) {
            LogManager.getLogger("CocoaInputX11:Clang").log(Level.INFO, msg);
        }
    };
    public static Callback clangError = new Callback() {
        public void invoke(String msg) {
            LogManager.getLogger("CocoaInputX11:Clang").log(Level.ERROR, msg);
        }
    };
    public static Callback clangDebug = new Callback() {
        public void invoke(String msg) {
                LogManager.getLogger("CocoaInputX11:Clang").log(Level.DEBUG, msg);
        }
    };
}
