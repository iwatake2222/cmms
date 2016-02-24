package ca.on.conestogac.cmms;

/**
 * Created by user on 2016-02-23.
 */
public class User {
    private static User instance = null;
    public String userID;

    private User() {
    }

    public static User getInstance() {
        if(instance == null) {
            instance = new User();
        }
        return instance;
    }

}
