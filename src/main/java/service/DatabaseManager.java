package service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE = "HotelReservation";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL + DATABASE, USER, PASSWORD);
    }

    public static void createDatabase() {
        String createDatabase = "CREATE DATABASE IF NOT EXISTS " + DATABASE;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTables() {
        createDatabase();

        String createHotelsTable = "CREATE TABLE IF NOT EXISTS hotels (" +
                "id INT PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "latitude DOUBLE, " +
                "longitude DOUBLE)";

        String createRoomsTable = "CREATE TABLE IF NOT EXISTS rooms (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "hotel_id INT, " +
                "room_number INT, " +
                "type INT, " +
                "price DOUBLE, " +
                "is_available BOOLEAN, " +
                "FOREIGN KEY (hotel_id) REFERENCES hotels(id))";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createHotelsTable);
            stmt.execute(createRoomsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
