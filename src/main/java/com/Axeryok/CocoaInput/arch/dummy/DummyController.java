package com.Axeryok.CocoaInput.arch.dummy;

import com.Axeryok.CocoaInput.ModLogger;
import com.Axeryok.CocoaInput.plugin.Controller;
import com.Axeryok.CocoaInput.plugin.IMEOperator;
import com.Axeryok.CocoaInput.plugin.IMEReceiver;

public class DummyController implements Controller{
	public DummyController() {
		ModLogger.log("This is a dummy controller.");
	}

	@Override
	public IMEOperator generateIMEOperator(IMEReceiver ime) {
		return new DummyIMEOperator();
	}
}
