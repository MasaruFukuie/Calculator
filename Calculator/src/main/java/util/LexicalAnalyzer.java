package main.java.util;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

import javax.script.ScriptException;

/**
* 字句解析器
*/
public final class LexicalAnalyzer {
    private final Operators[] opers = Operators.values();
    private LineNumberReader reader;
    private String line;
    private int index;
    private Token next = null;

    /**
     * ソースコードを読み込むリーダを設定します。
     *
     * @param reader リーダ
     * @throws ScriptException 読み込みに失敗した場合
     */
    public void load(Reader reader) throws ScriptException {
        this.reader = new LineNumberReader(reader);
        readNextLine();
    }

    /**
     * 現在の参照行の次の行を読み込みます。
     *
     * @throws ScriptException 読み込みに失敗した場合
     */
    private String readNextLine() throws ScriptException {
        try {
            while ((line = reader.readLine()) != null) {
                if (line.length() != 0)
                    break;
            }
            index = 0;
            return line;
        } catch (IOException ex) {
            throw new ScriptException(ex);
        }
    }

    /**
     * 指定された桁位置の文字を返します。
     *
     * @param column 桁
     * @return 文字
     * @throws ScriptException 位置が無効の場合
     */
    private char charAt(int column) throws ScriptException {
        if (column < line.length())
            return line.charAt(column);
        if (readNextLine() != null)
            return line.charAt(0);
        else
            return '\0'; // ファイルの末尾に達した場合
    }

    /**
     * トークンに指定文字を追加すると演算子の一部になるか検査します。
     *
     * @param token 検査対象のトークン
     * @param ch 追加する文字
     * @return 演算子の場合true
     */
    private boolean isOperator(Token token, char ch) {
        String target = token + Character.toString(ch);
        for (Operators oper : opers) {
            if (oper.toString().indexOf(target) >= 0)
                return true;
        }
        return false;
    }

    /**
     * トークンに対応する演算子を返します。
     *
     * @param token トークン
     * @return 対応する演算子
     */
    private Enum<?> getOperator(Token token) {
        for (Enum<?> oper : opers) {
            if (token.equals(oper.toString()))
                return oper;
        }
        return null;
    }

    /**
     * 構文違反があった場合に例外を通知します。
     *
     * @param ch 構文違反があった場所の文字
     * @return 例外を生成します。
     */
    private ScriptException error(char ch) {
        index++;
        return error("Character \'" + ch + "\' is illegal here.");
    }

    /**
     * 構文違反があった場合に例外を通知します。
     *
     * @return 生成した例外
     */
    private ScriptException error(Object msg) {
        int line = reader.getLineNumber();
        return new ScriptException(
                msg + " at line : " + line +
                        "\n => " + getLine(), null, line, index + 1);
    }

    /**
     * 次のトークンを取得します。リーダーが終端に達した場合、nullを返します。
     *
     * @return 次のトークン
     * @throws ScriptException 字句エラーがあった場合
     */
    public Token getNextToken() throws ScriptException {
        Token token = next;
        if (next != null) {
            next = null;
            return token;
        }
        return read(new Token());
    }

    /**
     * 直前に取得済みのトークンをスタックに待避します。
     *
     * @param token 待避させるトークン
     */
    public void ungetToken(Token token) {
        this.next = token; // 最大で大きさ1のスタック
    }

    /**
     * 次のトークンを取得します。リーダーが終端に達した場合、nullを返します。
     *
     * @return 次のトークン
     * @throws ScriptException 字句エラーがあった場合
     */
    private Token read(Token token) throws ScriptException {
        while (line != null) {
            char ch = charAt(index);
            index++;
            if (Character.isDigit(ch))
                return readInteger(token.append(ch));
            if (ch == '\0')
                return token;
            if (isOperator(token, ch))
                return readOperator(token.append(ch));
            if (!Character.isWhitespace(ch))
                throw error(ch);
            if (index >= line.length())
                readNextLine();
        }
        return null;
    }

    /**
     * 整数リテラルを取得します。リーダーが終端に達した場合、nullを返します。
     *
     * @return 次のトークン
     * @throws ScriptException 字句エラーがあった場合
     */
    private Token readInteger(Token token) throws ScriptException {
        while (line != null) {
            char ch = charAt(index);
            index++;
            if (Character.isDigit(ch))
                token.append(ch);
            else if (ch == '.' && Character.isDigit(charAt(index)))
                return readNumber(token.append(ch));
            else {
                index--;
                return token.setType(Tokens.NUMBER);
            }
        }
        return null;
    }

    /**
     * 小数リテラルを取得します。リーダーが終端に達した場合、nullを返します。
     *
     * @return 次のトークン
     * @throws ScriptException 字句エラーがあった場合
     */
    private Token readNumber(Token token) throws ScriptException {
        while (line != null) {
            char ch = charAt(index);
            index++;
            if (Character.isDigit(ch))
                token.append(ch);
            else {
                index--;
                return token.setType(Tokens.NUMBER);
            }
        }
        return null;
    }

    /**
     * 演算子を取得します。リーダーが終端に達した場合、nullを返します。
     *
     * @return 次のトークン
     * @throws ScriptException 字句エラーがあった場合
     */
    private Token readOperator(Token token)
            throws ScriptException {
        while (line != null) {
            char ch = charAt(index);
            index++;
            if (isOperator(token, ch)) {
                token.append(ch);
            } else {
                index--;
                return token.setType(getOperator(token));
            }
        }
        return null;
    }

    /**
     * 次のトークンが適切な型であるか確認します。
     *
     * @param expected 期待されるトークンの型
     * @return 次のトークン
     * @throws IOException 期待されないトークンが検出された場合
     */
    public Token checkNextToken(Enum<?>... expected)
            throws ScriptException {
        Token next = getNextToken();
        if (next != null) {
            for (Enum<?> exp : expected) {
                if (next.getType() == exp)
                    return next;
            }
        }
        throw error("\"" + next + "\" is not expected here.");
    }

    /**
     * 字句解析器が現在読み込んでいる行の文字列を返します。
     *
     * @return 現在の行
     */
    public String getLine() {
        return (line != null) ? line.substring(0, index) : "";
    }

    /**
     * 字句解析器が現在読み込んでいる行の行番号を返します。
     *
     * @return 1から始まる行番号
     */
    public int getLineNumber() {
        return reader.getLineNumber();
    }

    /**
     * 字句解析器が現在読み込んでいる行の桁番号を返します。
     *
     * @return 1から始まる桁番号
     */
    public int getColumnNumber() {
        return index + 1;
    }

}
