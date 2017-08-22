package com.Axeryok.CocoaInput.arch.darwin;

import org.apache.logging.log4j.Level;
import org.lwjgl.LWJGLException;

import com.Axeryok.CocoaInput.CocoaInput;
import com.Axeryok.CocoaInput.ModLogger;
import com.sun.jna.Callback;
import com.sun.jna.Pointer;

import net.minecraftforge.fml.common.FMLLog;

public class CallbackFunction {
	//used to interact Objective-C code
	interface Func_insertText extends Callback{
		void invoke(String str,int position,int length);
	}
	interface Func_setMarkedText extends Callback{
		void invoke(String str,int position1,int length1,int position2,int length2);
	}
	interface Func_firstRectForCharacterRange extends Callback{
		Pointer invoke();
	}
	
	
	//used to provide Objective-C with logging way
	public static Callback Func_log=new Callback(){
		public void invoke(String msg){
			FMLLog.log("CocoaInput:Objective-C", Level.INFO, msg);
		}
	};
	public static Callback Func_error=new Callback(){
		public void invoke(String msg){
			FMLLog.log("CocoaInput:Objective-C", Level.INFO, msg);
		}
	};
	public static Callback Func_debug=new Callback(){
		public void invoke(int suppliedDebugLevel,String msg){
			if(suppliedDebugLevel<=ModLogger.debugLevel){
				FMLLog.log("CocoaInput:Objective-C/DEBUG", Level.INFO, msg);
			}
		}
	};
	
}
