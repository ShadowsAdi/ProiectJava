package simulare_liga;

import java.util.*;

public class Meciuri{

    int nrDeEchipe;
    Map<String, Echipa> echipe;

    public int getNrDeEchipe() {
        return nrDeEchipe;
    }

    public void setNrDeEchipe(int nrDeEchipe) {
        this.nrDeEchipe = nrDeEchipe;
    }

    public Meciuri(int nrEchipe, Map<String, Echipa> Echipe){
        this.nrDeEchipe = nrEchipe;
        this.echipe = Echipe;
    }

    public void setScore(int nrDeEchipe, Map<String, Echipa> Echipe) {
        Scanner scanner = new Scanner(System.in);
        List<Echipa> listaEchipe = new ArrayList<>(echipe.values());
        List<String> listaNumeEchipe = new ArrayList<>(echipe.keySet()); // listă cu numele echipelor
        for(int i=1; i < this.nrDeEchipe; i++)
        {
            for(int j=i+1; j <= nrDeEchipe;j++){

                String numeEchipa1 = listaNumeEchipe.get(i);
                String numeEchipa2 = listaNumeEchipe.get(j);

                Echipa echipa1 = echipe.get(numeEchipa1);
                Echipa echipa2 = echipe.get(numeEchipa2);

                System.out.println("Introduceti scorul meciului dintre echipa " + i + " si echipa " + j);
                int goluriEchipa1 = scanner.nextInt();
                int goluriEchipa2 = scanner.nextInt();

                echipa1.setGoluriDate(echipa1.getGoluriDate() + goluriEchipa1);
                echipa1.setGoluriPrimite(echipa1.getGoluriPrimite() + goluriEchipa2);
                echipa2.setGoluriDate(echipa2.getGoluriDate() + goluriEchipa2);
                echipa2.setGoluriPrimite(echipa2.getGoluriPrimite() + goluriEchipa1);
                listaEchipe.sort(new Comparator<Echipa>() {
                    @Override
                    public int compare(Echipa o1, Echipa o2) {
                        // 1. Compară punctele (descrescător)
                        if (o1.getPuncte() != o2.getPuncte()) {
                            return o2.getPuncte() - o1.getPuncte();
                        }

                        // 2. Compară golaverajul (descrescător)
                        int golaveraj1 = o1.getGoluriDate() - o1.getGoluriPrimite();
                        int golaveraj2 = o2.getGoluriDate() - o2.getGoluriPrimite();
                        if (golaveraj1 != golaveraj2) {
                            return golaveraj2 - golaveraj1; // Echipa cu golaveraj mai mare este plasată mai sus
                        }

                        // 3. Compară numărul total de goluri marcate (descrescător)
                        if (o1.getGoluriDate() != o2.getGoluriDate()) {
                            return o2.getGoluriDate() - o1.getGoluriDate();
                        }

                        // Dacă toate criteriile sunt egale, returnează 0 (echipele sunt egale în clasament)
                        return 0;
                    }
                });
            }
        }
        afisareEchipe(echipe);
    }
    public static void afisareEchipe(Map<String, Echipa> echipe){
        for(Echipa element : echipe.values()){
            System.out.println(element.getNume());
        }
    }
}