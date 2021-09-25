package jp.axer.cocoainput.arch.darwin;

import java.lang.reflect.Field;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;
import net.minecraft.client.gui.screens.Screen;

public class DarwinController implements CocoaInputController {
    public DarwinController() throws Exception {
        CocoaInput.copyLibrary("libcocoainput.dylib", "darwin/libcocoainput.dylib");
        Handle.INSTANCE.initialize(CallbackFunction.Func_log, CallbackFunction.Func_error, CallbackFunction.Func_debug);
        ModLogger.log("DarwinController has been initialized.");
    }

    @Override
    public IMEOperator generateIMEOperator(IMEReceiver ime) {
        return new DarwinIMEOperator(ime);
    }


	@Override
	public void screenOpenNotify(Screen gui) {
	        try {
	            Field wrapper = gui.getClass().getField("wrapper");
	            wrapper.setAccessible(true);
	            if (wrapper.get(gui) instanceof IMEReceiver)
	                return;
	        } catch (Exception e) {/* relax */}
	        Handle.INSTANCE.refreshInstance();//GUIの切り替えでIMの使用をoffにする

	}
}
