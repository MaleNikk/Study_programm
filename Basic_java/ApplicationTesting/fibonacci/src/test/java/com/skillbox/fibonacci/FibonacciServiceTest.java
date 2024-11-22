package com.skillbox.fibonacci;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FibonacciServiceTest extends PostgresTestContainerInitializer{

    private final FibonacciRepository repository;
    private final FibonacciCalculator calculator;
    private final FibonacciNumber number;
    private final FibonacciService service;
    private final Integer checkIndex;


    public FibonacciServiceTest() {
        this.repository = Mockito.mock(FibonacciRepository.class);
        this.calculator = Mockito.mock(FibonacciCalculator.class);
        this.number = new FibonacciNumber(7,13);
        this.service = new FibonacciService(getRepository(),getCalculator());
        this.checkIndex = 7;
    }

    @Test
    @DisplayName("Test: use calculator, if number is not present in data base. ")
    @DirtiesContext
    public void test1FibonacciNumber(){
        Assertions.assertFalse(getRepository().findByIndex(getCheckIndex()).isPresent());
        Mockito.when(getService().fibonacciNumber(getCheckIndex())).thenReturn(getNumber());
    }

    @Test
    @DisplayName("Test: get number from data base, but unused calculator.")
    @DirtiesContext
    public void test2FibonacciNumber(){
        getRepository().save(getNumber());
        Assertions.assertTrue(getRepository().findByIndex(getCheckIndex()).isPresent());
        Mockito.when(getService().fibonacciNumber(getCheckIndex())).
                thenReturn(getRepository().findByIndex(getCheckIndex()).get());
    }


    @Test
    @DisplayName("Test: checking for Exception in code. ")
    @DirtiesContext
    public void test3FibonacciNumber(){
        Assertions.assertThrows(IllegalArgumentException.class,()->getService().fibonacciNumber(-getCheckIndex()),
               "Index should be greater or equal to 1");
    }

    @BeforeAll
    public static void startDB(){
        postgres.start();
    }

    @AfterAll
    public static void closeDB(){
        postgres.close();
    }
    private FibonacciRepository getRepository() {
        return repository;
    }

    private FibonacciCalculator getCalculator() {
        return calculator;
    }

    private FibonacciService getService() {
        return service;
    }

    public FibonacciNumber getNumber() {
        return number;
    }

    public int getCheckIndex() {
        return checkIndex;
    }
}
