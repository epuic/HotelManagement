package model;

import util.Position;
import service.DataInserter;
import service.DatabaseManager;
import service.JsonParser;
import util.LocationInMeters;
import util.LocationInZec;

import java.io.IOException;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/resources/hotels.json"; // specify the path to the JSON file

        try {
            List<Hotel> hotels = JsonParser.parseHotels(filePath);
            DatabaseManager.createTables();
            DataInserter.insertData(hotels);
            System.out.println("Data has been successfully inserted into the database.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        double userLatitude = 46.77510521688025;
        double userLongitude = 23.640423390673615;

        System.out.print("Enter the distance threshold in km: ");
        double distanceThreshold = scanner.nextDouble();
        distanceThreshold = distanceThreshold * 1000;

        Position userPosition = new Position(userLatitude, userLongitude);

        List<Hotel> hotels = Hotel.loadHotelsFromDatabase();

        String anotherHotel = "yes";
        while (anotherHotel.equals("yes")) {
            System.out.println("\nHotels within " + distanceThreshold / 1000 + " km:");
            for (Hotel hotel : hotels) {
                Position hotelPosition = new Position(hotel.getLatitude(), hotel.getLongitude());
                double distance = userPosition.distanceTo(hotelPosition);

                if (distance <= distanceThreshold) {
                    System.out.println("Hotel ID: " + hotel.getId() + ", Name: " + hotel.getName() + ", Distance: " + String.format("%.1f", distance / 1000) + " km away.");
                }
            }

            System.out.print("\nEnter the ID of the hotel you want to see details for: ");
            int selectedHotelId = scanner.nextInt();
            System.out.println("");
            Hotel selectedHotel = Hotel.findHotelById(selectedHotelId);
            if (selectedHotel != null) {
                System.out.println("Hotel Name: " + selectedHotel.getName());
                System.out.println("   Rooms available:");
                for (Room room : selectedHotel.getRooms()) {
                    String type_room = null;
                    if (room.getType() == 1) {
                        type_room = "single room";
                    } else if (room.getType() == 2) {
                        type_room = "double room";
                    } else if (room.getType() == 3) {
                        type_room = "suite room";
                    } else if (room.getType() == 4) {
                        type_room = "matrimonial room";
                    }
                    if (room.isAvailable() == true) {
                        System.out.println("      Room Number: " + room.getRoomNumber() + ", Type: " + type_room + ", Price: " + String.format("%.0f", room.getPrice()) + "$");
                    }
                }

                System.out.print("\nDo you want to book any of these rooms? (yes/no): ");
                String bookRoom = scanner.next();
                while (bookRoom.equalsIgnoreCase("yes")) {
                    System.out.print("Enter the room number to book: ");
                    int roomNumber = scanner.nextInt();
                    Room.bookRoom(roomNumber);
                    System.out.print("Do you want to book another room? (yes/no): ");
                    bookRoom = scanner.next();
                }

                System.out.print("\nDo you want to cancel a booking or change a booked room? (cancel/change/no): ");
                String modifyBooking = scanner.next();
                while (!modifyBooking.equalsIgnoreCase("no")) {
                    if (modifyBooking.equalsIgnoreCase("cancel")) {
                        System.out.print("Enter the room number to cancel: ");
                        int roomNumber = scanner.nextInt();
                        Room.cancelRoom(roomNumber);
                    } else if (modifyBooking.equalsIgnoreCase("change")) {
                        System.out.print("Enter the room number to cancel: ");
                        int oldRoomNumber = scanner.nextInt();
                        Room.cancelRoom(oldRoomNumber);
                        System.out.print("Enter the new room number to book: ");
                        int newRoomNumber = scanner.nextInt();
                        Room.bookRoom(newRoomNumber);
                    }
                    System.out.print("Do you want to cancel or change another booking? (cancel/change/no): ");
                    modifyBooking = scanner.next();
                }
            } else {
                System.out.println("   No hotel found with the given ID.");
            }
            System.out.print("\nDo you want to see the details of another hotel? (yes/no): ");
            anotherHotel = scanner.next();
        }
        System.out.println("Thank you for everything!");
    }
}