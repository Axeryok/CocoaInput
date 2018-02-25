package com.Axeryok.CocoaInput.arch.dummy;

import com.Axeryok.CocoaInput.ModLogger;
import com.Axeryok.CocoaInput.plugin.Controller;
import com.Axeryok.CocoaInput.plugin.IMEOperator;
import com.Axeryok.CocoaInput.plugin.IMEReceiver;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class DummyController implements Controller{

	public DummyController() {
		// TODO 自動生成されたメソッド・スタブ
		ModLogger.log("This is a dummy controller.");
	}

	@Override
	public IMEOperator generateIMEOperator(IMEReceiver ime) {
		// TODO 自動生成されたメソッド・スタブ
		return new DummyIMEOperator();
	}


}
