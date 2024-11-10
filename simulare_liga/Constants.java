package simulare_liga;

public class Constants {

    private static final String URL = "jdbc:mysql://89.40.105.3:3306/csa_2153";
    private static final String USERNAME = "csa_2153";
    private static final String PASSWORD = "vTuT9uZJ8?deWgc";

    public static final String TABLE_NAME_ECHIPE = "Echipe";
    public static final String TABLE_NAME_STATS = "Statistici";
    public static final String TABLE_NAME_MECIURI = "Meciuri";

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
