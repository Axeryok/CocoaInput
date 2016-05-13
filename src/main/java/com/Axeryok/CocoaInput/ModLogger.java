package com.Axeryok.CocoaInput;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.FMLLog;

public class ModLogger {
	public static int debugLevel;
	
	public static void log(String msg,Object...data){
		FMLLog.log("CocoaInput:Java", Level.INFO, msg, data);
	}
	
	public static void error(String msg,Object...data){
		FMLLog.log("CocoaInput:Java", Level.INFO, msg, data);
	}
	
	public static void debug(int suppliedDebugLevel,String msg,Object...data){
		if(suppliedDebugLevel<=debugLevel){
			FMLLog.log("CocoaInput:Java/DEBUG", Level.INFO, msg, data);
		}
	}
	
}
