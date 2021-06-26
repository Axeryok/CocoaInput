package jp.axer.cocoainput.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class ModLogger {
    public static boolean debugMode=true;

    public static void log(String msg,Object...data){
        LogManager.getLogger("CocoaInput:Java").log( Level.INFO,msg, data);
    }

    public static void error(String msg,Object...data){
        LogManager.getLogger("CocoaInput:Java").error(msg, data);
    }

    public static void debug(String msg,Object...data){
        if(debugMode){
            LogManager.getLogger("CocoaInput:Java").debug(msg, data);
        }
    }
}