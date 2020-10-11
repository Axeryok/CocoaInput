package jp.axer.cocoainput.arch.dummy;

import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;

public class DummyController implements CocoaInputController{
    public DummyController() {
        ModLogger.log("This is a dummy controller.");
    }

    @Override
    public IMEOperator generateIMEOperator(IMEReceiver ime) {
        return new DummyIMEOperator();
    }
}