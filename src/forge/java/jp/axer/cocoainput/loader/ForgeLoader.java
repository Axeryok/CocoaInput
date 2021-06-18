package jp.axer.cocoainput.loader;

import jp.axer.cocoainput.CocoaInput;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("cocoainput")
public class ForgeLoader {
	private CocoaInput instance;

	public ForgeLoader(){
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);

	}

	private void setup(final FMLCommonSetupEvent event) {
			this.instance=new CocoaInput("MinecraftForge",ModList.get().getModFileById("cocoainput").getFile().getFilePath().toString());
	}
	@SubscribeEvent
    public void didChangeGui(GuiOpenEvent event) {
		this.instance.distributeScreen(event.getGui());
	}
}
