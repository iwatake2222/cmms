package ca.on.conestogac.cmms;
/**
 * Created by user on 2016-02-23.
 */
public class User {
    private static User instance = null;
    public  int accessLevel;
    public String userID;
    public String password;
    private static final int ACCESS_LEVEL_STUDENT=0, ACCESS_LEVEL_TECHNICIAN=1, ACCESS_LEVEL_INSTRUCTOR=2, ACCESS_LEVEL_ADMINISTRATOR=3;

    private User() {
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }
    public boolean canSearchRepairRequest() {
        switch (accessLevel) {
            default:
            case ACCESS_LEVEL_STUDENT:
                return false;
            case ACCESS_LEVEL_TECHNICIAN:
                return true;
            case ACCESS_LEVEL_INSTRUCTOR:
                return true;
            case ACCESS_LEVEL_ADMINISTRATOR:
                return true;
        }
    }
    public boolean canCreateRepairRequest() {
        switch (accessLevel) {
            default:
            case ACCESS_LEVEL_STUDENT:
                return false;
            case ACCESS_LEVEL_TECHNICIAN:
                return true;
            case ACCESS_LEVEL_INSTRUCTOR:
                return true;
            case ACCESS_LEVEL_ADMINISTRATOR:
                return true;
        }
    }
    public boolean canDisplayModifyRepairRequest() {
        switch (accessLevel) {
            default:
            case ACCESS_LEVEL_STUDENT:
                return false;
            case ACCESS_LEVEL_TECHNICIAN:
                return true;
            case ACCESS_LEVEL_INSTRUCTOR:
                return true;
            case ACCESS_LEVEL_ADMINISTRATOR:
                return true;
        }
    }
    public boolean canDisplayListOfRepairRequest() {
        switch (accessLevel) {
            default:
            case ACCESS_LEVEL_STUDENT:
                return false;
            case ACCESS_LEVEL_TECHNICIAN:
                return true;
            case ACCESS_LEVEL_INSTRUCTOR:
                return true;
            case ACCESS_LEVEL_ADMINISTRATOR:
                return true;
        }
    }
    public boolean canCreateMaintenanceLog() {
        switch (accessLevel) {
            default:
            case ACCESS_LEVEL_STUDENT:
                return false;
            case ACCESS_LEVEL_TECHNICIAN:
                return true;
            case ACCESS_LEVEL_INSTRUCTOR:
                return false;
            case ACCESS_LEVEL_ADMINISTRATOR:
                return true;
        }
    }
    public boolean canDisplayModifyMaintenanceLog() {
        switch (accessLevel) {
            default:
            case ACCESS_LEVEL_STUDENT:
                return false;
            case ACCESS_LEVEL_TECHNICIAN:
                return true;
            case ACCESS_LEVEL_INSTRUCTOR:
                return false;
            case ACCESS_LEVEL_ADMINISTRATOR:
                return true;
        }
    }
    public boolean canDisplayListOfMaintenanceLog() {
        switch (accessLevel) {
            default:
            case ACCESS_LEVEL_STUDENT:
                return false;
            case ACCESS_LEVEL_TECHNICIAN:
                return true;
            case ACCESS_LEVEL_INSTRUCTOR:
                return false;
            case ACCESS_LEVEL_ADMINISTRATOR:
                return true;
        }
    }
    // left this business stuff in maybe we can hide the button from other users so they can save a bunch  of files that the admin has to go through later
    public boolean canViewBusinessReport() {
        switch (accessLevel) {
            default:
            case ACCESS_LEVEL_STUDENT:
                return false;
            case ACCESS_LEVEL_TECHNICIAN:
                return false;
            case ACCESS_LEVEL_INSTRUCTOR:
                return false;
            case ACCESS_LEVEL_ADMINISTRATOR:
                return true;
        }
    }

}
