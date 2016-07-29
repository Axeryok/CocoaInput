package com.Axeryok.CocoaInput.win;

import com.Axeryok.CocoaInput.IMEReceiver;
import com.Axeryok.CocoaInput.ModLogger;
import com.Axeryok.CocoaInput.impl.Controller;
import com.Axeryok.CocoaInput.impl.IMEOperator;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class WindowsController implements Controller{

	/*
	 * WindowsにCocoaInputを対応させる手引
	 * 
	 * IMEReceiver：インターフェース　GuiTextFieldとかGuiEditSignとかが実装している
	 * IMEOperator：Controllerから個別にIMEReceiverの処理を委託されている　またIMEReceiverからイベントも送られてくる　生成時（generateIMEOperator）に受け取ったIMEReceiverは保持しておくこと
	 * Controller：CocoaInputの初期化から各OS向けに処理をされるもの
	 * 
	 * IMM32のAPIを使ってIMEを制御できる
	 * GuiTextField,GuiEditSign,GuiScreenBookのいずれも有効でない時（いわゆるプレイ中）IMM32のAPIでIMEをオフにする（全角入力でもマイクラが操作できる）
	 * 
	 * 
	 * WindowsはOSXと違って、全てのウィンドウで同一のIMEを使用しているらしい。
	 * ControllerにIMEOperatorのリストメンバを置く
	 * Controllerにウィンドウメッセージを受け取るコールバックを設定する。
	 * 
	 * 途中変換が発生したら途中変換文字列を受け取る
	 * アクティブ（看板とか本編集中ならリストメンバには本か看板のどちらか一つしか無い、サーバー編集画面とかなら複数のGuiTextFieldがあるのでForeachをつかってfocusedなIMEOperatorを見つけ出す）
	 * IMEOperatorに途中変換文字列（強調文字列範囲の取得も忘れずに）を渡す
	 * 	IMEOperatorがIMEReceiverのsetMarkedTextメソッドを使ってGuiTextFieldに渡す
	 * 
	 * 描画位置指定命令が出たら同じようにアクティブなIMEReceiverに適合するように描画位置を返す（GuiTextFieldはクラス書き換えで主要なメンバをprivate→publicにした）
	 * 	
	 * 確定文字列の扱い方
	 * 	正直に言ってわからない
	 * 	IMEReceiverのinsertTextに確定文字列を流す　これで途中変換から確定する
	 * 	ただしWindowsだと確定文字列はそのままCocoaInputを通らずにMinecraftに入る様子
	 * 	Minecraftに確定文字列が入る前に途中変換文字列を消す方法がわからない
	 */
	
	@Override
	public void CocoaInputInitialization(FMLInitializationEvent event) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		ModLogger.log("CocoaInput has been initialized.");
	}

	@Override
	public IMEOperator generateIMEOperator(IMEReceiver ime) {
		// TODO 自動生成されたメソッド・スタブ
		return new WindowsIMEOperator(ime);
	}

	@Override
	public String getLibraryPath() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String getLibraryName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
