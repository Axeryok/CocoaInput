package jp.axer.cocoainput.plugin;

import jp.axer.cocoainput.util.PreeditFormatter;
import jp.axer.cocoainput.util.Rect;
import jp.axer.cocoainput.util.Tuple3;

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
		if (!preeditBegin) {
			originalCursorPosition = this.getCursorPos();
		}
		preeditBegin = false;
		cursorVisible = true;
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
		//owner.selectionEnd = owner.cursorPosition;
	}

	public void setMarkedText(String aString, int position1, int length1, int position2, int length2) {
		if (!preeditBegin) {
			originalCursorPosition = this.getCursorPos();
			preeditBegin = true;
		}
		Tuple3<String, Integer, Boolean> formattedText = PreeditFormatter.formatMarkedText(aString, position1, length1);
		String str = formattedText._1();
		int caretPosition = formattedText._2() + 4;//相対値
		boolean hasCaret = formattedText._3();
		this.setText((new StringBuffer(this.getText()))
				.replace(originalCursorPosition, originalCursorPosition + length, str).toString());
		length = str.length();
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

	public  abstract Rect getRect();

	abstract protected void setText(String text);

	abstract protected String getText();

	abstract protected void setCursorInvisible();

	abstract protected int getCursorPos();

	abstract protected void setCursorPos(int p);

	abstract protected void setSelectionPos(int p);
}
