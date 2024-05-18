package model;

import service.DatabaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private List<Room> rooms;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    // Method to find a hotel by ID
    public static Hotel findHotelById(int hotelId) {
        Hotel hotel = null;
        List<Room> rooms = new ArrayList<>();
        Connection dbConnection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSet roomResultSet = null;

        try {
            dbConnection = DatabaseManager.connect();
            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM hotels WHERE id = " + hotelId);
            if (resultSet.next()) {
                hotel = new Hotel();
                hotel.setId(resultSet.getInt("id"));
                hotel.setName(resultSet.getString("name"));
                hotel.setLatitude(resultSet.getDouble("latitude"));
                hotel.setLongitude(resultSet.getDouble("longitude"));

                roomResultSet = statement.executeQuery("SELECT * FROM rooms WHERE hotel_id = " + hotelId);
                while (roomResultSet.next()) {
                    Room room = new Room();
                    room.setRoomNumber(roomResultSet.getInt("room_number"));
                    room.setType(roomResultSet.getInt("type"));
                    room.setPrice(roomResultSet.getDouble("price"));
                    room.setAvailable(roomResultSet.getBoolean("is_available"));
                    rooms.add(room);
                }
                hotel.setRooms(rooms);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (roomResultSet != null) roomResultSet.close();
                if (statement != null) statement.close();
                if (dbConnection != null) dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return hotel;
    }

    public static List<Hotel> loadHotelsFromDatabase() {
        List<Hotel> hotels = new ArrayList<>();
        try {
            Connection dbConnection = DatabaseManager.connect();
            Statement statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM hotels");

            while (resultSet.next()) {
                Hotel hotel = new Hotel();
                hotel.setId(resultSet.getInt("id"));
                hotel.setName(resultSet.getString("name"));
                hotel.setLatitude(resultSet.getDouble("latitude"));
                hotel.setLongitude(resultSet.getDouble("longitude"));
                hotel.setRooms(Room.loadRoomsForHotel(dbConnection, hotel.getId()));

                hotels.add(hotel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotels;
    }
}
