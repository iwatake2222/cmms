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
    public boolean searchRepairRequest = false, createRepairRequest = false, displayModifyRepairRequest = false, displayListOfRepairRequest = false;
    public boolean createMaintenanceLog = false, displayModifyMaintenanceLog = false, displayListOfMaintenanceLog = false;


    private User() {
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }
    public boolean searchRepairRequest() {
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
    public boolean createRepairRequest() {
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
    public boolean displayModifyRepairRequest() {
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
    public boolean displayListOfRepairRequest() {
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
    public boolean createMaintenanceLog() {
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
    public boolean displayModifyMaintenanceLog() {
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
    public boolean displayListOfMaintenanceLog() {
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
    // left this businees stuff in maybe we can hide the button from other users so they can save a bunch  of files that the admin has to go through later
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
