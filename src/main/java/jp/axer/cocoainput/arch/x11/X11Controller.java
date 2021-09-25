package jp.axer.cocoainput.arch.x11;

import java.io.IOException;
import java.lang.reflect.Field;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWNativeX11;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class X11Controller implements CocoaInputController {

	static X11IMEOperator focusedOperator = null;

	Handle.DrawCallback c_draw = new Handle.DrawCallback() {
		public Pointer invoke(int caret, int chg_first, int chg_length, short length, boolean iswstring,
				String rawstring, WString rawwstring, int primary, int secondary, int tertiary) {
			Logger.log("Javaside draw begin");
			String string = iswstring ? rawwstring.toString() : rawstring;

			if (X11Controller.focusedOperator != null) {
				GLFW.glfwSetKeyCallback(window, null);
				X11Controller.focusedOperator.owner.setMarkedText(string, caret, tertiary - secondary, 0, 0);
			}
			Logger.log("Preedit:" + caret + " " + chg_first + " " + chg_length + " " + length + " " + primary + " "
					+ secondary + " " + tertiary + " " + string);
			int[] point = { 600, 600 };
			Memory memory = new Memory(8L);
			memory.write(0L, point, 0, 2);
			Logger.log("Javaside draw end");
			return (Pointer) memory;
		}
	};

	Handle.DoneCallback c_done = new Handle.DoneCallback() {
		public void invoke() {
			Logger.log("javaside preedit done");
			if (X11Controller.focusedOperator != null) {
				X11Controller.focusedOperator.owner.insertText("", 0, 0);
			}
			setupKeyboardEvent();
		}
	};

	public static void setupKeyboardEvent() {
		Minecraft.getInstance().keyboardHandler.setup(window);
		GLFW.glfwSetCharModsCallback(window, (p_228000_1_, p_228000_3_, p_228000_4_) -> {
			Minecraft.getInstance().execute(() -> {
				if (X11Controller.focusedOperator != null) {

					X11Controller.focusedOperator.owner.insertText(String.valueOf(Character.toChars(p_228000_3_)), 0,
							0);

				} else {
					Minecraft.getInstance().keyboardHandler.charTyped(p_228000_1_, p_228000_3_, p_228000_4_);
				}
			});
		});
	}

	private static final long window = Minecraft.getInstance().getWindow().getWindow();

	public X11Controller() throws IOException {

		setupKeyboardEvent();

		Logger.log("This is X11 Controller");
		CocoaInput.copyLibrary("libx11cocoainput.so", "x11/libx11cocoainput.so");
		Logger.log("Call clang initializer");
		Handle.INSTANCE.initialize(window, GLFWNativeX11.glfwGetX11Window(window), this.c_draw, this.c_done,
				Logger.clangLog, Logger.clangError, Logger.clangDebug);
		Handle.INSTANCE.set_focus(0);
		Logger.log("Finished clang initializer");
		Logger.log("X11Controller finished initialize");

	}

	@Override
	public IMEOperator generateIMEOperator(IMEReceiver arg0) {
		// TODO Auto-generated method stub
		return new X11IMEOperator(arg0);
	}

	@Override
	public void screenOpenNotify(Screen gui) {
		try {
			Field wrapper = gui.getClass().getField("wrapper");
			wrapper.setAccessible(true);
			if (wrapper.get(gui) instanceof IMEReceiver)
				return;
		} catch (Exception e) {
			/* relax */}
		if (X11Controller.focusedOperator != null) {
			X11Controller.focusedOperator.setFocused(false);
			X11Controller.focusedOperator = null;
		}
	}

}
