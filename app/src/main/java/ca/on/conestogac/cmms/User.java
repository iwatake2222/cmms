package ca.on.conestogac.cmms;
/**
 * Created by user on 2016-02-23.
 */
public class User {
    private static User instance = null;
    public static int accessLevel;
    public String userID;
    public String password;
    public boolean canSearchForMachine = false, canSearchForRepairRequest = false, canAccessHome = false, canAccessMachineInformation = false;
    public boolean canCreateWorkRequest = false, canDisplayWorkRequest = false, canAccessMaintenanceLogList = false, canSearchForWorkRequest = false, canViewBusinessReport = false;


    private User() {
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }
    public boolean canSearchForMachine() {
        switch (accessLevel) {
            default:
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
        }
    }
    public boolean canSearchForRepairRequest() {
        switch (accessLevel) {
            default:
            case 0:
                return false;
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
        }
    }
    public boolean canAccessHome() {
        switch (accessLevel) {
            default:
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
        }
    }
    public boolean canAccessMachineInformation() {
        switch (accessLevel) {
            default:
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
        }
    }
    public boolean canCreateWorkRequest() {
        switch (accessLevel) {
            default:
            case 0:
                return false;
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
        }
    }
    public boolean canDisplayWorkRequest() {
        switch (accessLevel) {
            default:
            case 0:
                return false;
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
        }
    }
    public boolean canAccessMaintenanceLogList() {
        switch (accessLevel) {
            default:
            case 0:
                return false;
            case 1:
                return true;
            case 2:
                return false;
            case 3:
                return true;
        }
    }
    public boolean canSearchForWorkRequest() {
        switch (accessLevel) {
            default:
            case 0:
                return false;
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
        }
    }
    public boolean canViewBusinessReport() {
        switch (accessLevel) {
            default:
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                return false;
            case 3:
                return true;
        }
    }

}
