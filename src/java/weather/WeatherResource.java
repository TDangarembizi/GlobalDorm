/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weather;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("weather")
public class WeatherResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of WeatherResource
     */
    public WeatherResource() {
    }

    /**
     * Retrieves representation of an instance of weather.WeatherResource
     *
     * @return an instance of java.lang.String
     */
    private static final String GEOCODE_BASE_URL = "https://geocoding-api.open-meteo.com/v1/search";
    private static final String WEATHER_BASE_URL = "https://api.open-meteo.com/v1/forecast";
    private static final String TARGET_COUNTRY = "United Kingdom";

    @POST
    @Path("/test")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response testEndpoint(String jsonData) {
        JsonObject jsonObject = new Gson().fromJson(jsonData, JsonObject.class);
        String message = jsonObject.get("message").getAsString();
        return Response.ok("{\"response\": \"Received: " + message + "\"}").build();
    }

    @GET
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String getJson(@QueryParam("name") String name) throws UnsupportedEncodingException {
        if (name == null || name.isEmpty()) {
            return "{\"error\": \"City name is required\"}";
        }
        return getWeather(name);
    }

    public String getWeather(String name) throws UnsupportedEncodingException {
        double[] coords = parseAndExtractCoords(getCoords(name));
        String url = buildWeatherUrl(coords);
        String json = fetchApiResponse(url);
        return json;
    }

    public String getCoords(String city) {
        try {
            String fullUrl = GEOCODE_BASE_URL + "?name=" + city;
            String jsonResponse = fetchApiResponse(fullUrl);

            if (jsonResponse == null) {
                return "{\"error\": \"Failed to retrieve data from the API.\"}";
            }

            return jsonResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"An error occurred: " + e.getMessage() + "\"}";
        }
    }

    public static String buildWeatherUrl(double[] coords) throws UnsupportedEncodingException {
        List<String> dailyParameters = new ArrayList<>();
        dailyParameters.add("temperature_2m_max");
        dailyParameters.add("temperature_2m_min");
        dailyParameters.add("sunrise");
        dailyParameters.add("sunset");
        dailyParameters.add("uv_index_max");
        dailyParameters.add("precipitation_hours");

        Map<String, String> params = new HashMap<>();
        params.put("latitude", String.valueOf(coords[0]));
        params.put("longitude", String.valueOf(coords[1]));
        params.put("daily", String.join(",", dailyParameters));
        params.put("timezone", "GMT");

        StringBuilder urlBuilder = new StringBuilder(WEATHER_BASE_URL);
        urlBuilder.append("?");

        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                urlBuilder.append("&");
            }
            first = false;
            urlBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            urlBuilder.append("=");
            urlBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return urlBuilder.toString();
    }

    public String fetchApiResponse(String urlString) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        } catch (Exception e) {
            System.out.println("Error fetching API response: " + e.getMessage()); // Debug statement
            return null;
        }
        return result.toString();
    }

    private double[] parseAndExtractCoords(String jsonResponse) {
        double[] coords = new double[2];

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        if (jsonObject.has("results")) {
            JsonArray results = jsonObject.getAsJsonArray("results");

            for (int i = 0; i < results.size(); i++) {
                JsonObject location = results.get(i).getAsJsonObject();
                String country = location.get("country").getAsString();

                if (checkCountry(country)) {
                    double latitude = location.get("latitude").getAsDouble();
                    double longitude = location.get("longitude").getAsDouble();

                    coords[0] = latitude;
                    coords[1] = longitude;
                    return coords;
                }
            }
        }
        return null;
    }

    private boolean checkCountry(String cnt) {
        return TARGET_COUNTRY.equalsIgnoreCase(cnt);
    }
}
