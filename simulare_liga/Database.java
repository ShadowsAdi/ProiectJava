package simulare_liga;

import java.sql.*;

public class Database {
    // Singleton pattern pentru conexiunea la baza de date
    private static Connection conn = null;

    private static Database INSTANCE;

    private Database() {
        // Se incearca conectarea la baza de date
        connect();
    }

    public static Database getInstance() {
        // Daca instanta nu a fost creata, se creaza una noua
        if(INSTANCE == null) {
            INSTANCE = new Database();
        }

        // Returneza instanta
        return INSTANCE;
    }

    public static void connect() {
        try {
            conn = DriverManager.getConnection(Constants.getURL(),
                    Constants.getUSERNAME(), Constants.getPASSWORD());
            if(Constants.DEBUG)
                System.out.println("Connected to the database successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    // Returneaza conexiunea la baza de date sau null daca nu s-a putut realiza conexiunea
    public static Connection getConnection() {
        if(conn != null)
            return conn;

        return null;
    }
}