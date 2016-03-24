package ca.on.conestogac.cmms;

/**
 * Created by user on 2016-02-23.
 */
public class ValueConstants {
    private ValueConstants(){}

    //public static final String SERVER_URL = "http://192.168.0.104:80/saveJsonPOST.php/";
    public static final String SERVER_URL = "http://cmmsmock.apphb.com/Service1.svc/";
    public static final int SERVER_TIMEOUT = 10000;

    public static final String ITEM_ANY = "Any";
    public static final String ITEM_NOTSELECTED = "---";

    public static final String RET_OK = "ok";
    public static final String ERROR_FORMAT = "error_format";
    public static final String ERROR_PARAMETER = "error_parameter";
    public static final String ERROR_TIMEOUT = "error_timeout";
    public static final String ERROR_USER_LEVEL = "error_user_level";
    public static final String ERROR_OTHER = "error";


    public static final String DATE_FORMAT = "yyyyMMdd";
    public static final String DATE_FORMAT_FORMATTED = "yyyy/MM/dd";
}
