package jp.axer.cocoainput.arch.win;

import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;

public class WinIMEOperator implements IMEOperator {
	public IMEReceiver owner;
	private boolean focus=false;
	public WinIMEOperator(IMEReceiver op) {
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
			WinController.focusedOperator=this;
			Handle.INSTANCE.set_focus(1);
		}
		else {
			if(WinController.focusedOperator==this) {
				owner.insertText("", 0, 0);
				WinController.focusedOperator=null;
				Handle.INSTANCE.set_focus(0);
			}
		}

	}

}
