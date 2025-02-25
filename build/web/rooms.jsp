<%@page import="application.ApplicationOps"%>
<%@page import="javax.ws.rs.client.Entity"%>
<%@page import="rooms.Rooms.Room"%>
<%@page import="weather.WeatherResource"%>
<%@page import="weather.WeatherData" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page import="java.io.*, com.google.gson.*, com.google.gson.reflect.*, java.lang.reflect.Type, rooms.Rooms, rooms.RoomsOperations, com.google.gson.JsonObject, com.google.gson.JsonArray" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Room Information</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
                background-color: #f8f9fa;
            }
            .container {
                padding: 20px;
                max-width: 1200px;
                margin: auto;
            }
            .room-card {
                background: #fff;
                padding: 20px;
                margin-bottom: 20px;
                border-radius: 8px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }
            .room-card h2 {
                margin: 0 0 10px;
            }
            .room-card p {
                margin: 5px 0;
            }
            .weather {
                background: #e7f4ff;
                padding: 15px;
                border-radius: 8px;
                margin-top: 20px;
                display: flex;
                overflow-x: auto;
            }
            .weather-detail {
                flex: 0 0 auto;
                width: auto;
                margin-right: 10px;
                background-color: #fff;
                padding: 10px;
                border-radius: 8px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }
            #searchForm {
                display: flex;
                width: 100%;
            }
            .search-container {
                max-width: 600px;
                margin: 0 auto;
                display: flex;
                justify-content: center;
            }
            #searchInput {
                flex: 1;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 4px 0 0 4px;
                outline: none;
            }
            #searchInput:focus {
                border-color: #007bff;

            }
            button {
                padding: 10px 20px;
                border: 1px solid #007bff;
                background-color: #007bff;
                color: white;
                border-radius: 5px;
                cursor: pointer;
            }
            button:hover {
                background-color: #0056b3;
            }
            .content {
                padding: 0 18px;
                background-color: #f1f1f1;
                border-radius: 4px;
                margin-top: 10px;
            }
            .collapsible {
                background-color: #777;
                color: white;
                cursor: pointer;
                padding: 10px;
                width: 100%;
                border: none;
                text-align: left;
                outline: none;
                font-size: 15px;
            }

            .active, .collapsible:hover {
                background-color: #555;
            }

            .content {
                padding: 0 18px;
                display: none;
                overflow: hidden;
                background-color: #f1f1f1;
            }

            .app {
                text-align: right; /* Aligns the container to the right */
                margin: 20px 0;
            }
            .scrollable-div {
                width: 100%;
                height: 200px;
                overflow-x: scroll;
                padding: 10px;
                white-space: nowrap;
            }
            .weather-item {
                display: inline-block;
                width: auto;
                margin-right: 10px;
                padding: 8px;
                text-align: left;
                vertical-align: top;
            }
        </style>
    </head>
    <body>
        <h1>Room Information</h1>
        <%//https://www.baeldung.com/jsp
            String username = (String) session.getAttribute("username");
            if (username != null && !username.isEmpty()) {
                session.setAttribute("username", username);

        %>
        <h1>Welcome <%= username%>!</h1>
        <%
        } else {
        %>
        <h1>No username provided!</h1>
        <%
            }
        %>
        <div class="search-container">
            <form action="rooms.jsp" method="get">
                <input type="text" name="city" placeholder="Search by city...">
                <button type="submit">Search</button>
            </form>

        </div>
        <div class="app">
            <a href="applications.jsp">
                <button>View Room Applications</button>
            </a>    
        </div>
        <div id="room-listings">
            <%
                String JSON_FILE = "/Users/Tino/Desktop/Service-Centric & Cloud Comp/NetBeansProjects/GlobalDormApp/WEB-INF/data/rooms.json";

                String city = request.getParameter("city");
                Gson gson = new Gson();

                // Read the JSON file and parse it into a Rooms object
                try (FileReader reader = new FileReader(JSON_FILE)) {
                    Type roomsType = new TypeToken<Rooms>() {
                    }.getType();
                    Rooms rooms = gson.fromJson(reader, roomsType);

                    JsonObject result;
                    if (city != null && !city.isEmpty()) {
                        // Call the searchRoomsByCity method
                        result = RoomsOperations.searchRoomsByCity(rooms, city);
                    } else {
                        // Call the method to get all rooms
                        result = RoomsOperations.displayAllRooms(rooms);
                    }

                    // Print the result as a JSON string
                    String jsonResult = gson.toJson(result);

                    // Get the rooms array from the result
                    JsonArray roomsArray = result.getAsJsonArray("rooms");

                    if (roomsArray.size() > 0) {
                        for (int i = 0; i < roomsArray.size(); i++) {
                            JsonObject room = roomsArray.get(i).getAsJsonObject();
                            JsonObject location = room.getAsJsonObject("location");
                            JsonObject details = room.getAsJsonObject("details");

            %>
            <div class="room-card">

                <h2><%= room.get("name").getAsString()%></h2>
                <p><strong>Location:</strong> <%= location.get("city").getAsString()%>, <%= location.get("county").getAsString()%> (Postcode: <%= location.get("postcode").getAsString()%>)</p>
                <p><strong>Furnished:</strong> <%= details.get("furnished").getAsBoolean() ? "Yes" : "No"%></p>
                <p><strong>Amenities:</strong> <%= details.getAsJsonArray("amenities").toString().replaceAll("[\\[\\]\"]", "")%></p>
                <p><strong>Live-in Landlord:</strong> <%= details.get("live_in_landlord").getAsBoolean() ? "Yes" : "No"%></p>
                <p><strong>Shared With:</strong> <%= details.get("shared_with").getAsInt()%> people</p>
                <p><strong>Bills Included:</strong> <%= details.get("bills_included").getAsBoolean() ? "Yes" : "No"%></p>
                <p><strong>Bathroom:</strong> <%= details.get("bathroom_shared").getAsBoolean() ? "Shared" : "Private"%></p>
                <p><strong>Price per Month:</strong> £<%= room.get("price_per_month_gbp").getAsInt()%></p>
                <p><strong>Availability Date:</strong> <%= room.get("availability_date").getAsString()%></p>
                <p><strong>Spoken Languages:</strong> <%= room.getAsJsonArray("spoken_languages").toString().replaceAll("[\\[\\]\"]", "")%></p>
                <button type="button" class="collapsible">Show Weather Data</button>

                <div class="content">
                    <h3><%= location.get("city").getAsString()%></h3>
                    <%
                        String url = "http://localhost:8080/GlobalDormApp/webresources/weather?name=" + location.get("city").getAsString();
                        WeatherResource weather = new WeatherResource();
                        String json = weather.fetchApiResponse(url);

                    %>
                    <div class="scrollable-div">
                        <%                            
                            WeatherData weatherData = gson.fromJson(json, WeatherData.class);
                            //https://www.baeldung.com/java-simple-date-format
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");

                            for (int j = 0; j < weatherData.daily.time.size(); j++) {
                                Date sunriseDate = inputFormat.parse(weatherData.daily.sunrise.get(j));
                                Date sunsetDate = inputFormat.parse(weatherData.daily.sunset.get(j));
                                String formattedSunrise = outputFormat.format(sunriseDate);
                                String formattedSunset = outputFormat.format(sunsetDate);
                        %>
                        <div class="weather-item">
                            <strong>Date:</strong> <%= weatherData.daily.time.get(j)%><br>
                            <strong>Max Temp:</strong> <%= weatherData.daily.temperature2mMax.get(j)%> °C<br>
                            <strong>Min Temp:</strong> <%= weatherData.daily.temperature2mMin.get(j)%> °C<br>
                            <strong>Sunrise:</strong> <%= formattedSunrise%><br>
                            <strong>Sunset:</strong> <%= formattedSunset%><br>
                            <strong>UV Index Max:</strong> <%= weatherData.daily.uvIndexMax.get(j)%><br>
                            <strong>Precipitation Hours:</strong> <%= weatherData.daily.precipitationHours.get(j)%> hour(s)<br>
                        </div>
                        <%
                            }
                        %>
                    </div>
                </div>
                <div style="display: flex; justify-content: space-between;">

                    <a href="distance.jsp?postcode1=<%= location.get("postcode").getAsString()%>">
                        <button>Go to distance calculator</button>
                    </a>
                    <a href="crime.jsp?postcode=<%= location.get("postcode").getAsString()%>">
                        <button style="width: auto;">Incidents in the area</button>
                    </a>
                    <%
                        ApplicationOps ops = new ApplicationOps();
                        boolean hasApplied = ops.hasApplied(username, room.get("id").getAsInt());

                        if (username == null || username.isEmpty()) {
                    %> 
                    <button onclick="alert('No username provided.')">Apply for room</button>
                    <%
                    } else if (hasApplied) {
                    %> 
                    <button onclick="alert('You have already applied for this room.')">Apply for room</button> 
                    <%
                    } else {%> 
                    <button onclick="applyForRoom('<%=username%>', <%=room.get("id").getAsInt()%>)">Apply for room</button>
                    <%
                        }
                    %>                           
                </div>
            </div>

            <%
                        }
                    } else {
                        out.println("<p>No rooms found.</p>");
                    }
                } catch (Exception e) {
                    out.println("<p>Error fetching room data: " + e.getMessage() + "</p>");
                }
            %>
        </div>
        <button onclick="logOut()" style="width: auto; align-self: right" >Log Out</button>

    </body>
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            var coll = document.getElementsByClassName("collapsible");
            for (var i = 0; i < coll.length; i++) {
                coll[i].addEventListener("click", function () {
                    this.classList.toggle("active");
                    var content = this.nextElementSibling;
                    if (content.style.display === "block") {
                        content.style.display = "none";
                    } else {
                        content.style.display = "block";
                    }
                });
            }
        });

        function applyForRoom(username, roomId) {
            fetch('http://localhost:8080/GlobalDormApp/webresources/applications', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({username: username, roomId: roomId
                })
            })
                    .then(response => response.json())
                    .then(data => {
                        console.log('Success:', data);
                        alert('Application submitted successfully!');
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('An error occurred while submitting the application.');
                    });
        }

        function logOut() {
            window.location.href = 'index.html';
        }

    </script>
</html>
