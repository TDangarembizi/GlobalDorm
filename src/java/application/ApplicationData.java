/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

/**
 *
 * @author Tino
 */
//https://jsonformatter.org/json-to-java
public class ApplicationData {

    private String username;
    private int roomId;
    private String status;

    public ApplicationData() {
    }

    public ApplicationData(String username, int roomId, String status) {
        this.username = username;
        this.roomId = roomId;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String value) {
        this.username = value;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int value) {
        this.roomId = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public String toString() {
        return "Application{"
                + "userId='" + username + '\''
                + ", roomId=" + roomId
                + ", status='" + status + '\''
                + '}';
    }

}
