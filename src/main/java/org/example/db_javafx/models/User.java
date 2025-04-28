package org.example.db_javafx.models;

import java.io.Serializable;

public class User implements Serializable {

    private String login;

    public User(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
