/**
 * 
 */
package test.java;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Properties;

import main.java.run.Calculator;

import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {
    
    private final String safix = "Ans";
    
    private Properties conf;
    
    private Calculator calculator;

    @Before
    public void before() throws IOException {
        conf = new Properties();
        conf.load(this.getClass().getResourceAsStream("/test/resources/test.properties"));
        calculator = new Calculator();
    }

    @Test
    public void testAdd() {
        String method = "testAdd";
        String formula = this.__getValue(method);
        String ans = this.__getValue(method + safix);
        
        String result = calculator.calc(formula);
        
        assertEquals(ans, result);
    }

    @Test
    public void testSub() {
        String method = "testSub";
        String formula = this.__getValue(method);
        String ans = this.__getValue(method + safix);
        
        String result = calculator.calc(formula);
        
        assertEquals(ans, result);
    }

    @Test
    public void testMul() {
        String method = "testMul";
        String formula = this.__getValue(method);
        String ans = this.__getValue(method + safix);
        
        String result = calculator.calc(formula);
        
        assertEquals(ans, result);
    }
    
    @Test
    public void testDiv() {
        String method = "testDiv";
        String formula = this.__getValue(method);
        String ans = this.__getValue(method + safix);
        
        String result = calculator.calc(formula);
        
        assertEquals(ans, result);
    }
    
    @Test
    public void testMod() {
        String method = "testMod";
        String formula = this.__getValue(method);
        String ans = this.__getValue(method + safix);
        
        String result = calculator.calc(formula);
        
        assertEquals(ans, result);
    }
    
    @Test
    public void testPow() {
        String method = "testPow";
        String formula = this.__getValue(method);
        String ans = this.__getValue(method + safix);
        
        String result = calculator.calc(formula);
        
        assertEquals(ans, result);
    }
    
    @Test
    public void testComplex() {
        String method = "testComplex";
        String formula = this.__getValue(method);
        String ans = this.__getValue(method + safix);
        
        String result = calculator.calc(formula);
        
        assertEquals(ans, result);
    }
    
    private String __getValue(String key) {
        return conf != null ? conf.getProperty(key) : null;
    }
}
