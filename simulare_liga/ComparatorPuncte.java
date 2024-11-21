package simulare_liga;

import java.util.Comparator;

// Interfata "ComparatorPuncte" extinde interfata "Comparator" si este folosita
// pentru a compara doua echipe in functie de punctele obtinute

// am facut asta doar ca sa folosim si altceva decat clase in proiect =)))
public interface ComparatorPuncte extends Comparator<Echipa> {
    @Override
    int compare(Echipa o1, Echipa o2);
}