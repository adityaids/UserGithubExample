package com.aditya.usergithub.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserList {

    @SerializedName("items")
    ArrayList<User> resultUser;

    public ArrayList<User> getResultUser() {
        return resultUser;
    }

    public void setResultUser(ArrayList<User> resultUser) {
        this.resultUser = resultUser;
    }
}
