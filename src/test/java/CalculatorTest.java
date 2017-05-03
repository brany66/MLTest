import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by YWJ on 2017.4.20.
 * Copyright (c) 2017 NJU PASA Lab All rights reserved.
 */
public class CalculatorTest {
    Calculator calc;

    @Before
    public void init() {
        System.out.println("init......");
    }

    @Before
    public void init2() {
        calc = new Calculator();
    }

    @Test
    public void basicAddition() {
        assertEquals(Double.valueOf(2), calc.add(1.0, 1.0));
    }

    @Test
    public void additionNegativeNumber() {
        assertEquals(Double.valueOf(0), calc.add(1, -1));
    }


    @Test
    public void testSubtract() {
        assertEquals(Double.valueOf(3), calc.subtract(5.0, 2.0));
    }

    @After
    public void clear() {
        calc = null;
        System.out.println("clean down");
    }
}
