package simulare_liga;

import java.util.*;

public class Meciuri {
    private static int nrDeEchipe;
    private static Map<String, Echipa> Echipe;

    public static final Map<Integer, PairMeci> meciuriMap = new HashMap<>();

    public Map<Integer, PairMeci> getMeciuriMap() {
        return meciuriMap;
    }

    public Meciuri(int nrEchipe, Map<String, Echipa> Echipe) {
        this.nrDeEchipe = nrEchipe;
        this.Echipe = Echipe;
    }

    public void setScore() {
        Scanner scanner = new Scanner(System.in);
        List<Echipa> listaEchipe = new ArrayList<>(Echipe.values());
        List<String> listaNumeEchipe = new ArrayList<>(Echipe.keySet());

        System.out.println("Size: " + listaNumeEchipe);

        System.out.println("Echipe: " + nrDeEchipe);
        int nrDeMeciuri = 0;
        for (int i = 0; i < nrDeEchipe; i++) {
            for (int j = 0; j < nrDeEchipe; j++) {
                if (i == j) {
                    continue;
                }
                String numeEchipa1 = listaNumeEchipe.get(i);
                String numeEchipa2 = listaNumeEchipe.get(j);

                Echipa echipa1 = Echipe.get(numeEchipa1);
                Echipa echipa2 = Echipe.get(numeEchipa2);

                System.out.println("Introduceti scorul pentru \"" + numeEchipa1 + "\" si \"" + numeEchipa2 + "\":");
                int goluriEchipa1 = scanner.nextInt();
                int goluriEchipa2 = scanner.nextInt();

                // trebuie gasita o alta abordare, asta adauga goluri la fiecare echipa overall, nu doar la meciul respectiv
                echipa1.setGoluriDate(echipa1.getGoluriDate() + goluriEchipa1);
                echipa1.setGoluriPrimite(echipa1.getGoluriPrimite() + goluriEchipa2);
                echipa2.setGoluriDate(echipa2.getGoluriDate() + goluriEchipa2);
                echipa2.setGoluriPrimite(echipa2.getGoluriPrimite() + goluriEchipa1);

                nrDeMeciuri++;
                meciuriMap.put(nrDeMeciuri, new PairMeci(echipa1, echipa2));
            }
        }

        System.out.println("Meciuri: " + meciuriMap.size());
        afisareEchipe(Echipe);
    }

    public void compareScores() {
        List<Echipa> listaEchipe = new ArrayList<>(Echipe.values());

        Collections.sort(listaEchipe, new ComparatorPuncteImpl());

        System.out.println("Meciuri: " + meciuriMap.size());
        afisareEchipe(Echipe);
    }

    public static void afisareEchipe(Map<String, Echipa> echipe){
        for(Echipa element : echipe.values()){
            System.out.println(element.getNume());
        }
    }
}