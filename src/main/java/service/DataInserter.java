package service;

import model.Room;
import model.Hotel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DataInserter {
    public static void insertData(List<Hotel> hotels) {
        String insertHotel = "INSERT INTO hotels (id, name, latitude, longitude) VALUES (?, ?, ?, ?)";
        String insertRoom = "INSERT INTO rooms (hotel_id, room_number, type, price, is_available) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement hotelStmt = conn.prepareStatement(insertHotel);
             PreparedStatement roomStmt = conn.prepareStatement(insertRoom)) {

            for (Hotel hotel : hotels) {
                hotelStmt.setInt(1, hotel.getId());
                hotelStmt.setString(2, hotel.getName());
                hotelStmt.setDouble(3, hotel.getLatitude());
                hotelStmt.setDouble(4, hotel.getLongitude());
                hotelStmt.executeUpdate();

                for (Room room : hotel.getRooms()) {
                    roomStmt.setInt(1, hotel.getId());
                    roomStmt.setInt(2, room.getRoomNumber());
                    roomStmt.setInt(3, room.getType());
                    roomStmt.setDouble(4, room.getPrice());
                    roomStmt.setBoolean(5, room.isAvailable());
                    roomStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
