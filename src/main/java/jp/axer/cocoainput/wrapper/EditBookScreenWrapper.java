package jp.axer.cocoainput.wrapper;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;
import jp.axer.cocoainput.util.PreeditFormatter;
import jp.axer.cocoainput.util.Rect;
import jp.axer.cocoainput.util.WrapperUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.EditBookScreen;
import net.minecraft.util.text.CharacterManager;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.Style;

import java.util.List;
import java.util.Optional;

public class EditBookScreenWrapper implements IMEReceiver {
    private IMEOperator myIME;
    private EditBookScreen owner;
    private int length = 0;
    private boolean hasMarkedText = false;
    private int lengthBeforeMarkedText;

    public EditBookScreenWrapper(EditBookScreen field) {
        ModLogger.log("EditBookScreen init: " + field.hashCode());
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
        myIME.setFocused(true);
    }

    @Override
    public void insertText(String aString, int position1, int length1) {
        if (owner.bookGettingSigned) {
            owner.bookTitle = new StringBuffer(owner.bookTitle).replace(lengthBeforeMarkedText,
                    lengthBeforeMarkedText + length, aString).toString();
            hasMarkedText = false;
            length = 0;
        } else {
            owner.func_214217_j(new StringBuffer(owner.getCurrPageText()).replace(
                    lengthBeforeMarkedText, lengthBeforeMarkedText + length, aString).toString());
            hasMarkedText = false;
            length = 0;
        }
    }

    @Override
    public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
        String str = PreeditFormatter.formatMarkedText(aString, position1, length1)._1();
        if (owner.bookGettingSigned) {
            if (hasMarkedText == false) {
                hasMarkedText = true;
                lengthBeforeMarkedText = owner.bookTitle.length();
            }
            owner.bookTitle = new StringBuffer(owner.bookTitle).replace(lengthBeforeMarkedText,
                    lengthBeforeMarkedText + length, str).toString();
            length = str.length();
        } else {
            if (hasMarkedText == false) {
                hasMarkedText = true;
                lengthBeforeMarkedText = owner.getCurrPageText().length();
            }
            owner.func_214217_j(new StringBuffer(owner.getCurrPageText()).replace(
                    lengthBeforeMarkedText, lengthBeforeMarkedText + length, str).toString());
            length = str.length();
        }
    }

    @Override
    public Rect getRect() {
        FontRenderer fontRendererObj = null;
        try {
            fontRendererObj = WrapperUtil.makeFontRenderer(owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (owner.bookGettingSigned) {
            return new Rect(
                    (fontRendererObj.getStringWidth(owner.bookTitle) / 2 + ((owner.width - 192) / 2) + 36 + (116 - 0) / 2),
                    (50 + fontRendererObj.FONT_HEIGHT),
                    0,
                    0
            );
        } else {
            CharacterManager manager = fontRendererObj.getCharacterManager();
            List<ITextProperties> lines = manager.func_238365_g_(owner.getCurrPageText(), 116, Style.EMPTY);
            final String[] lastLine = new String[1];
            ITextProperties.ITextAcceptor acceptor = new ITextProperties.ITextAcceptor() {
                @Override
                public Optional accept(String p_accept_1_) {
                    lastLine[0] = p_accept_1_;
                    return Optional.empty();
                }
            };
            lines.get(lines.size() - 1).getComponent(acceptor);
            return new Rect(
                    (((owner.width - 192) / 2) + 36 + fontRendererObj.getStringWidth(lastLine[0])),
                    (34 + lines.size() * fontRendererObj.FONT_HEIGHT),
                    0,
                    0
            );
        }
    }

}
