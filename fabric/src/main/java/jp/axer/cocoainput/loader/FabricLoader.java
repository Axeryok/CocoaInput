package jp.axer.cocoainput.loader;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import jp.axer.cocoainput.CocoaInput;
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
	}

	public void onChangeScreen(Screen sc){
		if(this.cocoainput==null){
			this.onWindowLaunched();
			return;
		}
		this.cocoainput.distributeScreen(sc);
	}
}
