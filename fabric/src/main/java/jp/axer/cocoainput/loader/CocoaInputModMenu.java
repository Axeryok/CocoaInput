package jp.axer.cocoainput.loader;


import io.github.prospector.modmenu.api.ModMenuApi;
import io.github.prospector.modmenu.api.ConfigScreenFactory;

public class CocoaInputModMenu implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> (new FCConfig().getScreen(parent));
	}
}
