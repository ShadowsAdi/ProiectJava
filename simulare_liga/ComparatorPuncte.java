package simulare_liga;

import java.util.Comparator;

public interface ComparatorPuncte extends Comparator<Echipa> {
    @Override
    int compare(Echipa o1, Echipa o2);
}