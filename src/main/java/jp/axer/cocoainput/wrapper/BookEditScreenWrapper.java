package jp.axer.cocoainput.wrapper;

import java.util.List;
import java.util.Optional;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;
import jp.axer.cocoainput.util.Rect;
import jp.axer.cocoainput.util.WrapperUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.StringSplitter;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;

public class BookEditScreenWrapper extends IMEReceiver {
    private IMEOperator myIME;
    private BookEditScreen owner;

    public BookEditScreenWrapper(BookEditScreen field) {
        ModLogger.log("BookEditScreen init: " + field.hashCode());
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
        myIME.setFocused(true);
    }

    protected void setText(String text) {
    	if(owner.isSigning) {
    		owner.title=text;
    	}
    	else {
    		owner.setCurrentPageText(text);
    	}
    }

	protected String getText() {
		if(owner.isSigning) {
			return owner.title;
		}
		else {
			return owner.getCurrentPageText();
		}
	}

	protected void setCursorInvisible() {
		owner.frameTick=6;
	} //TODO

	protected int getCursorPos() {
		if(owner.isSigning) {
			return owner.titleEdit.getCursorPos();
		}
		else {
			return owner.pageEdit.getCursorPos();
		}
	}

	protected void setCursorPos(int p) {
		if(owner.isSigning) {
			owner.titleEdit.setCursorPos(p,true);
		}
		else {
			owner.pageEdit.setCursorPos(p,true);
		}
	}

	protected void setSelectionPos(int p) {
		if(owner.isSigning) {
			owner.titleEdit.setSelectionRange(p, p);
		}
		else {
			owner.pageEdit.setSelectionRange(p, p);
		}
	}


    @Override
    public Rect getRect() {
        Font fontRendererObj = null;
        try {
            fontRendererObj = WrapperUtil.makeFont(owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (owner.isSigning) {
            return new Rect(
                    (fontRendererObj.width(owner.title.substring(0, originalCursorPosition)) / 2 + ((owner.width - 192) / 2) + 36 + (116 - 0) / 2),
                    (50 + fontRendererObj.lineHeight),
                    0,
                    0
            );
        } else {
            StringSplitter manager = fontRendererObj.getSplitter();
            List<FormattedText> lines = manager.splitLines(owner.getCurrentPageText(), 116, Style.EMPTY);
            final String[] lastLine = new String[1];
            FormattedText.ContentConsumer acceptor = new FormattedText.ContentConsumer() {
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

    public int renewCursorCounter() {
        return owner.frameTick+(cursorVisible?1:0);
    }

}
