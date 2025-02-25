/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import user.UserResource;

/**
 *
 * @author Tino
 */
public class ApplicationOps {

    public String app(String username, int roomId, String status) {
        try {
            List<ApplicationData> applications = loadApplications();

            // Add new application data
            ApplicationData newApplication = new ApplicationData();
            newApplication.setUsername(username);
            newApplication.setRoomId(roomId);
            newApplication.setStatus(status);

            applications.add(newApplication);

            // Save the updated list
            saveApplication(applications);

            System.out.println(newApplication.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String JSON_FILE = "/Users/Tino/Desktop/Service-Centric & Cloud Comp/NetBeansProjects/GlobalDormApp/WEB-INF/data/applications.json";
    private static final Gson gson = new Gson();

    public static List<application.ApplicationData> loadApplications() throws IOException {
        File file = new File(JSON_FILE);
        if (!file.exists()) {
            // Create the directory if it doesn't exist
            file.getParentFile().mkdirs();
            // Create the file
            file.createNewFile();
            System.out.println("applications.json does not exist. Creating a new file and returning an empty list.");
            return new ArrayList<>(); // Return an empty list if the file doesn't exist
        }
        try (FileReader reader = new FileReader(file)) {
            Type applicationListType = new TypeToken<List<application.ApplicationData>>() {
            }.getType();
            List<application.ApplicationData> applications = gson.fromJson(reader, applicationListType);

            // Debugging: Print the loaded users
            System.out.println("Loaded applications: " + applications);
            return applications != null ? applications : new ArrayList<>(); // Ensure a non-null list is returned
        }
    }

    private static void saveApplication(List<application.ApplicationData> applications) throws IOException {
        // Debugging: Print the users list before saving
        System.out.println("Saving the following users to JSON: " + applications);

        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(applications, writer);
        }

        // Debugging: Confirm the save process
        System.out.println("Application saved successfully.");
    }

    public static String getApplicationsByUsername(String username) {
        try {
            List<ApplicationData> applications = loadApplications();
            List<ApplicationData> userApplications = new ArrayList<>();
            for (ApplicationData application : applications) {
                if (application.getUsername().equals(username)) {
                    userApplications.add(application);
                }
            }
            return gson.toJson(userApplications);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Return null if an error occurs
    }

    public boolean updateApplicationStatus(String username, int roomId, String newStatus) {
        try {
            List<ApplicationData> applications = loadApplications();
            for (ApplicationData application : applications) {
                if (application.getUsername().equals(username) && application.getRoomId() == roomId && "Pending".equals(application.getStatus())) {
                    application.setStatus(newStatus);
                    saveApplication(applications);
                    return true; // Status updated successfully
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Status update failed
    }

    public String cancelApplication(String username, int roomId) {
        System.out.println("Username: " + username);
        System.out.println("Room ID: " + roomId);

        if (username == null || username.isEmpty() || roomId <= 0) {
            return "Username and Room ID are required";

        }

        String urlString = "http://localhost:8080/GlobalDormApp/webresources/applications/" + username + "/" + roomId;
        System.out.println("Constructed URL: " + urlString);

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String data = "\"Cancelled\"";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return "Application cancelled successfully";
            } else {
                return "Network response was not ok: " + responseCode;
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public boolean hasApplied(String username, int roomId) {
        try {
            List<ApplicationData> applications = loadApplications();
            for (ApplicationData application : applications) {
                if (application.getUsername().equals(username) && application.getRoomId() == roomId) {
                    return true; // Application found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Application not found or an error occurred
    }

}
