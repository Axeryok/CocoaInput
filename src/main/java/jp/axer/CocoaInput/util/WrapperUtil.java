package jp.axer.cocoainput.util;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;

import java.lang.reflect.Field;

public class WrapperUtil {
    public static FontRenderer makeFontRenderer(Screen owner) throws Exception {
        try {
            Field font = Screen.class.getDeclaredField("field_211127_e");
            font.setAccessible(true);
            return (FontRenderer) font.get(owner);
        } catch (Exception e) {
            try {
                Field font = Screen.class.getDeclaredField("font");
                font.setAccessible(true);
                return (FontRenderer) font.get(owner);
            } catch (Exception e1) {
                throw e1;
            }
        }
    }
}