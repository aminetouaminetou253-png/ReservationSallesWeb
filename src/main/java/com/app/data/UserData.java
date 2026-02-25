package com.app.data;

import com.app.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserData {

    public static final List<User> users = new ArrayList<>();

    static {
        // admin par défaut
        users.add(new User(
                "admin", "admin", "ADMIN",
                "Admin", "System", "-", "admin@mail.com", "34562435"));

        // gestionnaire par défaut
        users.add(new User(
                "manager", "manager", "GESTIONNAIRE",
                "Ali", "Sal", "-", "ali@mail.com", "35421144"));
    }

    public static User findByUsernameAndPassword(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username)
                        && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public static boolean usernameExists(String username) {
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public static void addUser(User user) {
        users.add(user);
    }
    
    public static boolean removeManager(String username) {
        return users.removeIf(u ->
                u.getUsername().equals(username)
                && u.getRole().equals("GESTIONNAIRE")
        );
    }
}