<%-- 
    Document   : distance
    Created on : 06-Jan-2025, 15:31:19
    Author     : Tino
--%>

<%@page import="weather.WeatherResource"%>
<%@page import="com.google.gson.JsonParser"%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="distanceCalculator.DistanceResource"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Postcode Distance Calculator</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
            }
            form {
                margin-bottom: 20px;
            }
            label {
                display: block;
                margin-bottom: 5px;
            }
            input {
                margin-bottom: 10px;
                padding: 8px;
                width: 200px;
            }
            button {
                width: 100%;
                padding: 10px;
                background-color: #007BFF;
                color: #fff;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }
            button:hover {
                background-color: #0056b3;
            }
            .nav-button {
                margin-bottom: 20px;
                background-color: #4CAF50;
            }
            .nav-button:hover {
                background-color: #45a049;
            }
            .hidden {
                display: none;
            }
            #result {
                margin-top: 20px;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <h1>Postcode Distance Calculator</h1>
        <form method="post">
            <label for="postcode1">Starting location postcode:</label>
            <input type="text" id="postcode1" name="postcode1" value="<%= request.getParameter("postcode1") != null ? request.getParameter("postcode1") : ""%>" required>

            <label for="postcode2">Enter destination postcode:</label>
            <input type="text" id="postcode2" name="postcode2" value="<%= request.getParameter("postcode2") != null ? request.getParameter("postcode2") : ""%>" required>

            <button type="submit" style="width: auto;">Calculate Distance</button>
        </form>
        <p id="result">
            <%//https://www.baeldung.com/jsp
                String postcode1 = request.getParameter("postcode1");
                String postcode2 = request.getParameter("postcode2");
                if (postcode1 != null && postcode2 != null) {
                    postcode1 = postcode1.toUpperCase().replaceAll(" ", "");
                    postcode2 = postcode2.toUpperCase().replaceAll(" ", "");
                    DistanceResource distanceResource = new DistanceResource();
                    if (distanceResource.isValidPostcode(postcode2)) {
                        WeatherResource weather = new WeatherResource();
                        String jsonResponse = weather.fetchApiResponse("http://localhost:8080/GlobalDormApp/webresources/distance?postcode1=" + postcode1 + "&postcode2=" + postcode2);

                        Gson gson = new Gson();
                        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

                        if (jsonObject.has("error")) {
                            out.println(jsonObject.get("error").getAsString());
                        } else {
                            double distance = jsonObject.getAsJsonArray("routes")
                                    .get(0).getAsJsonObject()
                                    .getAsJsonArray("legs")
                                    .get(0).getAsJsonObject()
                                    .get("distance").getAsDouble() / 1609; // Convert meters to miles
                            out.println("Distance: " + String.format("%.1f", distance) + " miles");
                        }
                    } else {
            %>
            <script>
                alert('Invalid postcode format');
            </script>
            <%
                    }
                } else {
                    out.println("No postcode provided");
                }

            %>

        </p>


        <button onclick="goToRooms()" style="width: auto;">Back</button>
        <button onclick="logOut()" style="width: auto; align-self: right" >Log Out</button>

        <script>
            function goToRooms() {
                window.location.href = 'rooms.jsp';
            }

            function logOut() {
                window.location.href = 'index.html';
            }
            const urlParams = new URLSearchParams(window.location.search);
            const postcode1 = urlParams.get('postcode1');
            if (postcode1) {
                document.getElementById('postcode1').value = postcode1;
            }
        </script>
    </body>
</html>
