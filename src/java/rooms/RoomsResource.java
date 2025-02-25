/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rooms;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Tino
 */
@Path("rooms")
public class RoomsResource {

    String JSON_FILE = "/Users/Tino/Desktop/Service-Centric & Cloud Comp/NetBeansProjects/GlobalDormApp/WEB-INF/data/rooms.json";

    @Context
    private UriInfo context;

    public RoomsResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson(@QueryParam("city") String city) {
        Gson gson = new Gson();

        if (city == null || city.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"City parameter is missing.\"}")
                    .build();
        }

        try (InputStreamReader reader = new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(JSON_FILE), StandardCharsets.UTF_8)) {

            if (reader == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"rooms.json file not found.\"}")
                        .build();
            }

            Type roomsType = new TypeToken<Rooms>() {
            }.getType();
            Rooms rooms = gson.fromJson(reader, roomsType);

            JsonObject result = searchRoomsByCity(rooms, city);
            return Response.ok(gson.toJson(result), MediaType.APPLICATION_JSON).build();

        } catch (NullPointerException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"A null pointer exception occurred.\"}" + e)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"An unexpected error occurred.\"}" + e)
                    .build();
        }
    }

    public static JsonObject searchRoomsByCity(Rooms rooms, String userInputCity) {
        JsonObject result = new JsonObject();
        JsonArray roomsArray = new JsonArray();

        boolean cityFound = false;
        for (Rooms.Room room : rooms.getRooms()) {
            if (room.getLocation().getCity().equalsIgnoreCase(userInputCity)) {
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
                roomDetails.addProperty("availability_date", room.getAvailabilityDate() != null
                        ? room.getAvailabilityDate().toString()
                        : "N/A");

                JsonArray spokenLanguages = new JsonArray();
                for (String language : room.getSpokenLanguages()) {
                    spokenLanguages.add(language);
                }
                roomDetails.add("spoken_languages", spokenLanguages);

                roomsArray.add(roomDetails);
            }
        }

        result.add("rooms", roomsArray);
        if (!cityFound) {
            result.addProperty("message", "No rooms found in " + userInputCity + ".");
        }

        return result;
    }
}
