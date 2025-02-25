/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distanceCalculator;

import com.google.gson.Gson;
import distanceCalculator.DistanceData.Data;
import java.io.UnsupportedEncodingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import weather.WeatherResource;

/**
 * REST Web Service
 *
 * @author Tino
 */
@Path("distance")
public class DistanceResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DistanceResource
     */
    public DistanceResource() {
    }

    /**
     * Retrieves representation of an instance of
     * distanceCalculator.DistanceResource
     *
     * @return an instance of java.lang.String
     */
    private static final String GEOCODE_URL = "http://api.getthedata.com/postcode/";
    private static final String DISTANCE_URL = "http://router.project-osrm.org/route/v1/driving/";

    @GET
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public Response getJson(@QueryParam("postcode1") String pc1, @QueryParam("postcode2") String pc2) throws UnsupportedEncodingException {
        if (pc1 == null || pc2 == null || pc1.isEmpty() || pc2.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Two postcodes are required\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        }
        String distance = getDistance(pc1, pc2);
        return Response.ok(distance)
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }

    public static String getCoordinates(String postcode) {
        String url = GEOCODE_URL + postcode;
        WeatherResource weather = new WeatherResource();
        String json = weather.fetchApiResponse(url);
        return json;
    }

    public static String parseCoords(String json) {
        Gson gson = new Gson();
        if (json == null) {
            System.out.println("coordJson is null"); // Debug statement
            return null;
        }
        // Parse the JSON string into a Data object
        Data data = gson.fromJson(json, Data.class);

        // Extract latitude and longitude
        String latitude = data.data.latitude;
        String longitude = data.data.longitude;
        String coords = longitude + "," + latitude;
        return coords;
    }

    public static String buildUrl(String coords1, String coords2) {
        String url = DISTANCE_URL + coords1 + ";" + coords2 + "?overview=false";
        return url;
    }

    private String getDistance(String pc1, String pc2) {
        WeatherResource weather = new WeatherResource();
        String coords1 = parseCoords(getCoordinates(pc1));
        String coords2 = parseCoords(getCoordinates(pc2));
        String json = weather.fetchApiResponse(buildUrl(coords1, coords2));
        return json;
    }

    public static String parseDistance(String json) {
        Gson gson = new Gson();
        DistanceData.Distance distanceData = gson.fromJson(json, DistanceData.Distance.class);
        double distance = distanceData.getRoutes()[0].getLegs()[0].getDistance();
        distance = distance / 1609;
        return String.format("%.1f", distance);
    }

    public boolean isValidPostcode(String postcode) {
        String postcodeRegex = "^([A-Z]{1,2}[0-9][0-9A-Z]?)\\s?([0-9][A-Z]{2})$";
        return postcode.matches(postcodeRegex);
    }

}
