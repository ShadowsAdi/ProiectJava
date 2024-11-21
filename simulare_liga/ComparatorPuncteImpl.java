package simulare_liga;

public class ComparatorPuncteImpl implements ComparatorPuncte {
    @Override
    public int compare(Echipa o1, Echipa o2) {
         // 1. Compară punctele (descrescator)
         if (o1.getPuncte() != o2.getPuncte()) {
            return o2.getPuncte() - o1.getPuncte();
         }

         // 2. Compara golaverajul (descrescator)
         int golaveraj1 = o1.getGoluriDate() - o1.getGoluriPrimite();
         int golaveraj2 = o2.getGoluriDate() - o2.getGoluriPrimite();
         if (golaveraj1 != golaveraj2) {
            return golaveraj2 - golaveraj1;
         }

        // 3. Compara numarul total de goluri marcate (descrescator)
        if (o1.getGoluriDate() != o2.getGoluriDate()) {
            return o2.getGoluriDate() - o1.getGoluriDate();
        }

        // Daca toate criteriile sunt egale
        return 0;
    }
}
