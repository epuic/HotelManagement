package model;

import service.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private int roomNumber;
    private int type;
    private double price;
    private boolean isAvailable;

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public static Room findRoomByNumber(int roomNumber) {
        Room room = new Room();
        try {
            Connection dbConnection = DatabaseManager.connect();
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM rooms WHERE room_number = " + roomNumber);
            if (resultSet.next()) {
                room.setRoomNumber(resultSet.getInt("room_number"));
                room.setType(resultSet.getInt("type"));
                room.setPrice(resultSet.getDouble("price"));
                room.setAvailable(resultSet.getBoolean("is_available"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return room;
    }

    public static List<Room> loadRoomsForHotel(Connection dbConnection, int hotelId) {
        List<Room> rooms = new ArrayList<>();
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM rooms WHERE hotel_id = " + hotelId);

            while (resultSet.next()) {
                Room room = new Room();
                room.setRoomNumber(resultSet.getInt("room_number"));
                room.setType(resultSet.getInt("type"));
                room.setPrice(resultSet.getDouble("price"));
                room.setAvailable(resultSet.getBoolean("is_available"));
                rooms.add(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public static void bookRoom(int roomNumber) {
        updateRoomAvailability(roomNumber, false);
    }

    public static void cancelRoom(int roomNumber) {
        updateRoomAvailability(roomNumber, true);
    }

    private static void updateRoomAvailability(int roomNumber, boolean isAvailable) {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            dbConnection = DatabaseManager.connect();
            String updateSQL = "UPDATE rooms SET is_available = ? WHERE room_number = ?";
            preparedStatement = dbConnection.prepareStatement(updateSQL);
            preparedStatement.setBoolean(1, isAvailable);
            preparedStatement.setInt(2, roomNumber);
            preparedStatement.executeUpdate();
            String status = isAvailable ? "cancelled" : "booked";
            System.out.println("Room number " + roomNumber + " has been " + status + " successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (dbConnection != null) dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
