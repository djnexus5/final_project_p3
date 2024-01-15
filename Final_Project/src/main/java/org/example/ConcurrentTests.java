package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentTests {

    @BeforeEach
    public void clearDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/final_project", "admin", "admin");
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("DELETE FROM groceries");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void runAllTestsConcurrently() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        try {
            executorService.submit(() -> Tests.testRefrigeratorAndGroceries());
            executorService.submit(() -> Tests.testFoodConstructor());
            executorService.submit(() -> Tests.testAddFoodToRefrigerator());
            executorService.submit(() -> Tests.testBeverageConstructor());
            executorService.submit(() -> Tests.testAddBeverageToRefrigerator());
            executorService.submit(() -> Tests.testRemoveExpiredGroceries());
        } finally {
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
        }
    }
}
