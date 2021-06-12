package jp.axer.cocoainput.wrapper;

import java.util.List;
import java.util.Optional;

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
        if (owner.isSigning) {
            owner.title = new StringBuffer(owner.title).replace(lengthBeforeMarkedText,
                    lengthBeforeMarkedText + length, aString).toString();
            hasMarkedText = false;
            length = 0;
        } else {
            owner.setCurrentPageText(new StringBuffer(owner.getCurrentPageText()).replace(
                    lengthBeforeMarkedText, lengthBeforeMarkedText + length, aString).toString());
            hasMarkedText = false;
            length = 0;
        }
    }

    @Override
    public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
        String str = PreeditFormatter.formatMarkedText(aString, position1, length1)._1();
        if (owner.isSigning) {
            if (hasMarkedText == false) {
                hasMarkedText = true;
                lengthBeforeMarkedText = owner.title.length();
            }
            owner.title = new StringBuffer(owner.title).replace(lengthBeforeMarkedText,
                    lengthBeforeMarkedText + length, str).toString();
            length = str.length();
        } else {
            if (hasMarkedText == false) {
                hasMarkedText = true;
                lengthBeforeMarkedText = owner.getCurrentPageText().length();
            }
            owner.setCurrentPageText(new StringBuffer(owner.getCurrentPageText()).replace(
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
        if (owner.isSigning) {
            return new Rect(
                    (fontRendererObj.width(owner.title) / 2 + ((owner.width - 192) / 2) + 36 + (116 - 0) / 2),
                    (50 + fontRendererObj.lineHeight),
                    0,
                    0
            );
        } else {
            CharacterManager manager = fontRendererObj.getSplitter();
            List<ITextProperties> lines = manager.splitLines(owner.getCurrentPageText(), 116, Style.EMPTY);
            final String[] lastLine = new String[1];
            ITextProperties.ITextAcceptor acceptor = new ITextProperties.ITextAcceptor() {
                @Override
                public Optional accept(String p_accept_1_) {
                    lastLine[0] = p_accept_1_;
                    return Optional.empty();
                }
            };
            lines.get(lines.size() - 1).visit(acceptor);
            return new Rect(
                    (((owner.width - 192) / 2) + 36 + fontRendererObj.width(lastLine[0])),
                    (34 + lines.size() * fontRendererObj.lineHeight),
                    0,
                    0
            );
        }
    }

}
