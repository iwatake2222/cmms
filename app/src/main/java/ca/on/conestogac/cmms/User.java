package ca.on.conestogac.cmms;
/**
 * Created by user on 2016-02-23.
 */
public class User {
    private static User instance = null;
    public String userID;
    public String password;
    public String accessLevel;
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


}
