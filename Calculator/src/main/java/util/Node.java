package main.java.util;

import java.math.BigDecimal;

/**
 * 構文木のノードの実装です。
 */
public class Node {
    private Operators op;
    private Node left, right;
    private BigDecimal value;

    /**
     * 演算子を指定してノードを構築します。
     *
     * @param op 演算子
     * @param left 左の子
     * @param right 右の子
     */
    public Node(Enum<?> op, Node left, Node right) {
        this.op = (Operators) op;
        this.left = left;
        this.right = right;
    }

    /**
     * 値を指定してノードを構築します。
     *
     * @param value ノードの持つ値
     */
    public Node(BigDecimal value) {
        this.op = null;
        this.value = value;
    }

    /**
     * 値を再帰的に計算して返します。
     *
     * @return このノードの表す値
     */
    public BigDecimal value() {
        if (op == null)
            return value;
        BigDecimal l = left.value();
        BigDecimal r = (right != null) ? right.value() : null;
        switch (op) {
        case ADD:
            return l.add(r);
        case SUB:
            return r != null ? l.subtract(r) : l.negate();
        case MUL:
            return l.multiply(r);
        case DIV:
            return l.divide
                    (r, 32, BigDecimal.ROUND_HALF_EVEN);
        case MOD:
            return l.remainder(r);
        case POW:
            return l.pow(r.intValue());
        default:
            // 呼び出しなし
        }
        return BigDecimal.ZERO;
    }

    /**
     * 後置記法の文字列表現を返します。
     *
     * @return このノードの表現
     */
    public String toString() {
        if (op == null)
            return String.valueOf(value);
        StringBuilder sb = new StringBuilder();
        sb.append(left);
        sb.append(" ");
        sb.append(right);
        sb.append(" ");
        sb.append(op);
        return new String(sb);
    }
}
