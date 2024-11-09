package simulare_liga;

import java.sql.*;

public class Database {
    private static Connection conn = null;

    private static Database INSTANCE;

    private Database() {
        connect();
    }

    public static Database getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Database();
        }

        return INSTANCE;
    }

    public static void connect() {
        try {
            conn = DriverManager.getConnection(Constants.getURL(),
                    Constants.getUSERNAME(), Constants.getPASSWORD());

            System.out.println("Connected to the database successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if(conn != null)
            return conn;

        return null;
    }
}