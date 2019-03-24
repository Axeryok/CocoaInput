package jp.axer.CocoaInput.wrapper;

import jp.axer.CocoaInput.CocoaInput;
import jp.axer.CocoaInput.plugin.IMEOperator;
import jp.axer.CocoaInput.plugin.IMEReceiver;
import jp.axer.CocoaInput.util.PreeditFormatter;
import jp.axer.CocoaInput.util.Rect;
import jp.axer.CocoaInput.util.WrapperUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.init.Blocks;
import net.minecraft.util.text.TextComponentString;

public class GuiEditSignWrapper implements IMEReceiver {
    private GuiEditSign owner;
    private IMEOperator myIME;
    private int length = 0;
    private boolean hasMarkedText = false;
    private int lengthBeforeMarkedText;

    public GuiEditSignWrapper(GuiEditSign field) {
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
        myIME.setFocused(true);
    }

    @Override
    public void insertText(String aString, int position1, int length1) {
        String text = owner.tileSign.func_212366_a(owner.editLine).getString();
        owner.tileSign.func_212365_a(owner.editLine, new TextComponentString(
                new StringBuffer(text).replace(lengthBeforeMarkedText, lengthBeforeMarkedText + length, aString)
                        .toString()));
        hasMarkedText = false;
        length = 0;
    }

    @Override
    public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
        String str = PreeditFormatter.formatMarkedText(aString, position1, length1)._1();
        String text = owner.tileSign.func_212366_a(owner.editLine).getString();
        if (hasMarkedText == false) {
            hasMarkedText = true;
            lengthBeforeMarkedText = text.length();
        }
        owner.tileSign.func_212365_a(owner.editLine, new TextComponentString(
                new StringBuffer(text).replace(lengthBeforeMarkedText, lengthBeforeMarkedText + length, str)
                        .toString()));
       /* owner.tileSign.signText[owner.editLine] = new TextComponentString(
                new StringBuffer(text).replace(lengthBeforeMarkedText, lengthBeforeMarkedText + length, str)
                        .toString());*/
        length = str.length();
    }

    @Override
    public Rect getRect() {
        FontRenderer fontRendererObj = null;
        try {
            fontRendererObj = WrapperUtil.makeFontRenderer(owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        float y = 91 + (owner.editLine - 1) * (10);
        if (owner.tileSign.getBlockState().getBlock() != Blocks.SIGN) {
            y += 30;
        }
        return new Rect(owner.width / 2 + fontRendererObj.getStringWidth(owner.tileSign.func_212366_a(owner.editLine).getString()) / 2, y, 0, 0);
    }

}