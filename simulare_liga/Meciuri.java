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

                // buna pentru statistica adica cate goluri a dat si primit fiecare echipa
                echipa1.setGoluriDate(echipa1.getGoluriDate() + goluriEchipa1);
                echipa1.setGoluriPrimite(echipa1.getGoluriPrimite() + goluriEchipa2);
                echipa2.setGoluriDate(echipa2.getGoluriDate() + goluriEchipa2);
                echipa2.setGoluriPrimite(echipa2.getGoluriPrimite() + goluriEchipa1);

                // folosit pentru a afisa in timp real scorul la fiecare meci
                nrDeMeciuri++;
                // pt fiecare meci se retine cate goluri a dat echipa1 si echipa2
                PairMeci meci = new PairMeci(echipa1, echipa2);
                meciuriMap.put(nrDeMeciuri, meci);
                meci.setGoluriDateEC1(goluriEchipa1);
                meci.setGoluriDateEC2(goluriEchipa2);
                // calcularea punctelor in functie de goluri
                if (goluriEchipa1 > goluriEchipa2) {
                    echipa1.setPuncte(3);
                    echipa1.setVictorii(echipa1.getVictorii()+1);
                    echipa2.setInfrangeri(echipa2.getInfrangeri()+1);
                } else if(goluriEchipa1 < goluriEchipa2) {
                    echipa2.setPuncte(3);
                    echipa2.setVictorii(echipa2.getVictorii()+1);
                    echipa1.setInfrangeri(echipa1.getInfrangeri()+1);
                }else{
                    echipa1.setPuncte(1);
                    echipa2.setPuncte(1);
                    echipa1.setEgaluri(echipa1.getEgaluri()+1);
                    echipa2.setEgaluri(echipa2.getEgaluri()+1);
                }
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