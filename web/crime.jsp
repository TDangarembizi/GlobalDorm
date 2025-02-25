<%-- 
    Document   : crime
    Created on : 05-Jan-2025, 17:27:08
    Author     : Tino
--%>

<%@page import="java.lang.reflect.Type"%>
<%@page import="crimeRate.CrimeData"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="java.util.List"%>
<%@page import="weather.WeatherResource"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Crime Rate</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
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
    </style>
</head>

<h1>Burglaries in the area</h1>

<%
    // Retrieve the postcode from the session
    String postcode = request.getParameter("postcode");
    if (postcode != null && !postcode.isEmpty()) {
        postcode = postcode.replaceAll(" ", "");
        // Fetch the crime data using the WeatherResource class
        String url = "http://localhost:8080/GlobalDormApp/webresources/generic?postcode=" + postcode;
        WeatherResource weather = new WeatherResource();
        String json = weather.fetchApiResponse(url);
        Type crimeListType = new TypeToken<List<CrimeData.Crime>>() {
        }.getType();
        List<CrimeData.Crime> crimeList = new Gson().fromJson(json, crimeListType);
        if (crimeList.isEmpty()) {
            out.println("<p>No data found for this area.</p>");
        } else {
            out.println("<div class=\"table-container\">");
            out.println("<table>");
            out.println("<tr><th>Street Name</th><th>Date</th><th>Outcome</th></tr>");
            for (CrimeData.Crime crime : crimeList) {
                out.println("<tr>");
                out.println("<td>" + crime.getLocation().getStreet().getName() + "</td>");
                out.println("<td>" + crime.getOutcomeStatus().getDate() + "</td>");
                out.println("<td>" + crime.getOutcomeStatus().getCategory() + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("</div>");
        }
    } else {
%>
<p>No postcode provided.</p>
<%
    }
%>
<br>
<button onclick="goToRooms()" style="width: auto;">Back</button>
<button onclick="logOut()" style="width: auto; align-self: right;">Log Out</button>

<script>
    function goToRooms() {
        window.location.href = 'rooms.jsp';
    }

    function logOut() {
        window.location.href = 'index.html';
    }
</script>

