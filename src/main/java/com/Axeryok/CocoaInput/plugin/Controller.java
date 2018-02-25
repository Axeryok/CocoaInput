package com.Axeryok.CocoaInput.plugin;

/*
 * テキストフィールドや看板、本が開かれたときに呼ばれる。
 */

public interface Controller {
	IMEOperator generateIMEOperator(IMEReceiver ime); //GuiTextFieldとかが作成された時に割り当てるIMEOperator生成処理を委託
	/*
	 * Minecraftが使うJNAのバージョンではそのままではセルフライブラリの場所を認識できない
	 * getLibraryPath()がnullでない限りCocoaInputがそのライブラリを認識できる場所にコピーする
	 * WindowsはIME操作する関数がC言語で提供されているのでセルフライブラリを用意せずともJNAで書けると思うので不要
	 */
}
