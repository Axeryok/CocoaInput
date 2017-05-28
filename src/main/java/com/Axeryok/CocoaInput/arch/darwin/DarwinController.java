package com.Axeryok.CocoaInput.arch.darwin;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.lwjgl.opengl.Display;

import com.Axeryok.CocoaInput.IMEReceiver;
import com.Axeryok.CocoaInput.ModLogger;
import com.Axeryok.CocoaInput.impl.Controller;
import com.Axeryok.CocoaInput.impl.IMEOperator;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DarwinController implements Controller{

	@Override
	public void CocoaInputInitialization(FMLInitializationEvent event) throws Exception {
		this.acceptUnderline();
    	MinecraftForge.EVENT_BUS.register(this);
    	ModLogger.log("CocoaInput is being initialized.If stops here,click minecraft window.");
    	Handle.INSTANCE.initialize(CallbackFunction.Func_log,CallbackFunction.Func_error,CallbackFunction.Func_debug);
    	ModLogger.log("CocoaInput has been initialized.");
	}

	@Override
	public IMEOperator generateIMEOperator(IMEReceiver ime) {
		return new DarwinIMEOperator(ime);
	}
	
	@SubscribeEvent
    public void didChangeGui(net.minecraftforge.client.event.GuiOpenEvent event){
    	if(!(event.getGui() instanceof IMEReceiver)){
    		Handle.INSTANCE.refreshInstance();//GUIの切り替えでIMの使用をoffにする
    	}
    }



	@Override
	public String getLibraryPath() {
		// TODO 自動生成されたメソッド・スタブ
		return "/darwin/libcocoainput.dylib";
	}

	@Override
	public String getLibraryName() {
		// TODO 自動生成されたメソッド・スタブ
		return "libcocoainput.dylib";
	}
	
	private void acceptUnderline() throws Exception{
    	try
	    {
	      Class Display = Display.class;
	      Field field_MacOSXDisplay = Display.getDeclaredField("display_impl");
	      field_MacOSXDisplay.setAccessible(true);
	      Class MacOSXDisplay = field_MacOSXDisplay.get(null).getClass();
	      Field field_MacOSXNativeKeyboard = MacOSXDisplay.getDeclaredField("keyboard");
	      field_MacOSXNativeKeyboard.setAccessible(true);
	      Class MacOSXNativeKeyboard = field_MacOSXNativeKeyboard.get(field_MacOSXDisplay.get(null)).getClass();
	      Field field_map = MacOSXNativeKeyboard.getDeclaredField("nativeToLwjglMap");
	      field_map.setAccessible(true);
	      HashMap<Short, Integer> map = (HashMap)field_map.get(field_MacOSXNativeKeyboard.get(field_MacOSXDisplay
	        .get(null)));
	      map.put(Short.valueOf((short)94), Integer.valueOf(147));
	      ModLogger.log("UnderlineFix has fixed UnderLineBug.");
	    }
	    catch (Exception e)
	    {
	      throw e;
	    }
    }
}
