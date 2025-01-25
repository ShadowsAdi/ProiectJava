package simulare_liga;

public class Constants {
    private static final String URL = "jdbc:mysql://hostname/db";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    // numele tabelelor din baza de date
    public static final String TABLE_NAME_ECHIPE = "Echipe";
    public static final String TABLE_NAME_STATS = "Statistici";
    public static final String TABLE_NAME_MECIURI = "Meciuri";

    // numele triggerelor pentru insert si delete in baza de date
    public static final String INSERT_TRIGGER = "insert_trigger1";
    public static final String DELETE_TRIGGER = "delete_trigger1";

    public static final boolean DEBUG = false;

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static String getURL() {
        return URL;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }
}
