/**
 * 
 */
package main.java.util;

import java.math.BigDecimal;

/**
 * トークンの実装です。
 */
public final class Token {
    private Enum<?> type;
    private final StringBuilder sb;

    /**
     * 空のトークンを生成します。
     */
    public Token() {
        sb = new StringBuilder();
    }

    /**
     * トークンの末尾に1文字追加します。
     *
     * @param ch 追加する文字
     * @return このトークン自体
     */
    public Token append(char ch) {
        sb.append(ch);
        return this;
    }

    /**
     * トークンの文字列表現を返します。
     *
     * @return 文字列で表したトークン
     */
    public String toString() {
        return sb.toString();
    }

    /**
     * トークンの種類を設定します。
     *
     * @param 種類(演算子や数値など)
     * @return このトークン自体
     */
    public Token setType(Enum<?> type) {
        this.type = type;
        return this;
    }

    /**
     * トークンの種類を返します。
     *
     * @return 種類
     */
    public Enum<?> getType() {
        return type;
    }

    /**
     * トークンが数値を表す場合に、トークンの表す数値を返します。
     *
     * @return トークンの数値
     */
    public BigDecimal toNumber() {
        return new BigDecimal(sb.toString());
    }

    /**
     * トークンが指定された文字列と同じであるか返します。
     *
     * @return 同じ場合に限りtrue
     */
    public boolean equals(Object obj) {
        return sb.toString().equals(String.valueOf(obj));
    }
}
