package com.Axeryok.CocoaInput.gui;

import com.Axeryok.CocoaInput.CocoaInput;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class CocoaInputGuiConfig extends GuiConfig {
	public CocoaInputGuiConfig(GuiScreen parent){
		super(parent,new ConfigElement(CocoaInput.configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), "CocoaInput", false, false, GuiConfig.getAbridgedConfigPath(CocoaInput.configFile.toString()));
	}
}
