package com.app.data;

import com.app.model.User;

public class UserData {

    public static User findByUsernameAndPassword(String username, String password) {

    	if (username == null || password == null) {
    	    return null;
    	}

    	if (username.equals("admin") && password.equals("admin")) {
    	    return new User("admin", "ADMIN");
    	}

    	if (username.equals("manager") && password.equals("manager")) {
    	    return new User("manager", "GESTIONNAIRE");
    	}

    	if (username.equals("client") && password.equals("client")) {
    	    return new User("client", "CLIENT");
    	}


        return null;
        
        
    }
}
