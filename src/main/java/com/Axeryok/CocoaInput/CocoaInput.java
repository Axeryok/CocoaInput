package com.Axeryok.CocoaInput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.Display;

import com.Axeryok.CocoaInput.asm.CocoaInputTransformer;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CocoaInput extends DummyModContainer
{
    public static final String MODID = "CocoaInput";
    public static final String VERSION = "3.0.5";
    public static Configuration configFile;
    
    public CocoaInput(){
    	super(new ModMetadata());
    	ModMetadata meta = getMetadata();
    	meta.modId=MODID;
    	meta.name="CocoaInput";
    	meta.description="Support IME input on OSX.";
    	meta.version=this.VERSION;
    	meta.authorList=Arrays.asList("Axer");
    	meta.credits="Logo was painted by RedWheat.This mod uses JavaNativeAccess(Apache License2).";
    	meta.logoFile="/logo.png";
    	this.setEnabledState(true);
    	
    }
    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
    	bus.register(this);
        return true;
    }
    @Override
    public String getGuiClassName(){
    	return "com.Axeryok.CocoaInput.gui.CocoaInputGuiFactory";
    }
    
    @Subscribe
    public void preInit(FMLPreInitializationEvent event){
    	configFile = new Configuration(event.getSuggestedConfigurationFile());
    	this.syncConfig();
    }
    
    @Subscribe
    public void init(FMLInitializationEvent event) throws Exception
    {
    	if(!System.getProperty("os.name").toLowerCase().startsWith("mac")){
    		ModLogger.error("CocoaInput has not been initialized:Not OSX");
    		return;
    	}
    	this.acceptUnderline();
    	this.copyLibrary();//JNAがライブラリを見つけられる位置にコピーする
    	MinecraftForge.EVENT_BUS.register(this);
    	ModLogger.log("CocoaInput is being initialized.If stops here,click minecraft window.");
    	Handle.INSTANCE.initialize(CallbackFunction.Func_log,CallbackFunction.Func_error,CallbackFunction.Func_debug);
    	ModLogger.log("CocoaInput has been initialized.");
    }
    
    @SubscribeEvent
    public void didChangeGui(net.minecraftforge.client.event.GuiOpenEvent event){
    	if(!(event.getGui() instanceof IME)){
    		Handle.INSTANCE.refreshInstance();//GUIの切り替えでIMの使用をoffにする
    	}
    }
    
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
    	if(event.getModID().equals("CocoaInput")){
    		this.syncConfig();
    		ModLogger.log("Configuration has changed.");
    	}
    }
    
    private void syncConfig(){
    	ModLogger.debugLevel=configFile.getInt("debugLevel", Configuration.CATEGORY_GENERAL, 0, 0, 4, "Logger shows debug messages less than the debugLevel you set.");
    	if(configFile.hasChanged()){
    		configFile.save();
    	}
    }
    
    private void copyLibrary() throws IOException{
    	InputStream libFile=this.getClass().getResourceAsStream("/darwin/libcocoainput.dylib");
		File nativeDir=new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath().concat("/native"));
		File copyLibFile=new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath().concat("/native/libcocoainput.dylib"));
		try {
			nativeDir.mkdir();
			FileOutputStream fos=new FileOutputStream(copyLibFile);
			copyLibFile.createNewFile();
			IOUtils.copy(libFile, fos);
			fos.close();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			ModLogger.error("Attempted to copy library to ./native/libcocoainput.dylib but failed.");
			throw e1;
		}
		System.setProperty("jna.library.path",nativeDir.getAbsolutePath());
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
