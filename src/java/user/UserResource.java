/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

@Path("/users")
public class UserResource {

    private static final String JSON_FILE = "/Users/Tino/Desktop/Service-Centric & Cloud Comp/NetBeansProjects/GlobalDormApp/WEB-INF/data/user.json";
    private static final Gson gson = new Gson();

    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testEndpoint() {
        return Response.ok("{\"message\":\"Test endpoint is working!\"}").build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(String userData) {
        JsonObject jsonObject = new Gson().fromJson(userData, JsonObject.class);
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();

        try {
            List<User> users = loadUsers();

            // Check if username already exists
            for (User existingUser : users) {
                if (existingUser.getUsername().equalsIgnoreCase(username)) {
                    System.out.println("Username already exists");
                    return Response.status(Response.Status.CONFLICT)
                            .entity("{\"error\": \"Username already exists\"}")
                            .build();
                }
            }

            // Hash the password
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            User newUser = new User(users.size() + 1, username, hashedPassword);

            // Add new user to the list and save to JSON
            users.add(newUser);
            saveUsers(users);

            // Debugging: Print the new user and the updated user list
            System.out.println("New user added: " + newUser);
            System.out.println("Updated user list: " + users);

            return Response.status(Response.Status.CREATED)
                    .entity(new Gson().toJson(newUser))
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Internal server error\"}")
                    .build();
        }
    }

    public String registerUser(String username, String password) throws IOException {
        List<User> users = loadUsers();

        // Check if username already exists
        for (User existingUser : users) {
            if (existingUser.getUsername().equalsIgnoreCase(username)) {
                System.out.println("Username already exists");
                return "{\"error\": \"Username already exists\"}";
            }
        }

        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User(users.size() + 1, username, hashedPassword);

        // Add new user to the list and save to JSON
        users.add(newUser);
        saveUsers(users);

        // Debugging: Print the new user and the updated user list
        System.out.println("New user added: " + newUser);
        System.out.println("Updated user list: " + users);

        return gson.toJson(newUser);
    }

    public String loginUser(String username, String password) throws IOException {
        List<User> users = loadUsers();
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username or password cannot be null");
        }

        for (User existingUser : users) {
            if (existingUser.getUsername().equalsIgnoreCase(username)) {
                if (BCrypt.checkpw(password, existingUser.getPassword())) {
                    return gson.toJson(existingUser);
                } else {
                    return "{\"error\": \"Incorrect password\"}";
                }
            }
        }

        return "{\"error\": \"User not found. Please register.\"} " + users;
    }

    private static List<User> loadUsers() throws IOException {
        File file = new File(JSON_FILE);
        if (!file.exists()) {
            // Create the directory if it doesn't exist
            file.getParentFile().mkdirs();
            // Create the file
            file.createNewFile();
            System.out.println("User.json does not exist. Creating a new file and returning an empty list.");
            return new ArrayList<>(); // Return an empty list if the file doesn't exist
        }

        try (FileReader reader = new FileReader(file)) {
            Type userListType = new TypeToken<List<User>>() {
            }.getType();
            List<User> users = gson.fromJson(reader, userListType);

            // Debugging: Print the loaded users
            System.out.println("Loaded users: " + users);
            return users != null ? users : new ArrayList<>(); // Ensure a non-null list is returned
        }
    }

    private static void saveUsers(List<User> users) throws IOException {
        // Debugging: Print the users list before saving
        System.out.println("Saving the following users to JSON: " + users);

        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(users, writer);
        }

        // Debugging: Confirm the save process
        System.out.println("Users saved successfully.");
    }

    class User {

        private int userId;
        private String username;
        private String password;

        public User() {
        }

        public User(int userId, String username, String password) {
            this.userId = userId;
            this.username = username;
            this.password = password;
        }

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        // Getter and Setter methods
        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "User{"
                    + "userId=" + userId
                    + ", username='" + username + '\''
                    + '}';
        }
    }
}
