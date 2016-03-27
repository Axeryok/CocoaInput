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

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//@Mod(modid = CocoaInput.MODID, version = CocoaInput.VERSION)
public class CocoaInput extends DummyModContainer
{
    public static final String MODID = "CocoaInput";
    public static final String VERSION = "3.0.0";
    
    public CocoaInput(){
    	super(new ModMetadata());
    	ModMetadata meta = getMetadata();
    	meta.modId="CocoaInput";
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
    
    @Subscribe
    public void init(FMLInitializationEvent event)
    {
		// some example code
    	this.acceptUnderline();
    	this.copyLibrary();//JNAがライブラリを見つけられる位置にコピーする
    	MinecraftForge.EVENT_BUS.register(this);
    	Handle.INSTANCE.initialize();
    }
    
    @SubscribeEvent
    public void didChangeGui(net.minecraftforge.client.event.GuiOpenEvent event){
    	if(!(event.getGui() instanceof IME)){
    		Handle.INSTANCE.refreshInstance();//GUIの切り替えでIMの使用をoffにする
    	}
    }
    
    
    private void copyLibrary(){
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
			System.out.println("Attempted to copy library to ./native/libcocoainput.dylib but failed.");
			e1.printStackTrace();
		}
		System.setProperty("jna.library.path",nativeDir.getAbsolutePath());
    }
    
    private void acceptUnderline(){
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
	      System.out.println("UnderlineFix has fixed UnderLineBug.");
	    }
	    catch (Exception e)
	    {
	      System.out.println("UnderlineFix failed to fix.");
	    }
    }
}
