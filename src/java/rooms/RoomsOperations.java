/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rooms;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import crimeRate.CrimeData;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

/**
 *
 * @author Tino
 */
public class RoomsOperations {

    public static JsonObject searchRoomsByCity(Rooms rooms, String userInputCity) {
        // Create a new JSON object to store the results
        JsonObject result = new JsonObject();
        JsonArray roomsArray = new JsonArray();

        // Iterate through rooms and add details to the JSON array if the city matches the user's input
        boolean cityFound = false;
        for (Rooms.Room room : rooms.getRooms()) {
            String city = (room.getLocation().getCity()).toLowerCase();
            if (city.equalsIgnoreCase(userInputCity) || city.contains(userInputCity)) {
                cityFound = true;
                JsonObject roomDetails = new JsonObject();
                roomDetails.addProperty("id", room.getID());
                roomDetails.addProperty("name", room.getName());

                JsonObject location = new JsonObject();
                location.addProperty("city", room.getLocation().getCity());
                location.addProperty("county", room.getLocation().getCounty());
                location.addProperty("postcode", room.getLocation().getPostcode());
                roomDetails.add("location", location);

                JsonObject details = new JsonObject();
                details.addProperty("furnished", room.getDetails().isFurnished());
                JsonArray amenities = new JsonArray();
                for (String amenity : room.getDetails().getAmenities()) {
                    amenities.add(amenity);
                }
                details.add("amenities", amenities);
                details.addProperty("live_in_landlord", room.getDetails().isLiveInLandlord());
                details.addProperty("shared_with", room.getDetails().getSharedWith());
                details.addProperty("bills_included", room.getDetails().isBillsIncluded());
                details.addProperty("bathroom_shared", room.getDetails().isBathroomShared());
                roomDetails.add("details", details);

                roomDetails.addProperty("price_per_month_gbp", room.getPricePerMonthGbp());

                String availabilityDate = room.getAvailabilityDate() != null
                        ? room.getAvailabilityDate().toString()
                        : "N/A";
                roomDetails.addProperty("availability_date", availabilityDate);

                JsonArray spokenLanguages = new JsonArray();
                for (String language : room.getSpokenLanguages()) {
                    spokenLanguages.add(language);
                }
                roomDetails.add("spoken_languages", spokenLanguages);

                roomsArray.add(roomDetails);
            }
        }

        // Add the rooms array to the result JSON object
        result.add("rooms", roomsArray);

        // If no rooms were found for the city, add a message to the result
        if (!cityFound) {
            result.addProperty("message", "No rooms found in " + userInputCity + ".");
        }

        return result;
    }

    public static JsonObject displayAllRooms(Rooms rooms) {
        // Create a new JSON object to store the results
        JsonObject result = new JsonObject();
        JsonArray roomsArray = new JsonArray();

        // Iterate through rooms and add details to the JSON array if the city matches the user's input
        boolean cityFound = false;
        for (Rooms.Room room : rooms.getRooms()) {
            JsonObject roomDetails = new JsonObject();
            roomDetails.addProperty("id", room.getID());
            roomDetails.addProperty("name", room.getName());

            JsonObject location = new JsonObject();
            location.addProperty("city", room.getLocation().getCity());
            location.addProperty("county", room.getLocation().getCounty());
            location.addProperty("postcode", room.getLocation().getPostcode());
            roomDetails.add("location", location);

            JsonObject details = new JsonObject();
            details.addProperty("furnished", room.getDetails().isFurnished());
            JsonArray amenities = new JsonArray();
            for (String amenity : room.getDetails().getAmenities()) {
                amenities.add(amenity);
            }
            details.add("amenities", amenities);
            details.addProperty("live_in_landlord", room.getDetails().isLiveInLandlord());
            details.addProperty("shared_with", room.getDetails().getSharedWith());
            details.addProperty("bills_included", room.getDetails().isBillsIncluded());
            details.addProperty("bathroom_shared", room.getDetails().isBathroomShared());
            roomDetails.add("details", details);

            roomDetails.addProperty("price_per_month_gbp", room.getPricePerMonthGbp());

            String availabilityDate = room.getAvailabilityDate() != null
                    ? room.getAvailabilityDate().toString()
                    : "N/A";
            roomDetails.addProperty("availability_date", availabilityDate);

            JsonArray spokenLanguages = new JsonArray();
            for (String language : room.getSpokenLanguages()) {
                spokenLanguages.add(language);
            }
            roomDetails.add("spoken_languages", spokenLanguages);

            roomsArray.add(roomDetails);
        }

        // Add the rooms array to the result JSON object
        result.add("rooms", roomsArray);

        // If no rooms were found for the city, add a message to the result
        return result;
    }

    public static JsonObject getRoomsById(int roomId) {
        // Path to the JSON file
        String JSON_FILE = "/Users/Tino/Desktop/Service-Centric & Cloud Comp/NetBeansProjects/GlobalDormApp/WEB-INF/data/rooms.json";
        Gson gson = new Gson();
        JsonObject result = new JsonObject();
        JsonArray roomsArray = new JsonArray();

        try (FileReader reader = new FileReader(JSON_FILE)) {
            Type roomsType = new TypeToken<Rooms>() {
            }.getType();
            Rooms rooms = gson.fromJson(reader, roomsType);

            boolean IDFound = false;
            for (Rooms.Room room : rooms.getRooms()) {
                long ID = room.getID();
                if (roomId == ID) {
                    IDFound = true;
                    JsonObject roomDetails = new JsonObject();
                    roomDetails.addProperty("id", room.getID());
                    roomDetails.addProperty("name", room.getName());

                    JsonObject location = new JsonObject();
                    location.addProperty("city", room.getLocation().getCity());
                    location.addProperty("county", room.getLocation().getCounty());
                    location.addProperty("postcode", room.getLocation().getPostcode());
                    roomDetails.add("location", location);

                    JsonObject details = new JsonObject();
                    details.addProperty("furnished", room.getDetails().isFurnished());
                    JsonArray amenities = new JsonArray();
                    for (String amenity : room.getDetails().getAmenities()) {
                        amenities.add(amenity);
                    }
                    details.add("amenities", amenities);
                    details.addProperty("live_in_landlord", room.getDetails().isLiveInLandlord());
                    details.addProperty("shared_with", room.getDetails().getSharedWith());
                    details.addProperty("bills_included", room.getDetails().isBillsIncluded());
                    details.addProperty("bathroom_shared", room.getDetails().isBathroomShared());
                    roomDetails.add("details", details);

                    roomDetails.addProperty("price_per_month_gbp", room.getPricePerMonthGbp());

                    String availabilityDate = room.getAvailabilityDate() != null
                            ? room.getAvailabilityDate().toString()
                            : "N/A";
                    roomDetails.addProperty("availability_date", availabilityDate);

                    JsonArray spokenLanguages = new JsonArray();
                    for (String language : room.getSpokenLanguages()) {
                        spokenLanguages.add(language);
                    }
                    roomDetails.add("spoken_languages", spokenLanguages);

                    roomsArray.add(roomDetails);
                }
            }

            result.add("rooms", roomsArray);

            if (!IDFound) {
                result.addProperty("message", "No rooms found for ID: " + roomId + ".");
            }
        } catch (IOException e) {
            result.addProperty("message", "Error reading rooms data: " + e.getMessage());
        }

        return result;
    }
}
