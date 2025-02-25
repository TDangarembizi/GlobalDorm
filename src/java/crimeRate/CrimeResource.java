/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crimeRate;

import com.google.gson.Gson;
import distanceCalculator.DistanceData;
import distanceCalculator.DistanceResource;
import java.io.UnsupportedEncodingException;
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
import weather.WeatherResource;

/**
 * REST Web Service
 *
 * @author Tino
 */
@Path("generic")
public class CrimeResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CrimeResource
     */
    public CrimeResource() {
    }

    private static final String CRIME_BASE_URL = "https://data.police.uk/api/crimes-street/burglary?date=2024-11&lat=";

    @GET
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public Response getJson(@QueryParam("postcode") String postcode) throws UnsupportedEncodingException {
        if (postcode == null || postcode.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Postcode required\"}")
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        }

        String crime = getCrimes(postcode);
        return Response.ok(crime)
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }

    String urlBuilder(double[] coords) {
        String url = CRIME_BASE_URL + coords[1] + "&lng=" + coords[0];
        return url;
    }

    String getCrimes(String postcode) {
        double[] coords = getCoordinates(postcode);
        String url = urlBuilder(coords);
        WeatherResource weather = new WeatherResource();
        String json = weather.fetchApiResponse(url);
        return json;
    }

    public static double[] convertStringToCoordinates(String str) {
        String[] parts = str.split(",");
        double longitude = Double.parseDouble(parts[0]);
        double latitude = Double.parseDouble(parts[1]);
        return new double[]{longitude, latitude};
    }

    public static double[] getCoordinates(String postcode) {
        DistanceResource coord = new DistanceResource();
        String coordJson = coord.getCoordinates(postcode);
        String coordinates=coord.parseCoords(coordJson);
        double[] coords = convertStringToCoordinates(coordinates);
        return coords;
    }

}
