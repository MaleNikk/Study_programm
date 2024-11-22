package com.skillbox.fibonacci;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FibonacciCalculatorTest {

    private final FibonacciCalculator fibonacciCalculator;

    public FibonacciCalculatorTest() {
        this.fibonacciCalculator = new FibonacciCalculator();
    }

    @Test
    @DisplayName("Test Fibonacci calculator with index \"1\".")
    public void testFibonacciNumber1() {
        assertEquals(1,
                getFibonacciCalculator().getFibonacciNumber(1));
    }

    @Test
    @DisplayName("Test Fibonacci calculator with index \"2\".")
    public void testFibonacciNumber2() {
        assertEquals(1,
                getFibonacciCalculator().getFibonacciNumber(2));
    }

    @Test
    @DisplayName("Test Fibonacci calculator with index \"7\".")
    public void testFibonacciNumber5() {
        assertEquals(13,
                getFibonacciCalculator().getFibonacciNumber(7));
    }

    @Test
    @DisplayName("Test Fibonacci calculator with index \"-1\" .")
    public void testFibonacciNumberMinus1() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> getFibonacciCalculator().getFibonacciNumber(-1),
                "Index should be greater or equal to 1");
    }

    @Test
    @DisplayName("Test Fibonacci calculator with index \"0\".")
    public void testFibonacciNumberZero() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> getFibonacciCalculator().getFibonacciNumber(0),
                "Index should be greater or equal to 1");
    }

    @Test
    @DisplayName("Test Fibonacci calculator with index \"47\".")
    public void testFibonacciNumberRevert() {
        Assertions.assertTrue(getFibonacciCalculator().getFibonacciNumber(47) < 0);
    }


    public FibonacciCalculator getFibonacciCalculator() {
        return fibonacciCalculator;
    }
}
