package simulare_liga;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.sql.*;

//sa porneasca de aici gen aplicatia, good practice
public class Main {
    public static void main(String[] args) {

        Connection conn = Database.getInstance().getConnection();

        assert conn != null;
        createTables(conn);
        createTriggers(conn);

        /* Cred ca un hashmap e mai ok totusi*/
        Map<String, Echipa> echipe = new HashMap<>();

        String query = "SELECT * FROM `Echipe`";
        boolean bFoundTeams = false;

        try {
            try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {

                // isBeforeFirst() verifica daca s-a gasit cel putin un rezultat in query
                if(rs.isBeforeFirst()) {
//                    bFoundTeams = true;
                    while (rs.next()) {
//                        System.out.println("ID Echipa: " + rs.getInt("id"));
//                        System.out.println("Echipa: " + rs.getString("echipa"));
//                        System.out.println("Puncte: " + rs.getInt("puncte"));

                        echipe.put(rs.getString("echipa"), new Echipa(rs.getString("echipa"),
                                rs.getInt("puncte")));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Daca nu sunt gasite echipe in baza de date, introducem unele de la tastatura
        // TODO: si mai apoi in baza de date
        if(!bFoundTeams) {
            Scanner scanner = new Scanner(System.in);
            int nrDeEchipe = scanner.nextInt();
            scanner.nextLine();
            for (int i = 0; i < nrDeEchipe; i++) {
                String nume = scanner.nextLine();
                int puncte = scanner.nextInt();
                scanner.nextLine();
                echipe.put(nume, new Echipa(nume, puncte));
            }
            Meciuri meciuri = new Meciuri(nrDeEchipe, echipe);
            meciuri.setScore(nrDeEchipe, echipe);
        }


    }

    public static void afisareEchipe(Map<String, Echipa> echipe){
        for(Echipa element : echipe.values()){
            System.out.println(element.getNume());
            System.out.println(element.getLocatia());
        }
    }

    public static void createTables(Connection conn) {
        String query = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_NAME_ECHIPE +
                "(`ID` INT NOT NULL AUTO_INCREMENT," +
                "`Echipa` VARCHAR(32)," +
                "`Locatie` VARCHAR(32)," +
                "`Puncte` INT NOT NULL," +
                "PRIMARY KEY(ID));";

        try {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        query = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_NAME_STATS +
                "(`ID` INT NOT NULL AUTO_INCREMENT," +
                "`ID_Echipa` INT NOT NULL," +
                "`meciuri_jucate` INT NOT NULL," +
                "`victorii` INT NOT NULL," +
                "`infrangeri` INT NOT NULL," +
                "`egaluri` INT NOT NULL," +
                "`goluri_marcate` INT NOT NULL," +
                "`goluri_primite` INT NOT NULL," +
                "PRIMARY KEY(ID), " +
                "FOREIGN KEY (ID_Echipa) REFERENCES " + Constants.TABLE_NAME_ECHIPE +  "(ID) ON DELETE CASCADE);";

        if(Constants.DEBUG)
            System.out.println(query);

        try {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Verificare in tabela informatio_schema daca triggerele create exista pentru a nu se crea un conflict*/
    private static boolean triggerExists(Connection conn, String triggerName) throws SQLException {
        String query = "SELECT TRIGGER_NAME FROM information_schema.TRIGGERS WHERE TRIGGER_NAME = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, triggerName);
            try (ResultSet rs = pstmt.executeQuery()) {
                return !rs.next();
            }
        }
    }

    /* Triggere pentru a insera / sterge randurile din tabelul Statistici in cazul in care o echipa
    * este adaugata / stearsa din tabelul Echipe*/
    public static void createTriggers(Connection conn) {

        // \s = whitespace
        String createInsertTrigger = """
              CREATE TRIGGER\s""" + Constants.INSERT_TRIGGER + """
               AFTER INSERT ON Echipe
                FOR EACH ROW
                BEGIN\s
                INSERT INTO Statistici (ID_Echipa, meciuri_jucate, victorii, infrangeri, egaluri, goluri_marcate, goluri_primite)
                VALUES (NEW.ID, 0, 0, 0, 0, 0, 0);
                END;
        """;
        if(Constants.DEBUG)
            System.out.println(createInsertTrigger);

        try (Statement stmt = conn.createStatement()) {
            if (triggerExists(conn, Constants.INSERT_TRIGGER)) {
                stmt.executeUpdate(createInsertTrigger);
                if(Constants.DEBUG)
                    System.out.println("Inser trigger created.");
            }
            else {
                if(Constants.DEBUG)
                    System.out.println("Insert trigger already exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


            String createDeleteTrigger = """
               CREATE TRIGGER\s""" + Constants.DELETE_TRIGGER + """
               AFTER DELETE ON Echipe
               FOR EACH ROW
               BEGIN
               DELETE FROM Statistici WHERE ID_Echipa = OLD.ID;
                END;
            """;


        try (Statement stmt = conn.createStatement()) {
            if (triggerExists(conn, Constants.DELETE_TRIGGER)) {
                stmt.executeUpdate(createDeleteTrigger);
                if(Constants.DEBUG)
                    System.out.println("Delete trigger created.");
            }
            else {
                if(Constants.DEBUG)
                    System.out.println("Delete trigger already exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}