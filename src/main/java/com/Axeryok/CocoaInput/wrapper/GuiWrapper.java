package com.Axeryok.CocoaInput.wrapper;

import com.Axeryok.CocoaInput.plugin.IMEReceiver;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;

import java.lang.reflect.Field;

public abstract class GuiWrapper implements IMEReceiver {
    GuiEditSign owner;

    FontRenderer makeFontRenderer() throws Exception {
        try {
            Field font=GuiScreen.class.getDeclaredField("field_146289_q");
            font.setAccessible(true);
            return (FontRenderer) font.get(owner);
        } catch (Exception e) {
            try {
                Field font = GuiScreen.class.getDeclaredField("fontRendererObj");
                font.setAccessible(true);
                return (FontRenderer) font.get(owner);
            }
            catch (Exception e1) {
                throw e1;
            }
        }
    }
}
