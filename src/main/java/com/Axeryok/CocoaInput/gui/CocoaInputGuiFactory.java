package com.Axeryok.CocoaInput.gui;


import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.FMLConfigGuiFactory;

public class CocoaInputGuiFactory extends FMLConfigGuiFactory {
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass(){
		return CocoaInputGuiConfig.class;
	}
	
	@Override
	public GuiScreen createConfigGui( GuiScreen parentScreen ){
		return new CocoaInputGuiConfig( parentScreen );
	}
}
