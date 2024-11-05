package simulare_liga;

import java.util.ArrayList;
import java.util.Scanner;

public class Simulare_liga1 {
    public static void main(String[] args) {
        //echipele introduse de la tastatura
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