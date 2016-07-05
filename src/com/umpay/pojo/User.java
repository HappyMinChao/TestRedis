package com.umpay.pojo;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String gender;
    private String state;

    public User() {
    }

    public User(String username, String gender, String state) {
        this.username = username;
        this.gender = gender;
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", gender=" + gender + ", state="
                + state + "]";
    }

}
