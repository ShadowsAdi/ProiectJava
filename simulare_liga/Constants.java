package simulare_liga;

public class Constants {

    private static final String URL = "jdbc:mysql://localhost:3306/test";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "test";

    public static final String TABLE_NAME_ECHIPE = "Echipe";

    public static final String TABLE_NAME_STATS = "Statistici";

    public static final String INSERT_TRIGGER = "insert_trigger1";
    public static final String DELETE_TRIGGER = "delete_trigger1";

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
