package com.Axeryok.CocoaInput.impl;

import javax.annotation.Nullable;
import com.Axeryok.CocoaInput.impl.IMEReceiver;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public interface Controller {
	public void CocoaInputInitialization(FMLInitializationEvent event) throws Exception; //起動時に呼ばれる
	public IMEOperator generateIMEOperator(IMEReceiver ime); //GuiTextFieldとかが作成された時に割り当てるIMEOperator生成処理を委託
	
	/*
	 * Minecraftが使うJNAのバージョンではそのままではセルフライブラリの場所を認識できない
	 * getLibraryPath()がnullでない限りCocoaInputがそのライブラリを認識できる場所にコピーする
	 * WindowsはIME操作する関数がC言語で提供されているのでセルフライブラリを用意せずともJNAで書けると思うので不要
	 */
	public @Nullable String getLibraryPath(); // /darwin/libcocoainput.dylib　とか、無いなら不要
	public @Nullable String getLibraryName(); // libcocoainput.dylibとか、無いなら不要
}
