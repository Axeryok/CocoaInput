package jp.axer.cocoainput.arch.x11;

import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;

public class X11IMEOperator implements IMEOperator {
	public IMEReceiver owner;
	private boolean focus=false;
	public X11IMEOperator(IMEReceiver op) {
		this.owner=op;
	}
	
	@Override
	public void discardMarkedText() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeInstance() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocused(boolean arg0) {
		// TODO Auto-generated method stub
		if(arg0==focus) {
			return ;
		}
		focus=arg0;
		Logger.log("setFocusedCalled "+ arg0);
		if(arg0) {
			X11Controller.focusedOperator=this;
			Handle.INSTANCE.set_focus(1);
		}
		else {
			if(X11Controller.focusedOperator==this) {
				owner.insertText("", 0, 0);
				X11Controller.focusedOperator=null;
				Handle.INSTANCE.set_focus(0);
				X11Controller.setupKeyboardEvent();
			}
		}

	}

}
