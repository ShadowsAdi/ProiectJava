package simulare_liga;

import java.util.Comparator;

public class EchipaComparator implements Comparator<Echipa> {
    @Override
    public int compare(Echipa e1, Echipa e2) {
        //just an example de comparator, inca nu stiu care sunt criteriile de departajare

        // int numeComparison = e1.getNume().compareTo(e2.getNume());
        // if (numeComparison != 0) {
        //     return numeComparison;
        // }
        // return e1.getLocatia().compareTo(e2.getLocatia());

        return 0;
    }
}