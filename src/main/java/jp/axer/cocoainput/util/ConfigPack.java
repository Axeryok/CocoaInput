package jp.axer.cocoainput.util;

public interface ConfigPack {

	public static ConfigPack defaultConfig = new ConfigPack() {
		@Override
		public boolean isAdvancedPreeditDraw() {
			return true;
		}
		@Override
		public boolean isNativeCharTyped() {
			return true;
		}
	};

	public boolean isAdvancedPreeditDraw();
	public boolean isNativeCharTyped();
}
