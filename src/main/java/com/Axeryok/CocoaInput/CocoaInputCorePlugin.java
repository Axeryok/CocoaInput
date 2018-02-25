package com.Axeryok.CocoaInput;

import java.io.File;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class CocoaInputCorePlugin implements IFMLLoadingPlugin {
    private static File location;

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"com.Axeryok.CocoaInput.CocoaInputTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return "com.Axeryok.CocoaInput.CocoaInput";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		location = (File) data.get("coremodLocation");
		if(location==null){
			location = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		}
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
