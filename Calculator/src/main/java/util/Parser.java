package main.java.util;

import java.io.Reader;

import javax.script.ScriptException;

/**
* 数式を計算する構文解析器
*
*/
public class Parser {
    private final LexicalAnalyzer analyzer;

    /**
     * パーサーを生成します。
     */
    public Parser() {
        analyzer = new LexicalAnalyzer();
    }

    /**
     * 数式を解析します。
     *
     * @param src 数式のソース
     * @return 構築した構文木
     * @throws ScriptException 読み込みに失敗したか構文エラーの場合
     */
    public Node parse(Reader src) throws ScriptException {
        analyzer.load(src);
        return parseExpression();
    }

    /**
     * 式を解析します。
     *
     * @return 構築した構文木
     * @throws ScriptException
     */
    private Node parseExpression() throws ScriptException {
        return parseAdditive();
    }

    /**
     * 加算演算子または減算演算子を主演算子とする式を解析します。
     */
    private Node parseAdditive() throws ScriptException {
        Node left = parseMultiplicative();
        if (left == null)
            return null;
        Token token;
        while ((token = analyzer.getNextToken()) != null) {
            if (token.getType() != Operators.ADD
                    && token.getType() != Operators.SUB) {
                analyzer.ungetToken(token);
                return left;
            }
            Node right = parseMultiplicative();
            left = new Node(token.getType(), left, right);
        }
        return left;
    }

    /**
     * 乗算演算子または除算演算子または剰余演算子を主演算子とする式を解析します。
     */
    private Node parseMultiplicative() throws ScriptException {
        Node left = parsePower();
        if (left == null)
            return null;
        Token token;
        while ((token = analyzer.getNextToken()) != null) {
            if (token.getType() != Operators.MUL
                    && token.getType() != Operators.DIV
                    && token.getType() != Operators.MOD) {
                analyzer.ungetToken(token);
                return left;
            }
            Node right = parsePower();
            left = new Node(token.getType(), left, right);
        }
        return left;
    }

    /**
     * 累乗演算子を主演算子とする式を解析します。
     */
    private Node parsePower() throws ScriptException {
        Node left = parseUnary();
        if (left == null)
            return null;
        Token token;
        while ((token = analyzer.getNextToken()) != null) {
            if (token.getType() != Operators.POW) {
                analyzer.ungetToken(token);
                return left;
            }
            Node right = parsePower();
            left = new Node(token.getType(), left, right);
        }
        return left;
    }

    /**
     * 単項演算子式を解析します。
     */
    private Node parseUnary() throws ScriptException {
        Token token = analyzer.getNextToken();
        if (token == null)
            return null;
        if (token.getType() == Operators.ADD) {
            return parsePrimary();
        } else if (token.getType() == Operators.SUB) {
            Node node = parsePrimary();
            return new Node(Operators.SUB, node, null);
        } else {
            analyzer.ungetToken(token);
            return parsePrimary();
        }
    }

    /**
     * 基本式の構文を解析します。
     */
    private Node parsePrimary() throws ScriptException {
        Token token = analyzer.getNextToken();
        if (token == null)
            return null;
        if (token.getType() == Tokens.NUMBER) {
            return new Node(token.toNumber());
        } else if (token.getType() == Operators.OPEN_PARENS) {
            Node node = parseExpression();
            analyzer.checkNextToken(Operators.CLOSE_PARENS);
            return node;
        }
        return null;
    }
}
