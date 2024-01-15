package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Tests {

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
    public static void testRefrigeratorAndGroceries() {
        Refrigerator refrigerator = new Refrigerator(5);
        assertNotNull(refrigerator);
        assertEquals(5, refrigerator.getTemperature());

        Food food = new Food("Apple", 2, "2024-01-20", "Fruit");
        refrigerator.storeGrocery("Apple", food);

        Map<String, Grocery> fridgeContent = refrigerator.getFridgeContent();
        assertEquals(1, fridgeContent.size());

        boolean removedExpiredGroceries = refrigerator.removeExpiredGroceries();
        assertFalse(removedExpiredGroceries);

        fridgeContent = refrigerator.getFridgeContent();
        assertEquals(1, fridgeContent.size());
    }

    @Test
    public static void testFoodConstructor() {
        Food food = new Food("Banana", 3, "2024-01-25", "Fruit");
        assertNotNull(food);
        assertEquals("Banana", food.getName());
        assertEquals(3, food.getSpaceRequired());
        assertEquals("2024-01-25", food.getExpiryDate().toString());
        assertEquals("Fruit", food.getCategory());
    }

    @Test
    public static void testAddFoodToRefrigerator() {
        Refrigerator refrigerator = new Refrigerator(5);
        Food food = new Food("Carrot", 2, "2024-02-01", "Vegetable");
        refrigerator.storeGrocery("Carrot", food);

        Map<String, Grocery> fridgeContent = refrigerator.getFridgeContent();
        assertEquals(1, fridgeContent.size());
    }

    @Test
    public static void testBeverageConstructor() {
        Beverage beverage = new Beverage("Soda", 1, "2024-01-25", 75.0);
        assertNotNull(beverage);
        assertEquals("Soda", beverage.getName());
        assertEquals(1, beverage.getSpaceRequired());
        assertEquals("2024-01-25", beverage.getExpiryDate().toString());
        assertEquals(75.0, beverage.getVolumeLeft());
    }

    @Test
    public static void testAddBeverageToRefrigerator() {
        Refrigerator refrigerator = new Refrigerator(5);
        Beverage beverage = new Beverage("Water", 1, "2024-02-01", 100.0);
        refrigerator.storeGrocery("Water", beverage);

        Map<String, Grocery> fridgeContent = refrigerator.getFridgeContent();
        assertEquals(1, fridgeContent.size());
    }

    @Test
    public static void testRemoveExpiredGroceries() {
        Refrigerator refrigerator = new Refrigerator(5);
        Beverage expiredBeverage = new Beverage("Expired Soda", 2, "2022-01-01", 50.0);
        refrigerator.storeGrocery("Expired Soda", expiredBeverage);

        refrigerator.removeExpiredGroceries();

        Map<String, Grocery> fridgeContent = refrigerator.getFridgeContent();
        assertEquals(0, fridgeContent.size());
    }
}
