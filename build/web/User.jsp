<%@page import="java.io.IOException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="user.UserResource" %>
<!DOCTYPE html>
<html>
    <head>
        <title>User Processing</title>
    </head>
    <body>
        <h1>User Processing</h1>
        <%//https://www.baeldung.com/jsp
            // Create an instance of UserResource
            UserResource userResource = new UserResource();

            // Get the form type to determine which form was submitted
            String formType = request.getParameter("formType");
            out.println("<p>Form Type: " + formType + "</p>");

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            out.println("<p>Username: " + username + "</p>");
            out.println("<p>Password: " + password + "</p>");

            try {
                if ("login".equals(formType)) {
                    // Handle login form submission
                    String json = userResource.loginUser(username, password);
                    if (!json.contains("error")) {
                        session.setAttribute("username", username);
                        response.sendRedirect("rooms.jsp");
                    } else {
                        out.println("<p>Login Response: " + json + "</p>");
                    }
                } else if ("signup".equals(formType)) {
                    // Handle sign-up form submission
                    String json = userResource.registerUser(username, password);
                    if (!json.contains("error")) {
                        session.setAttribute("username", username);
                        response.sendRedirect("rooms.jsp");
                    } else {
                        out.println("<p>Sign-Up Response: " + json + "</p>");
                    }
                } else {
                    out.println("<p>Invalid form submission.</p>");
                }
            } catch (IOException e) {
                e.printStackTrace();
                out.println("<p>Error processing request: " + e.getMessage() + "</p>");
            }
        %>
    </body>
</html>