package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Main {

        public static void main(String[] args) {
                try (Connection connection = DBManager.getConnection();
                     Scanner scanner = new Scanner(System.in)){

                        Refrigerator refrigerator = new Refrigerator(10);

                        while (true) {
                                System.out.println("Choose an option:");
                                System.out.println("1. View fridge");
                                System.out.println("2. Add a grocery");
                                System.out.println("3. Eat/Drink/Remove a grocery");
                                System.out.println("0. Exit the program");

                                int choice = validateIntInput(scanner);
                                scanner.nextLine();

                                switch (choice) {
                                        case 1:
                                                viewFridge(refrigerator);
                                                break;
                                        case 2:
                                                System.out.println("Choose an option:");
                                                System.out.println("1. Add some food");
                                                System.out.println("2. Add a beverage");
                                                choice = validateIntInput(scanner);
                                                scanner.nextLine();
                                                switch (choice) {
                                                        case 1:
                                                                addFood(refrigerator, scanner);
                                                                break;
                                                        case 2:
                                                                addBeverage(refrigerator, scanner);
                                                                break;
                                                }
                                                break;
                                        case 3:
                                                System.out.println("Choose an option:");
                                                System.out.println("1. Eat some food");
                                                System.out.println("2. Drink a beverage");
                                                System.out.println("3. Remove a grocery");
                                                System.out.println("4. Remove the expired groceries");
                                                System.out.println("0. Exit the program");
                                                choice = validateIntInput(scanner);
                                                scanner.nextLine();
                                                switch (choice) {
                                                        case 1:
                                                                System.out.println("Type the name of the food to eat:");
                                                                String foodEaten = validateStringInput(scanner);
                                                                eatFood(refrigerator, foodEaten);
                                                                break;
                                                        case 2:
                                                                System.out.println("Type the name of the beverage to drink:");
                                                                String beverageDrank = validateStringInput(scanner);
                                                                drinkBeverage(refrigerator, beverageDrank);
                                                                break;
                                                        case 3:
                                                                System.out.println("Type the name of the grocery to remove:");
                                                                String groceryRemoved = validateStringInput(scanner);
                                                                removeGrocery(refrigerator, groceryRemoved);
                                                                break;
                                                        case 4:
                                                                refrigerator.removeExpiredGroceries();
                                                                break;
                                                        case 0:
                                                                System.out.println("Exiting program. Goodbye!");
                                                                System.exit(0);
                                                                break;
                                                        default:
                                                                System.out.println("Invalid choice. Please try again.");
                                                }
                                                break;
                                        case 0:
                                                System.out.println("Exiting program. Goodbye!");
                                                System.exit(0);
                                                break;
                                        default:
                                                System.out.println("Invalid choice. Please try again.");

                                }
                        }
                } catch (Exception e) {
                        System.err.println("An unexpected error occurred: " + e.getMessage());
                }

        }

        private static void viewFridge(Refrigerator refrigerator) {
                Map<String, Grocery> fridgeContent = refrigerator.getFridgeContent();

                try {
                        Connection connection = DBManager.getConnection();
                        String sql = "SELECT * FROM groceries";

                        try (PreparedStatement statement = connection.prepareStatement(sql);
                             ResultSet resultSet = statement.executeQuery()) {

                                while (resultSet.next()) {
                                        String name = resultSet.getString("name");
                                        int spaceRequired = resultSet.getInt("space_required");
                                        LocalDate expiryDate = resultSet.getDate("expiry_date").toLocalDate();
                                        String type = resultSet.getString("type");

                                        System.out.println("Name: " + name);
                                        System.out.println("Space Required: " + spaceRequired + "L");
                                        System.out.println("Expiry Date: " + expiryDate);

                                        if ("Food".equals(type)) {
                                                String category = resultSet.getString("category");
                                                System.out.println("Category: " + category);
                                        } else if ("Beverage".equals(type)) {
                                                double volumeLeft = resultSet.getDouble("volume_left");
                                                System.out.println("Volume Left: " + volumeLeft);
                                        }

                                        System.out.println("--------");
                                }
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }


        private static void addFood(Refrigerator refrigerator, Scanner scanner) {
                try {
                        System.out.println("What's the name?");
                        String name = validateStringInput(scanner);
                        System.out.println("What's the space required?");
                        int spaceRequired = validateIntInput(scanner);
                        scanner.nextLine();
                        System.out.println("What's the expiry date? (please YYYY-MM-DD)");
                        LocalDate expiryDate = validateDateInput(scanner);
                        System.out.println("What's its category?");
                        String category = validateStringInput(scanner);

                        String sql = "INSERT INTO groceries (name, type, space_required, expiry_date, category, volume_left) VALUES (?, ?, ?, ?, ?, ?)";

                        try (Connection connection = DBManager.getConnection();
                             PreparedStatement statement = connection.prepareStatement(sql)) {

                                statement.setString(1, name);
                                statement.setString(2, "Food");
                                statement.setInt(3, spaceRequired);
                                statement.setDate(4, java.sql.Date.valueOf(expiryDate));
                                statement.setString(5, category);
                                statement.setDouble(6, 0);  // Not applicable for Food, set to 0

                                statement.executeUpdate();

                                System.out.println("Food added successfully.");

                        } catch (SQLException e) {
                                e.printStackTrace();
                        }

                } catch (DateTimeParseException e) {
                        System.out.println("Please enter the expiry date in the format YYYY-MM-DD.");
                }
        }

        private static void addBeverage(Refrigerator refrigerator, Scanner scanner) {
                try {
                        System.out.println("What's the name?");
                        String name = validateStringInput(scanner);
                        System.out.println("What's the space required?");
                        int spaceRequired = validateIntInput(scanner);
                        scanner.nextLine();
                        System.out.println("What's the expiry date? (please YYYY-MM-DD)");
                        LocalDate expiryDate = validateDateInput(scanner);
                        System.out.println("How much volume is left? (from 0 to 100%)");
                        double volumeLeft = validateDoubleInput(scanner);
                        scanner.nextLine();

                        String sql = "INSERT INTO groceries (name, type, space_required, expiry_date, category, volume_left) VALUES (?, ?, ?, ?, ?, ?)";

                        try (Connection connection = DBManager.getConnection();
                             PreparedStatement statement = connection.prepareStatement(sql)) {

                                statement.setString(1, name);
                                statement.setString(2, "Beverage");
                                statement.setInt(3, spaceRequired);
                                statement.setDate(4, java.sql.Date.valueOf(expiryDate));
                                statement.setNull(5, java.sql.Types.VARCHAR);  // Not applicable for Beverage, set to NULL
                                statement.setDouble(6, volumeLeft);

                                statement.executeUpdate();

                                System.out.println("Beverage added successfully.");

                        } catch (SQLException e) {
                                e.printStackTrace();
                        }

                } catch (DateTimeParseException e) {
                        System.out.println("Please enter the expiry date in the format YYYY-MM-DD.");
                }
        }

        private static void removeGrocery(Refrigerator refrigerator, String groceryRemoved) {
                try {
                        Connection connection = DBManager.getConnection();

                        String sql = "DELETE FROM groceries WHERE name = ?";

                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                                statement.setString(1, groceryRemoved);

                                int rowsAffected = statement.executeUpdate();

                                if (rowsAffected > 0) {
                                        System.out.println("Grocery removed successfully.");
                                } else {
                                        System.out.println("Grocery not found.");
                                }
                        }

                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }

        private static void eatFood(Refrigerator refrigerator, String foodEaten) {
                removeGrocery(refrigerator, foodEaten);
        }

        private static void drinkBeverage(Refrigerator refrigerator, String beverageDrank) {
                removeGrocery(refrigerator, beverageDrank);
        }

        private static int validateIntInput(Scanner scanner) {
                while (true) {
                        try {
                                System.out.print("Enter an integer: ");
                                return Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException ex) {
                                System.out.println("Invalid input. Please enter a valid integer.");
                        }
                }
        }

        private static String validateStringInput(Scanner scanner) {
                while (true) {
                        System.out.print("Enter a string: ");
                        String input = scanner.nextLine().trim();
                        if (!input.isEmpty()) {
                                return input;
                        } else {
                                System.out.println("Invalid input. Please enter a non-empty string.");
                        }
                }
        }

        private static LocalDate validateDateInput(Scanner scanner) {
                while (true) {
                        System.out.print("Enter a date (YYYY-MM-DD): ");
                        try {
                                return LocalDate.parse(scanner.nextLine());
                        } catch (DateTimeParseException ex) {
                                System.out.println("Invalid date format. Please enter a date in YYYY-MM-DD format.");
                        }
                }
        }

        private static double validateDoubleInput(Scanner scanner) {
                while (true) {
                        try {
                                System.out.print("Enter a double (from 0 to 100%): ");
                                double input = Double.parseDouble(scanner.nextLine());
                                if (input >= 0 && input <= 100) {
                                        return input;
                                } else {
                                        System.out.println("Invalid input. Please enter a double between 0 and 100.");
                                }
                        } catch (NumberFormatException ex) {
                                System.out.println("Invalid input. Please enter a valid double.");
                        }
                }
        }
}
