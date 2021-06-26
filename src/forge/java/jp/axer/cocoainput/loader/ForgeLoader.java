package jp.axer.cocoainput.loader;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.util.ModLogger;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("cocoainput")
public class ForgeLoader {
	private CocoaInput instance;

	public ForgeLoader(){
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);

	}

	private void setup(final FMLCommonSetupEvent event) {
			this.instance=new CocoaInput("MinecraftForge",ModList.get().getModFileById("cocoainput").getFile().getFilePath().toString());
			ModLogger.log("Forge config setup");
			ModLogger.log("Config path:"+FMLPaths.CONFIGDIR.get().resolve("cocoainput.json").toString());
			FCConfig.init("cocoainput",FMLPaths.CONFIGDIR.get().resolve("cocoainput.json"), FCConfig.class);
			ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, ()->(mc,modListScreen)->new FCConfig().getScreen(modListScreen));
			CocoaInput.config=new FCConfig();
			ModLogger.log("ConfigPack:"+CocoaInput.config.isAdvancedPreeditDraw()+" "+CocoaInput.config.isNativeCharTyped());
	}
	@SubscribeEvent
    public void didChangeGui(GuiOpenEvent event) {
		this.instance.distributeScreen(event.getGui());
	}
}
