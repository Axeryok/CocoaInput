package jp.axer.cocoainput.util;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;

import java.lang.reflect.Field;

public class WrapperUtil {
    public static FontRenderer makeFontRenderer(Screen owner) throws Exception {
        return owner.font;
    }
}
