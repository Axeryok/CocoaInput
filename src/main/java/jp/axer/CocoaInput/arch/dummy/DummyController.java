package jp.axer.CocoaInput.arch.dummy;

import jp.axer.CocoaInput.plugin.CocoaInputController;
import jp.axer.CocoaInput.plugin.IMEOperator;
import jp.axer.CocoaInput.plugin.IMEReceiver;
import jp.axer.CocoaInput.util.ModLogger;

public class DummyController implements CocoaInputController{
    public DummyController() {
        ModLogger.log("This is a dummy controller.");
    }

    @Override
    public IMEOperator generateIMEOperator(IMEReceiver ime) {
        return new DummyIMEOperator();
    }
}