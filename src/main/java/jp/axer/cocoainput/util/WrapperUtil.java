package jp.axer.cocoainput.util;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;


public class WrapperUtil {
    public static Font makeFont(Screen owner) throws Exception {
        return owner.font;
    }
}
