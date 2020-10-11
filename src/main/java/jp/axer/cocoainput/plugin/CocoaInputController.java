package jp.axer.cocoainput.plugin;

public interface CocoaInputController {
    IMEOperator generateIMEOperator(IMEReceiver ime); //GuiTextFieldとかが作成された時に割り当てるIMEOperator生成処理を委託
}
