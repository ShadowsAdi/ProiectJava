package simulare_liga;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.sql.*;

//sa porneasca de aici gen aplicatia, good practice
public class Main {
     /* Cred ca un hashmap e mai ok totusi*/
    private static Map<String, Echipa> echipe = new HashMap<>();

    public static Map<String, Echipa> getEchipe() {
        return echipe;
    }

    private static Meciuri meciuri;

    public static Meciuri getMeciuri() {
        return meciuri;
    }

    private static int nrDeEchipe = 0;

    public static void main(String[] args) {
        // preluam conexiunea la baza de date din singleton-ul Databawse
        Connection conn = Database.getInstance().getConnection();

        // assert = daca conditia e falsa, programul da eroare.
        // echivalent cu:
        /*
        if(conn == null) {
            throw new AssertionError();
        }*/
        assert conn != null;

        // creem tabelele in baza de date
        createTables(conn);
        // creem triggerele pentru tabele pentru a insera/sterge randurile specifice din
        // Statistici in cazul in care se adauga/sterge o echipa
        createTriggers(conn);

        // preluam echipele din baza de date
        String query = "SELECT * FROM `Echipe`";
        // boolean pentru a verifica daca s-au gasit echipe in baza de date
        boolean bFoundTeams = false;
        try {
            // ResultSet.TYPE_SCROLL_INSENSITIVE = putem naviga inapoi si inainte in ResultSet folosind "cursorul"
            // ResultSet.CONCUR_READ_ONLY = ResultSet-ul nu poate fi modificat
            try (Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                     ResultSet rs = stmt.executeQuery(query)) {

                // daca s-a gasit cel putin un rezultat in query, ne pozitionam pe ultimul rand
                if (rs.last()) {
                    // preluam numarul de echipe din baza de date prin a muta cursorul pe ultimul rand ( inseamna ca am trecut prin toate
                    // randurile din rezultatul query-ului )
                    // si apoi preluam numarul randului pe care ne aflam
                    nrDeEchipe = rs.getRow();
                    // ne pozitionam pe primul rand pentru a citi datele din query de la inceput
                    rs.beforeFirst();
                }

                // isBeforeFirst() verifica daca s-a gasit cel putin un rezultat in query
                if(rs.isBeforeFirst()) {
                    // daca s-au gasit echipe in baza de date, setam bFoundTeams pe true
                    bFoundTeams = true;
                    // cat timp exista randuri in ResultSet, preluam datele si le introducem in HashMap-ul de echipe
                    while (rs.next()) {
                        // nu prea e relevant asta, totusi, daca in Constants, DEBUG = true, atunci afisam niste mesaje
                        if(Constants.DEBUG)
                        {
                            System.out.println("ID Echipa: " + rs.getInt("ID"));
                            System.out.println("Echipa: " + rs.getString("Echipa"));
                            System.out.println("Puncte: " + rs.getInt("Puncte"));
                            System.out.println("Locatie: " + rs.getString("Locatie"));
                        }

                        // preluam datele din ResultSet si le introducem in HashMap-ul de echipe
                        // primul parametru este numele echipei, al doilea parametru este numarul de puncte al echipei
                        Echipa echipa = new Echipa(rs.getString("echipa"), rs.getInt("puncte"));
                        // setam locatia echipei in obiectul Echipa
                        echipa.setLocatia(rs.getString("Locatie"));

                        // introducem echipa in HashMap-ul de echipe
                        // key = numele echipei, value = obiectul Echipa
                        echipe.put(rs.getString("echipa"), echipa);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // daca nu sunt gasite echipe in baza de date, introducem unele de la tastatura
        if(!bFoundTeams) {
            System.out.println("Introduceti numarul de echipe:");

            Scanner scanner = new Scanner(System.in);
            // preluam numarul de echipe de la tastatura pentru a sti cate echipe sa introducem de la tastatura
            nrDeEchipe = scanner.nextInt();

            System.out.println("Introduceti echipele participante in liga:");
            // folosim scanner.nextLine() pentru a citi newline-ul ramas in buffer dupa ce am citit numarul de echipe
            // cuz, la urmatoarea citire de la tastatura nu va stii daca sa astepte input de la tastatura sau sa foloseasca
            // NOTA: asta e problema doar cu nextInt(), nextDouble() si nextFloat()
            scanner.nextLine();

            for (int i = 0; i < nrDeEchipe; i++) {
                // Basic readin'
                System.out.println("Nume echipa " + (i + 1) + ":");
                String nume = scanner.nextLine();

                System.out.println("Puncte echipa " + (i + 1) + ":");
                int puncte = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Locatie echipa " + (i + 1) + ":");
                String locatie = scanner.nextLine();

                Echipa echipa = new Echipa(nume, puncte);
                echipa.setLocatia(locatie);

                // introducem echipa in HashMap-ul de echipe
                // key = numele echipei, value = obiectul Echipa
                echipe.put(nume, echipa);
            }

            // introducem echipele in baza de date iterand prin HashMap-ul de echipe
            for (Echipa echipa : echipe.values()) {
                // query-ul de inserare in baza de date
                query = "INSERT INTO `Echipe` (Echipa, Puncte, Locatie) VALUES (?, ?, ?)";
                try {
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {

                        // setam parametrii query-ului cu datele echipei curente
                        // inlocuind pe rand "?"-urile din query cu datele echipei
                        pstmt.setString(1, echipa.getNume());
                        pstmt.setInt(2, echipa.getPuncte());
                        pstmt.setString(3, echipa.getLocatia());
                        pstmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // instantiem un obiect de tip ClasamentEchipe pentru a interfata grafica a programului
        // se instantiaza inainte de setarea scorurilor pentru a vedea in timp real tabela de meciuri LIVE
        new ClasamentEchipe();

        // instantiem un obiect de tip Meciuri pentru a declara meciurilor si setarea scorului fiecarui meci
        meciuri = new Meciuri(nrDeEchipe, echipe);
        meciuri.setScore();

        // asta iar e irelevant, trebuie in incadrat la DEBUG
        afisareEchipe(echipe);
    }

    public static void afisareEchipe(Map<String, Echipa> echipe){
        for(Echipa echipa : echipe.values()){
            System.out.println(echipa.getNume());
            System.out.println(echipa.getPuncte());
        }
    }

    // functia pentru a crea tabelele in baza de date
    public static void createTables(Connection conn) {

        // nuj ce pot explica aici tbh
        String query = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_NAME_ECHIPE +
                "(`ID` INT NOT NULL AUTO_INCREMENT," +
                "`Echipa` VARCHAR(32)," +
                "`Locatie` VARCHAR(32)," +
                "`Puncte` INT NOT NULL DEFAULT(0)," +
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
                "FOREIGN KEY (ID_Echipa) REFERENCES " + Constants.TABLE_NAME_ECHIPE + "(ID) ON DELETE CASCADE);";

        if(Constants.DEBUG)
            System.out.println(query);

        try {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        query = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_NAME_MECIURI +
                "(`ID` INT NOT NULL AUTO_INCREMENT," +
                "`ID_Gazda` INT NOT NULL," +
                "`ID_Oaspete` INT NOT NULL," +
                "`Scor_gazda` INT NOT NULL," +
                "`Scor_oapete` INT NOT NULL," +
                "PRIMARY KEY(ID)," +
                "FOREIGN KEY (ID_Gazda) REFERENCES " + Constants.TABLE_NAME_ECHIPE + "(ID)," +
                "FOREIGN KEY (ID_Oaspete) REFERENCES " + Constants.TABLE_NAME_ECHIPE + "(ID));";

        try {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Verificare in tabela information_schema daca triggerele create exista pentru a nu se crea un conflict*/
    private static boolean triggerExists(Connection conn, String triggerName) throws SQLException {
        // asta trebuie aratat, nu pot explica mai multe
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

        // trigger pentru a insera un rand in tabelul Statistici in cazul in care o echipa este adaugata in tabelul Echipe
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

        // trigger pentru a sterge un rand din tabelul Statistici in cazul in care o echipa este stearsa din tabelul Echipe
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