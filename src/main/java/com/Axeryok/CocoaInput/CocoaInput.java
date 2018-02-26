package com.Axeryok.CocoaInput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.Axeryok.CocoaInput.arch.darwin.DarwinController;
import com.Axeryok.CocoaInput.arch.darwin.Handle;
import com.Axeryok.CocoaInput.arch.dummy.DummyController;
import com.Axeryok.CocoaInput.plugin.Controller;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.sun.jna.Platform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CocoaInput extends DummyModContainer{
	private static final String MODID = "CocoaInput";
	private static final String VERSION = "3.1.1";
	public static Configuration configFile;
	public static Controller controller=null;
	private static CocoaInput instance=null;
	private static boolean enableNativeFullscreen;
	public CocoaInput(){
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId=MODID;
		meta.name="CocoaInput";
		meta.description="Support IME input on macOS.";
		meta.version=this.VERSION;
		meta.authorList=Arrays.asList("Axer");
		meta.credits="Logo was painted by RedWheat.This mod uses JavaNativeAccess(Apache License2).";
		meta.logoFile="/logo.png";
		this.setEnabledState(true);
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller){
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
	public void init(FMLInitializationEvent event) throws Exception{
		CocoaInput.instance=this;
		MinecraftForge.EVENT_BUS.register(this);
		this.applyController(new DummyController());
		if(Platform.isMac()){
			this.applyController(new DarwinController());
		}
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void applyController(Controller controller) throws IOException{
		this.controller=controller;
		ModLogger.log("CocoaInput is now using controller:"+controller.getClass().toString());
	}

	//TextFormatting.getTextWithoutFormattingCodes(String str)の代替
	public static String returnSameObject(String obj){
		return obj;
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
		if(event.getModID().equals("CocoaInput")){
			this.syncConfig();
			ModLogger.log("Configuration has changed.");
		}
	}

	private void syncConfig(){
		CocoaInput.enableNativeFullscreen=configFile.getBoolean("CocoaInputFullScreenSystem", Configuration.CATEGORY_GENERAL,false, "If true,you can see conversation window in fullscreen mode.But sometimes it causes minecraft to crash.");
		ModLogger.debugLevel=configFile.getInt("debugLevel", Configuration.CATEGORY_GENERAL, 0, 0, 4, "Logger shows debug messages less than the debugLevel you set.");
		if(configFile.hasChanged()){
			configFile.save();
		}
	}

	public static void copyLibrary(String libraryName,String libraryPath) throws IOException{
		if(libraryName==null)return;
		InputStream libFile=instance.getClass().getResourceAsStream(libraryPath);
		File nativeDir=new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath().concat("/native"));
		File copyLibFile=new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath().concat("/native/"+libraryName));
		try{
			nativeDir.mkdir();
			FileOutputStream fos=new FileOutputStream(copyLibFile);
			copyLibFile.createNewFile();
			IOUtils.copy(libFile, fos);
			fos.close();
		} catch (IOException e1) {
			ModLogger.error("Attempted to copy library to ./native/"+libraryName+" but failed.");
			throw e1;
		}
		System.setProperty("jna.library.path",nativeDir.getAbsolutePath());
	}

	public static boolean toggleFullScreen(){//Only used in macOS
		if(!enableNativeFullscreen)return false;
		Minecraft mc=Minecraft.getMinecraft();
		mc.fullscreen=!mc.fullscreen;
		mc.gameSettings.fullScreen=mc.fullscreen;

		try{
			if(mc.fullscreen){
				mc.updateDisplayMode();
				mc.displayWidth = Display.getDisplayMode().getWidth();
				mc.displayHeight = Display.getDisplayMode().getHeight();
				if (mc.displayWidth <= 0) {
					mc.displayWidth = 1;
				}

				if (mc.displayHeight <= 0) {
					mc.displayHeight = 1;
				}
			}else{
				Display.setDisplayMode(new DisplayMode(mc.tempDisplayWidth, mc.tempDisplayHeight));
				mc.displayWidth = mc.tempDisplayWidth;
				mc.displayHeight = mc.tempDisplayHeight;

				if (mc.displayWidth <= 0)
				{
					mc.displayWidth = 1;
				}

				if (mc.displayHeight <= 0)
				{
					mc.displayHeight = 1;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}if (mc.currentScreen != null){
			mc.resize(mc.displayWidth, mc.displayHeight);
		}else{
			mc.updateFramebufferSize();
		}
		Handle.INSTANCE.toggleFullScreen();
		mc.updateDisplay();
		return true;
	}


	public static String formatMarkedText(String aString,int position1,int length1){//ユーティリティ
		StringBuilder builder=new StringBuilder(aString);
		if(length1!=0){
			builder.insert(position1+length1, "§r§n");
			builder.insert(position1,"§l");
		}
		builder.insert(0, "§n");
		builder.append("§r");

		return new String(builder);
	}

	public static int getScreenScaledFactor(){//ユーティリティ
		return new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
	}
}
