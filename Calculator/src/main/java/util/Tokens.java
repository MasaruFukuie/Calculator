package main.java.util;

/**
 * 演算子
 */
enum Operators {
    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/"),
    MOD("%"),
    POW("**"),
    OPEN_PARENS("("),
    CLOSE_PARENS(")");
    private final String desc;

    private Operators(String desc) {
        this.desc = desc;
    }

    public String toString() {
        return desc;
    }
}

/**
 * その他のトークンの種別です。
 */
enum Tokens {
    NUMBER; //上記以外のトークンは数値しかない
}
