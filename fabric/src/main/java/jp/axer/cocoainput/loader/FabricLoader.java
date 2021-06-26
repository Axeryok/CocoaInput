package jp.axer.cocoainput.loader;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.util.ModLogger;
import net.minecraft.client.gui.screens.Screen;

public class FabricLoader implements ClientModInitializer {
	public static FabricLoader instance;
	public CocoaInput cocoainput;
	@Override
	public void onInitializeClient() {
		FabricLoader.instance=this;
	}
	public void onWindowLaunched(){
		this.cocoainput=new CocoaInput("Fabric",null);
		ModLogger.log("Fabric config setup");
		ModLogger.log("Config path:"+net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().resolve("cocoainput.json").toString());
		FCConfig.init("cocoainput",net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().resolve("cocoainput.json"), FCConfig.class);
		CocoaInput.config=new FCConfig();
		ModLogger.log("ConfigPack:"+CocoaInput.config.isAdvancedPreeditDraw()+" "+CocoaInput.config.isNativeCharTyped());
	}

	public void onChangeScreen(Screen sc){
		if(this.cocoainput==null){
			this.onWindowLaunched();
			return;
		}
		this.cocoainput.distributeScreen(sc);
	}
}
