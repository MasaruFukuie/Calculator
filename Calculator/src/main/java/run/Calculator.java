package main.java.run;

import java.io.StringReader;
import java.math.BigDecimal;

import main.java.util.Node;
import main.java.util.Parser;

public class Calculator {

    public String calc(String formula) {
        String result = null;
        try {
            Parser parser = new Parser();
            StringReader reader = new StringReader(formula);
            Node node = parser.parse(reader);
            BigDecimal val = node.value();
            if (val == null) {
                return null;
            }
            result = val.stripTrailingZeros().toPlainString();
        } catch (Exception ex) {
            return null;
        }
        return result;
    }
}
