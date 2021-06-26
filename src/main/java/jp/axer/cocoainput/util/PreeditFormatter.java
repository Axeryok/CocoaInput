package jp.axer.cocoainput.util;

public class PreeditFormatter {
    public static final char SECTION = 167; // avoid shift-jis bug...

    public static Tuple3<String, Integer, Boolean> formatMarkedText(String aString, int position1, int length1) {//ユーティリティ
        StringBuilder builder = new StringBuilder(aString);
        boolean hasCaret = length1 == 0;
        if (!hasCaret) {//主文節がある
            builder.insert(position1 + length1, SECTION+"r"+SECTION+"n");//主文節の終わりで修飾をリセットして下線修飾をセット
            builder.insert(position1, SECTION+"l");//主文節の始まりで太字修飾を追加
        } else {//主文説がない（キャレットが存在するのでそれを意識する）
            builder.insert(position1, SECTION+"r"+SECTION+"n");
        }
        builder.insert(0, SECTION+"r"+SECTION+"n");//最初に下線修飾をセット
        builder.append(SECTION+"r");//最後に修飾をリセット
        return new Tuple3(new String(builder), position1, hasCaret);
    }
}
