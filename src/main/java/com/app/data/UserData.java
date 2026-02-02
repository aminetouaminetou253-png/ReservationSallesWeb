package com.app.data;

import com.app.model.User;

public class UserData {

    public static User findByUsernameAndPassword(String username, String password) {

        if (username == null || password == null) {
            return null;
        }

        // User admin
        if (username.equals("admin") && password.equals("admin")) {
            return new User("admin", "ADMIN");
        }

        // User normal
        if (username.equals("user") && password.equals("user")) {
            return new User("user", "USER");
        }

        return null;
    }
}
