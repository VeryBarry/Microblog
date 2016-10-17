package com.company;

import java.util.ArrayList;

/**
 * Created by VeryBarry on 10/4/16.
 */
public class User {
    String name;
    String password;
    ArrayList<String> messages;

    public User(String name, String password, ArrayList<String> messages) {
        this.name = name;
        this.password = password;
        this.messages = messages;
    }
}
