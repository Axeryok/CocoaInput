package com.Axeryok.CocoaInput.asm;

import java.io.File;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class CocoaInputCorePlugin implements IFMLLoadingPlugin {

	static File location;
	
	
	@Override
	public String[] getASMTransformerClass() {
		// TODO 自動生成されたメソッド・スタブ
		return new String[]{"com.Axeryok.CocoaInput.asm.CocoaInputTransformer"};
	}

	@Override
	public String getModContainerClass() {
		// TODO 自動生成されたメソッド・スタブ
		return "com.Axeryok.CocoaInput.CocoaInput";
	}

	@Override
	public String getSetupClass() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		// TODO 自動生成されたメソッド・スタブ
		location = (File) data.get("coremodLocation");
		if(location==null){
			location=new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		}
	}

	@Override
	public String getAccessTransformerClass() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
