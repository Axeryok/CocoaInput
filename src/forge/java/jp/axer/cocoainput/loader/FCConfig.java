package jp.axer.cocoainput.loader;

import jp.axer.cocoainput.util.ConfigPack;

public class FCConfig extends TinyConfig implements ConfigPack{

	@Entry(comment="AdvancedPreeditDraw - Is preedit marking - Default:true")
	public static boolean advancedPreeditDraw=true;
	@Entry(comment="NativeCharTyped - Is text inserted with native way - Default:true")
	public static boolean nativeCharTyped=true;



	@Override
	public boolean isAdvancedPreeditDraw() {
		// TODO 自動生成されたメソッド・スタブ
		return advancedPreeditDraw;
	}
	@Override
	public boolean isNativeCharTyped() {
		// TODO 自動生成されたメソッド・スタブ
		return nativeCharTyped;
	}


}
