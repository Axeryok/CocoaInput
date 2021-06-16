package jp.axer.cocoainput.wrapper;

import java.util.List;
import java.util.Optional;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.plugin.IMEOperator;
import jp.axer.cocoainput.plugin.IMEReceiver;
import jp.axer.cocoainput.util.ModLogger;
import jp.axer.cocoainput.util.PreeditFormatter;
import jp.axer.cocoainput.util.Rect;
import jp.axer.cocoainput.util.Tuple3;
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
    
    private boolean cursorVisible=true;
    private int originalCursorPosition=0;

    public EditBookScreenWrapper(EditBookScreen field) {
        ModLogger.log("EditBookScreen init: " + field.hashCode());
        owner = field;
        myIME = CocoaInput.getController().generateIMEOperator(this);
        myIME.setFocused(true);
    }

    @Override
    public void insertText(String aString, int position1, int length1) {
    	
        if (owner.isSigning) {
            if(!this.hasMarkedText) {
            	originalCursorPosition=owner.titleEdit.getCursorPos();
            }
            this.hasMarkedText=false;
            this.cursorVisible=true;
            if(aString.length()==0) {
            	owner.title = new StringBuffer(owner.title).replace(originalCursorPosition,originalCursorPosition + length, aString).toString();
            	length=0;
            	owner.titleEdit.setCursorPos(originalCursorPosition,true);
            	owner.titleEdit.setSelectionRange(originalCursorPosition,originalCursorPosition);
            	return;
            }
            length=0;
            owner.titleEdit.setCursorPos(originalCursorPosition,true);
            
        } else {
        	if(!this.hasMarkedText) {
            	originalCursorPosition=owner.pageEdit.getCursorPos();
            }
            this.hasMarkedText=false;
            this.cursorVisible=true;
            if(aString.length()==0) {
            	owner.setCurrentPageText(new StringBuffer(owner.getCurrentPageText()).replace(originalCursorPosition,originalCursorPosition + length, aString).toString());
            	length=0;
            	owner.pageEdit.setCursorPos(originalCursorPosition,true);
            	owner.pageEdit.setSelectionRange(originalCursorPosition,originalCursorPosition);
            	return;
            }
            length=0;
            owner.pageEdit.setCursorPos(originalCursorPosition,true);
        	
        	
        }
    }

    @Override
    public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
    	if(owner.isSigning) {
    		if(!this.hasMarkedText) {
    			originalCursorPosition = owner.titleEdit.getCursorPos();
    			hasMarkedText=true;
    		}
    		Tuple3<String, Integer, Boolean> formattedText = PreeditFormatter.formatMarkedText(aString, position1, length1);
            String str = formattedText._1();
            int caretPosition = formattedText._2()+4;//相対値
            boolean hasCaret = formattedText._3();
            owner.title = new StringBuffer(owner.title).replace(originalCursorPosition, originalCursorPosition + length, str).toString();
            length = str.length();
            if (hasCaret) {
                this.cursorVisible = true;
                owner.titleEdit.setCursorPos(originalCursorPosition + caretPosition,true);
                owner.titleEdit.setSelectionRange(originalCursorPosition + caretPosition,originalCursorPosition + caretPosition);
            } else {
                this.cursorVisible = false;
                //TODO need to enable -> owner.frame = 6;
                owner.titleEdit.setCursorPos(originalCursorPosition,true);
                //owner.selectionEnd=owner.cursorPosition;
            }
    	}
    	else {
    		if(!this.hasMarkedText) {
    			originalCursorPosition = owner.pageEdit.getCursorPos();
    			hasMarkedText=true;
    		}
    		Tuple3<String, Integer, Boolean> formattedText = PreeditFormatter.formatMarkedText(aString, position1, length1);
            String str = formattedText._1();
            int caretPosition = formattedText._2()+4;//相対値
            boolean hasCaret = formattedText._3();
            owner.setCurrentPageText(new StringBuffer(owner.getCurrentPageText()).replace(originalCursorPosition, originalCursorPosition + length, str).toString());
            length = str.length();
            if (hasCaret) {
                this.cursorVisible = true;
                owner.pageEdit.setCursorPos(originalCursorPosition + caretPosition,true);
                owner.pageEdit.setSelectionRange(originalCursorPosition + caretPosition,originalCursorPosition + caretPosition);
            } else {
                this.cursorVisible = false;
                //TODO need to enable -> owner.frame = 6;
                owner.pageEdit.setCursorPos(originalCursorPosition,true);
                owner.pageEdit.setSelectionRange(originalCursorPosition,originalCursorPosition);
                //owner.selectionEnd=owner.cursorPosition;
            }
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
                    (fontRendererObj.width(owner.title.substring(0, originalCursorPosition)) / 2 + ((owner.width - 192) / 2) + 36 + (116 - 0) / 2),
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
