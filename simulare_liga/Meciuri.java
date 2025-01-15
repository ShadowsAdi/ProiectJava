package simulare_liga;

import java.util.*;

public class Meciuri {
    private static int nrDeEchipe;
    private static Map<String, Echipa> Echipe;
    
    public static final Map<Integer, PairMeci> meciuriMap = new HashMap<>();

    public static Map<Integer, PairMeci> getMeciuriMap() {
        return meciuriMap;
    }

    public Meciuri(int nrEchipe, Map<String, Echipa> Echipe) {
        nrDeEchipe = nrEchipe;
        Meciuri.Echipe = Echipe;
    }

    public void setScoreRandom() throws InterruptedException {
        List<String> listaNumeEchipe = new ArrayList<>(Echipe.keySet());

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

                Random random = new Random();
                int goluriEchipa1 = random.nextInt((3) + 1);
                int goluriEchipa2 = random.nextInt((3) + 1);

                System.out.println("Intre " + numeEchipa1 + " si " + numeEchipa2 + " s-a terminat cu scorul: " + goluriEchipa1 + " - " + goluriEchipa2);

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

                Thread.sleep(1000);
            }
        }

        afisareEchipe(Echipe);
    }

    public static void afisareEchipe(Map<String, Echipa> echipe){
        for(Echipa element : echipe.values()){
            System.out.println(element.getNume());
        }
    }
}