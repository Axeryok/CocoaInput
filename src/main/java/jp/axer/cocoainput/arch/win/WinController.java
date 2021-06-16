package jp.axer.cocoainput.arch.win;
import java.io.IOException;
import java.lang.reflect.Field;

import com.sun.jna.Pointer;
import com.sun.jna.WString;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.arch.win.Handle.DoneCallback;
import jp.axer.cocoainput.arch.win.Handle.PreeditCallback;
import jp.axer.cocoainput.arch.win.Handle.RectCallback;
import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.Rect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WinController implements CocoaInputController {

	static WinIMEOperator focusedOperator = null;

	PreeditCallback pc = new PreeditCallback() {
		@Override
		public void invoke(WString str, int cursor, int length) {
			// TODO 自動生成されたメソッド・スタブ
			if(focusedOperator!=null) {
				Logger.log("marked "+str.toString()+" "+cursor+" "+length);
				focusedOperator.owner.setMarkedText(str.toString(), cursor, length,0,0);
			}
		}
	};
	DoneCallback dc = new DoneCallback() {
		@Override
		public void invoke(WString str) {
			// TODO 自動生成されたメソッド・スタブ
			if(focusedOperator!=null) {
				focusedOperator.owner.insertText(str.toString(), 0, 0);
				Logger.log("done %s",str.toString());
			}
		}
	};

	RectCallback rc = new RectCallback() {
		@Override
		public int invoke(Pointer ret) {
			// TODO 自動生成されたメソッド・スタブ
			if(focusedOperator!=null) {
				Logger.log("Rect callback");
				Rect point = focusedOperator.owner.getRect();
                float[] buff;
                if (point == null) {
                    buff = new float[]{0, 0, 0, 0};
                } else {
                    buff = new float[]{point.getX(), point.getY(), point.getWidth(), point.getHeight()};
                }
                double factor = CocoaInput.getScreenScaledFactor();
                buff[0] *= factor;
                buff[1] *= factor;
                buff[2] *= factor;
                buff[3] *= factor;

                ret.write(0, buff, 0, 4);
                return 0;
			}
			return 1;
		}

	};

	public WinController() {
		Logger.log("This is Windows Controller");
		try {
			CocoaInput.copyLibrary("libwincocoainput.dll", "win/libwincocoainput.dll");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		MinecraftForge.EVENT_BUS.register(this);
    	Handle.INSTANCE.initialize(org.lwjgl.glfw.GLFWNativeWin32.glfwGetWin32Window(Minecraft.getInstance().getWindow().getWindow()), pc, dc,rc, Logger.clangLog, Logger.clangError, Logger.clangDebug);

	}


	@Override
	public IMEOperator generateIMEOperator(IMEReceiver arg0) {
		// TODO 自動生成されたメソッド・スタブ
		return new WinIMEOperator(arg0);
	}
	@SubscribeEvent
	public void didChangeGui(GuiOpenEvent event) {
		Screen gui = event.getGui();
		try {
			Field wrapper = gui.getClass().getField("wrapper");
			wrapper.setAccessible(true);
			if (wrapper.get(gui) instanceof IMEReceiver)
				return;
		} catch (Exception e) {
			/* relax */}
		if (WinController.focusedOperator != null) {
			//WinIMEOperator old=WinController.focusedOperator;
			//WinController.focusedOperator=null;
			WinController.focusedOperator.setFocused(false);
		}
	}

}
