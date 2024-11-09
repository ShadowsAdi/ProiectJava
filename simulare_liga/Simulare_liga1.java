package simulare_liga;

import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

public class Simulare_liga1 {
    public static void main(String[] args) {
        //echipele introduse de la tastatura
        Database dObj = Database.getInstance();

        Connection conn = dObj.getConnection();

        // TODO: Tabele diferite pentru statistici ( folosire cheie primara ID )
        String query = "SELECT * FROM echipe";

        boolean bFoundTeams = false;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // rs.isBeforeFirst() verifica daca s-a gasit cel putin un rezultat in query
            if(rs.isBeforeFirst()) {
                bFoundTeams = true;
                while (rs.next()) {
                    System.out.println("ID Echipa: " + rs.getString("id"));
                    System.out.println("Echipa: " + rs.getString("echipa"));
                    System.out.println("Puncte: " + rs.getString("puncte"));
                    System.out.println("Meciuri: " + rs.getString("meciuri_jucate"));
                    System.out.println("Win: " + rs.getString("victorii"));
                    System.out.println("Lose: " + rs.getString("infrangeri"));
                    System.out.println("Draw: " + rs.getString("egal"));
                    System.out.println("Goluri marcate: " + rs.getString("goluri_marcate"));
                    System.out.println("Goluri primite: " + rs.getString("goluri_primite"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Daca nu sunt gasite echipe in baza de date, introducem unele de la tastatura
        // TODO: si mai apoi in baza de date
        if(!bFoundTeams) {
            Scanner scanner = new Scanner(System.in);
            ArrayList<Echipa> echipe = new ArrayList<>();
            int nrDeEchipe = scanner.nextInt();
            scanner.nextLine();
            for (int i = 0; i < nrDeEchipe; i++) {
                String nume = scanner.nextLine();
                String locatia = scanner.nextLine();
                scanner.nextLine();
                echipe.add(new Echipa(nume, locatia));
            }
            new Meciuri(nrDeEchipe, echipe);
            for (Echipa e : echipe) {
                System.out.println(e.getNume());
            }
        }
    }
}