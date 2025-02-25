<%-- 
    Document   : applications
    Created on : 02-Jan-2025, 12:47:20
    Author     : Tino
--%>

<%@page import="com.google.gson.JsonArray"%>
<%@page import="rooms.RoomsOperations"%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="java.io.FileReader"%>
<%@page import="rooms.Rooms"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.lang.reflect.Type"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="application.ApplicationData"%>
<%@page import="application.ApplicationOps"%>
<%@page import="java.util.List"%>
<%@page import="java.io.IOException"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Applications</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
                background-color: #f8f9fa;
            }
            .table-container { 
                padding-left: 20px; /* Adjust the padding as needed */ 
                padding-right: 20px; /* Adjust the padding as needed */ 
            }
            table {
                width: 100%;
                border-collapse: collapse;
            }
            table, th, td {
                border: 1px solid black;
            }
            th, td {
                padding: 8px;
                text-align: left;
            }
            th {
                background-color: #f2f2f2;
            }
            tr:nth-child(even) {
                background-color: #f9f9f9;
            }
            tr:hover {
                background-color: #e0e0e0;
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
        </style>
        <script>

            function cancelApplication(username, roomId) {
                console.log("Username:", username);
                console.log("Room ID:", roomId);

                if (!username || !roomId) {
                    console.error('Username and Room ID are required');
                    return;
                }
                const url = 'http://localhost:8080/GlobalDormApp/webresources/applications/' + username + '/' + roomId;
                console.log("Constructed URL:", url);
                const data = JSON.stringify("Cancelled");

                fetch(url, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: data
                })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Network response was not ok');
                            }
                            return response.json();
                        })
                        .then(data => {
                            console.log(data);
                            alert('Application cancelled successfully!');
                            window.location.reload();
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            alert('Failed to cancel application. Please try again.');
                        });
            }

            function goToRooms() {
                window.location.href = 'rooms.jsp';
            }

            function logOut() {
                window.location.href = 'index.html';
            }
        </script>
    </head>
    <%String username = (String) session.getAttribute("username");%>
    <h1>Applications for <%=username%></h1>
    <%//https://www.baeldung.com/jsp
        if (username != null && !username.isEmpty()) {
            try {
                String applicationsJson = ApplicationOps.getApplicationsByUsername(username);
                if (applicationsJson == null || applicationsJson.isEmpty()) {
                    out.println("<p>No applications found.</p>");
                } else {
                    Type applicationListType = new TypeToken<List<ApplicationData>>() {
                    }.getType();
                    List<ApplicationData> applications = new Gson().fromJson(applicationsJson, applicationListType);

                    out.println("<div class=\"table-container\">");
                    out.println("<table border='1'>");
                    out.println("<tr><th>Property</th><th>Application Status</th></tr>");
                    if (applications.isEmpty()) {
                        // Display message if no applications exist
                        out.println("<tr><td colspan='2' style='text-align: center;'>You have no applications at this time.</td></tr>");
                    } else {
                        for (ApplicationData app : applications) {

                            RoomsOperations ops = new RoomsOperations();
                            JsonObject jsonResult = ops.getRoomsById(app.getRoomId());

                            out.println("<tr>");

                            // Extract room details
                            Gson gson = new Gson();
                            JsonObject jsonObject = gson.fromJson(jsonResult, JsonObject.class);
                            JsonArray roomsArray = jsonObject.getAsJsonArray("rooms");

                            for (int i = 0; i < roomsArray.size(); i++) {
                                JsonObject room = roomsArray.get(i).getAsJsonObject();
                                String name = room.get("name").getAsString();
                                JsonObject location = room.getAsJsonObject("location");
                                String city = location.get("city").getAsString();
                                String postcode = location.get("postcode").getAsString();

                                out.println("<td>" + name + ", " + city + ", " + postcode + "</td>");
                            }
                            String user = app.getUsername();
                            int room = app.getRoomId();

                            if ("Pending".equals(app.getStatus())) {
%>
    <td><%=app.getStatus()%>
        <button onclick="cancelApplication('<%=app.getUsername()%>', '<%=app.getRoomId()%>')">Cancel</button>
    </td>
    <%
                            } else {
                                out.println("<td>" + app.getStatus() + "</td>");
                            }
                            out.println("</tr>");
                        }
                    }
                    out.println("</table>");
                    out.println("</div>");
                }
            } catch (IOException e) {
                e.printStackTrace();
                out.println("<p>Error loading applications.</p>");
            }
        } else {
            out.println("<p>No username provided!</p>");
        }
    %>
    <br>
    <button onclick="goToRooms()" style="width: auto;">Back</button>
    <button onclick="logOut()" style="width: auto; align-self: right;">Log Out</button>


</html>
