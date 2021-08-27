package com.example.bnja.Modals;

public class Users {
    private String userName, bloodGroup, email,
            password, userId, Location, LastMessage, status;

    public Users(String userName, String bloodGroup,
                 String email, String password, String userId,
                 String location, String lastMessage, String status) {
        this.bloodGroup = bloodGroup;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        Location = location;
        LastMessage = lastMessage;
    }
    public  Users(){

    }

    public Users(String userName, String bloodGroup, String email, String password) {
        this.bloodGroup = bloodGroup;
        this.email = email;
        this.password = password;
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    public String getLocation() {
        return Location;
    }

    public String getLastMessage() {
        return LastMessage;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }
}
