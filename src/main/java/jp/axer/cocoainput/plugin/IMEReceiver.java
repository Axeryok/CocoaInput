package jp.axer.cocoainput.plugin;

import jp.axer.cocoainput.CocoaInput;
import jp.axer.cocoainput.arch.win.Logger;
import jp.axer.cocoainput.util.PreeditFormatter;
import jp.axer.cocoainput.util.Rect;
import jp.axer.cocoainput.util.Tuple3;
import net.minecraft.client.Minecraft;

public abstract class IMEReceiver {

	private int length = 0;
	protected boolean cursorVisible = true;
	private boolean preeditBegin = false;
	protected int originalCursorPosition = 0;

	/*
	 * position1 length1は下線と強調変換のため必須 position2 length2は意味をなしてない
	 * positionの位置から文字数lengthの範囲という意味
	 */
	public void insertText(String aString, int position1, int length1) {//確定文字列 現状aString以外の引数は意味をなしてない
		if (true) {
			Logger.log("just comming:(" + aString + ") now:(" + getText() + ")");
		}
		if (!preeditBegin) {
			originalCursorPosition = this.getCursorPos();
		}
		preeditBegin = false;
		cursorVisible = true;
		this.setText((new StringBuffer(this.getText()))
				.replace(originalCursorPosition, originalCursorPosition + length, "").toString());
		length = 0;
		this.setCursorPos(originalCursorPosition);
		this.setSelectionPos(originalCursorPosition);
		if (CocoaInput.config.isNativeCharTyped()) {
			this.insertTextNative(aString);
		} else {
			this.insertTextEmurated(aString);
		}
		/*
		if (aString.length() == 0) {
			this.setText((new StringBuffer(this.getText()))
					.replace(originalCursorPosition, originalCursorPosition + length, "").toString());
			length = 0;
			this.setCursorPos(originalCursorPosition);
			this.setSelectionPos(originalCursorPosition);
			return;
		}
		this.setText((new StringBuffer(this.getText()))
				.replace(originalCursorPosition, originalCursorPosition + length,
						aString.substring(0, aString.length()))
				.toString());
		length = 0;
		this.setCursorPos(originalCursorPosition + aString.length());
		this.notifyParent(this.getText());
		//owner.selectionEnd = owner.cursorPosition;
		 */
	}

	public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
		if (!preeditBegin) {
			originalCursorPosition = this.getCursorPos();
			preeditBegin = true;
		}
		int caretPosition;
		boolean hasCaret;
		String commitString;
		if (CocoaInput.config.isAdvancedPreeditDraw()) {
			Tuple3<String, Integer, Boolean> formattedText = PreeditFormatter.formatMarkedText(aString, position1,
					length1);
			commitString = formattedText._1();
			caretPosition = formattedText._2() + 4;//相対値
			hasCaret = formattedText._3();
			
		}
		else {
			hasCaret=true;
			caretPosition=0;
			commitString=PreeditFormatter.SECTION+"n"+aString+PreeditFormatter.SECTION+"r";
		}
		this.setText((new StringBuffer(this.getText()))
				.replace(originalCursorPosition, originalCursorPosition + length, commitString).toString());
		length = commitString.length();
		if (hasCaret) {
			this.cursorVisible = true;
			this.setCursorPos(originalCursorPosition + caretPosition);
			this.setSelectionPos(originalCursorPosition + caretPosition);
		} else {
			this.cursorVisible = false;
			this.setCursorInvisible();
			this.setCursorPos(originalCursorPosition);
			this.setSelectionPos(originalCursorPosition);
			//owner.selectionEnd=owner.cursorPosition;
		}
	}

	public abstract Rect getRect();

	abstract protected void setText(String text);

	abstract protected String getText();

	abstract protected void setCursorInvisible();

	abstract protected int getCursorPos();

	abstract protected void setCursorPos(int p);

	abstract protected void setSelectionPos(int p);

	protected void insertTextNative(String text) {
		for (char c : text.toCharArray()) {
			Minecraft instance = Minecraft.getInstance();
			instance.keyboardHandler.charTyped(instance.getWindow().getWindow(), c, 0);
		}
	}

	protected void insertTextEmurated(String aString) {
		this.setText((new StringBuffer(this.getText()))
				.replace(this.getCursorPos(), this.getCursorPos(),
						aString.substring(0, aString.length()))
				.toString());
		length = 0;
		this.setCursorPos(this.getCursorPos() + aString.length());
		this.notifyParent(this.getText());
	}

	protected void notifyParent(String text) {
	};
}
