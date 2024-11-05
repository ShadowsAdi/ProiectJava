package simulare_liga;

import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.ArrayList;

public class Meciuri{

    int nrDeEchipe;
    ArrayList<Echipa> echipe;

    public int getNrDeEchipe() {
        return nrDeEchipe;
    }

    public void setNrDeEchipe(int nrDeEchipe) {
        this.nrDeEchipe = nrDeEchipe;
    }

    public Meciuri(int nrEchipe, ArrayList<Echipa> aEchipe){
        this.nrDeEchipe = nrEchipe;
        this.echipe = aEchipe;
    }
    Scanner scanner = new Scanner(System.in);
    public boolean setScore() {
        for(int i=1; i < this.nrDeEchipe; i++)
        {
            for(int j=i+1; j <= nrDeEchipe;j++){
                System.out.println("Introduceti scorul meciului dintre echipa " + i + " si echipa " + j);
                int goluriEchipa1 = scanner.nextInt();
                int goluriEchipa2 = scanner.nextInt();
                goluriEchipa1 = echipe.get(i-1).getGoluri();
                goluriEchipa2 = echipe.get(j-1).getGoluri();

                echipe.get(i-1).setGoluri(goluriEchipa1);
                echipe.get(j-1).setGoluri(goluriEchipa2);
                Collections.sort(echipe, new Comparator<Echipa>() {
                    @Override
                    public int compare(Echipa o1, Echipa o2) {
                        if (o1.getPuncte() != o2.getPuncte()) {
                            return o2.getPuncte() - o1.getPuncte();
                        }

                        // Nu inteleg ce trebuia sa fie aici
                    /*if (o1.goluriDate - o1.goluriPrimite != o2.goluriDate - o2.goluriPrimite) {
                        return (o2.goluriDate - o2.goluriPrimite) - (o1.goluriDate - o1.goluriPrimite);
                    }
                    return o2.goluriDate - o1.goluriDate;*/
                        return 0;
                    }
                });
            }
        }

        return true;
    }
}