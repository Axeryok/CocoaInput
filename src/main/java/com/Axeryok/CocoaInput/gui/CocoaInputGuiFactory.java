package com.Axeryok.CocoaInput.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class CocoaInputGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		// TODO 自動生成されたメソッド・スタブ
		return CocoaInputGuiConfig.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
