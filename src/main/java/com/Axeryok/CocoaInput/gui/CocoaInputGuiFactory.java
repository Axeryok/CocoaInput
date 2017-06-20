package com.Axeryok.CocoaInput.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.FMLConfigGuiFactory;
import net.minecraftforge.fml.client.IModGuiFactory;

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
