package jp.axer.cocoainput.wrapper;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.PreeditFormatter;
import jp.axer.cocoainput.util.Rect;
import jp.axer.cocoainput.util.WrapperUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.block.Blocks;

public class EditSignScreenWrapper implements IMEReceiver {
    private EditSignScreen owner;
    private IMEOperator myIME;
    private int length = 0;
    private boolean hasMarkedText = false;
    private int lengthBeforeMarkedText;

    public EditSignScreenWrapper(EditSignScreen field) {
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
        myIME.setFocused(true);
    }

    @Override
    public void insertText(String aString, int position1, int length1) {
        String text = owner.tileSign.getText(owner.editLine).getString();
        owner.tileSign.setText(owner.editLine, new StringTextComponent(
                new StringBuffer(text).replace(lengthBeforeMarkedText, lengthBeforeMarkedText + length, aString)
                        .toString()));
        hasMarkedText = false;
        length = 0;
    }

    @Override
    public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
        String str = PreeditFormatter.formatMarkedText(aString, position1, length1)._1();
        String text = owner.tileSign.getText(owner.editLine).getString();
        if (hasMarkedText == false) {
            hasMarkedText = true;
            lengthBeforeMarkedText = text.length();
        }
        owner.tileSign.setText(owner.editLine, new StringTextComponent(
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
        if (owner.tileSign.getBlockState().getBlock().getRegistryName().toString().contains("wall")) {
            y += 30;
        }
        return new Rect(owner.width / 2 + fontRendererObj.getStringWidth(owner.tileSign.getText(owner.editLine).getString()) / 2, y, 0, 0);
    }

}