import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by YWJ on 2017.4.20.
 * Copyright (c) 2017 NJU PASA Lab All rights reserved.
 */
public class calculatorTestSuite {
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new JUnit4TestAdapter(CalculatorTest.class));
        return suite;
    }
}
