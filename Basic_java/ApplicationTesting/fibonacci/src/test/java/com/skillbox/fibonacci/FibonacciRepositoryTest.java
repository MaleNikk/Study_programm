package com.skillbox.fibonacci;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FibonacciRepositoryTest extends PostgresTestContainerInitializer {

    @Autowired
    private FibonacciRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    private final FibonacciNumber number;

    public FibonacciRepositoryTest(){
        this.number = new FibonacciNumber(7,13);
    }

    @Test
    @DisplayName("Test repository: check search in database.")
    public void testFindByIndex(){
        List<FibonacciNumber> numbers = getJdbcTemplate().query("SELECT * FROM fibonacci_number WHERE index = 7",
              (rs, rowNum) -> new FibonacciNumber(rs.getInt("index"), rs.getInt("volume")));
        Assertions.assertFalse(numbers.isEmpty());
        Assertions.assertEquals(13,numbers.get(0).getValue());
    }

    @Test
    @DisplayName("Test repository: check save to database.")
    public void testSave(){
        List<FibonacciNumber> numbers = new ArrayList<>();
        int index = 0;
        do {
            index++;
            numbers.add(new FibonacciNumber(index,new FibonacciCalculator().getFibonacciNumber(index)));
        } while (index <= 10);
        numbers.forEach(n->{
            getRepository().save(n);
            getEntityManager().flush();
            getEntityManager().detach(n);
        });
        numbers.clear();
        numbers = getJdbcTemplate().
                query("SELECT * FROM fibonacci_number WHERE index = 7",
                        (rs, rowNum) -> new FibonacciNumber(rs.getInt("index"),
                                rs.getInt("volume")));
        Assertions.assertFalse(numbers.isEmpty());
        Assertions.assertEquals(numbers.size(),10);
    }

    @Test
    @DisplayName("Test repository: check saving process.")
    public void testSavingProcess(){
        int countRecords = 0;
        do {
            createRecord();
            countRecords++;
        } while (countRecords<7);

        List<FibonacciNumber> numbers = getJdbcTemplate().
                query("SELECT * FROM fibonacci_number WHERE index = 7",
                        (rs, rowNum) -> new FibonacciNumber(rs.getInt("index"),
                                rs.getInt("volume")));
        Assertions.assertEquals(numbers.size(),1);

    }

    @BeforeEach
    public void createRecord(){
        getRepository().save(getNumber());
        getEntityManager().flush();
        getEntityManager().detach(getNumber());
    }

    public FibonacciRepository getRepository() {
        return repository;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public FibonacciNumber getNumber() {
        return number;
    }

}
