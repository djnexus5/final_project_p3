package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Refrigerator implements Storable {
    private static final int MAX_CAPACITY = 100;
    private int temperature;
    private Map<String, Grocery> Fridge = new HashMap<>();
    private Connection connection;

    public Refrigerator(int temperature) {
        this.temperature = temperature;
        this.connection = DBManager.getConnection();
        loadDataFromDatabase();
    }

    @Override
    public void storeGrocery(String name, Grocery grocery) {
        Fridge.put(name, grocery);
        saveDataToDatabase(name, grocery);
    }

    public Map<String, Grocery> getFridgeContent() {
        return Fridge;
    }

    public boolean removeExpiredGroceries() {
        Iterator<Map.Entry<String, Grocery>> iterator = Fridge.entrySet().iterator();
        boolean ok = false;
        while (iterator.hasNext()) {
            Map.Entry<String, Grocery> entry = iterator.next();
            Grocery grocery = entry.getValue();

            if (grocery instanceof Expirable && ((Expirable) grocery).isExpired()) {
                ok = true;
                System.out.println("Removing expired grocery: " + entry.getKey());
                iterator.remove();
                removeExpiredGroceryFromDatabase(entry.getKey());
            }
        }
        if (ok) {
            System.out.println("Removing done.");
        } else {
            System.out.println("There were no expired groceries to remove");
        }
        return ok;
    }

    private void loadDataFromDatabase() {
        try (PreparedStatement statement = connection.prepareStatement("SELECT name, type, space_required, expiry_date, category, volume_left FROM groceries");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                int spaceRequired = resultSet.getInt("space_required");
                String expiryDate = resultSet.getString("expiry_date");

                Grocery grocery;
                if ("Food".equals(type)) {
                    String category = resultSet.getString("category");
                    grocery = new Food(name, spaceRequired, expiryDate, category);
                } else if ("Beverage".equals(type)) {
                    double volumeLeft = resultSet.getDouble("volume_left");
                    grocery = new Beverage(name, spaceRequired, expiryDate, volumeLeft);
                } else {
                    grocery = new Grocery(name, spaceRequired, expiryDate);
                }

                Fridge.put(name, grocery);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveDataToDatabase(String name, Grocery grocery) {
        try {
            String type;
            if (grocery instanceof Food) {
                type = "Food";
            } else if (grocery instanceof Beverage) {
                type = "Beverage";
            } else {
                type = "Other";
            }

            String sql = "INSERT INTO groceries (name, type, space_required, expiry_date, category, volume_left) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setString(2, type);
                statement.setInt(3, grocery.getSpaceRequired());
                statement.setString(4, grocery.getExpiryDate().toString());

                if (grocery instanceof Food) {
                    statement.setString(5, ((Food) grocery).getCategory());
                    statement.setDouble(6, 0);
                } else if (grocery instanceof Beverage) {
                    statement.setNull(5, java.sql.Types.VARCHAR);
                    statement.setDouble(6, ((Beverage) grocery).getVolumeLeft());
                } else {
                    statement.setNull(5, java.sql.Types.VARCHAR);
                    statement.setDouble(6, 0);
                }

                statement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeExpiredGroceryFromDatabase(String name) {
        try {
            String sql = "DELETE FROM groceries WHERE name = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTemperature() {
        return this.temperature;
    }
}
