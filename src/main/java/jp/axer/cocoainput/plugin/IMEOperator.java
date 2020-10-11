package jp.axer.cocoainput.plugin;

public interface IMEOperator {
    void setFocused(boolean inFocused);//保持するIMEのフォーカスが変化した時に呼び出される
    void discardMarkedText();//実装してないので呼び出されない（いつか実装する）

    //TODO removeInstanceを実装する（現時点では呼ばれない）
    void removeInstance();//看板や本の編集が終わった時に呼び出される　テキストフィールドは廃棄位置が曖昧なため呼び出されない

}