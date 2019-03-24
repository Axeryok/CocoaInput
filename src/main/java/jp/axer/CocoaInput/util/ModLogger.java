package jp.axer.CocoaInput.util;

import org.apache.logging.log4j.Level;

import org.apache.logging.log4j.LogManager;

public class ModLogger {
    public static boolean debugMode=false;

    public static void log(String msg,Object...data){
        LogManager.getLogger("CocoaInput:Java").log( Level.INFO,msg, data);
    }

    public static void error(String msg,Object...data){
        LogManager.getLogger("CocoaInput:Java").log( Level.ERROR,msg, data);
    }

    public static void debug(String msg,Object...data){
        if(debugMode){
            LogManager.getLogger("CocoaInput:Java").log( Level.DEBUG,msg, data);
        }
    }
}