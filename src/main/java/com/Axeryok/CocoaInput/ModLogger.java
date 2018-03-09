package com.Axeryok.CocoaInput;

import org.apache.logging.log4j.Level;

import net.minecraftforge.fml.common.FMLLog;

public class ModLogger {
	public static boolean debugMode;
	
	public static void log(String msg,Object...data){
		FMLLog.log("CocoaInput:Java", Level.INFO, msg, data);
	}
	
	public static void error(String msg,Object...data){
		FMLLog.log("CocoaInput:Java", Level.INFO, msg, data);
	}
	
	public static void debug(String msg,Object...data){
		if(debugMode){
			FMLLog.log("CocoaInput:Java/DEBUG", Level.INFO, msg, data);
		}
	}
}
