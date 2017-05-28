package com.Axeryok.CocoaInput.arch.dummy;

import com.Axeryok.CocoaInput.IMEReceiver;
import com.Axeryok.CocoaInput.ModLogger;
import com.Axeryok.CocoaInput.impl.Controller;
import com.Axeryok.CocoaInput.impl.IMEOperator;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class DummyController implements Controller{

	@Override
	public void CocoaInputInitialization(FMLInitializationEvent event) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		ModLogger.log("This is a dummy controller.");
	}

	@Override
	public IMEOperator generateIMEOperator(IMEReceiver ime) {
		// TODO 自動生成されたメソッド・スタブ
		return new DummyIMEOperator();
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
