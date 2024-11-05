package simulare_liga;

public class Echipa{

    private int puncte;
    private String nume;
    private String locatia;
    private int goluri;
    Echipa(String nume, String locatia){
        this.nume = nume;
        this.locatia = locatia;
    }
    Echipa(String nume, int goluri){
        this.nume = nume;
        this.goluri = goluri;
    }

    public int getPuncte() {
        return puncte;
    }

    public void setPuncte(int puncte) {
        this.puncte = puncte;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getGoluri() {
        return goluri;
    }

    public void setGoluri(int goluri) {
        this.goluri = goluri;
    }

    public String getLocatia() {
        return locatia;
    }

    public void setLocatia(String locatia) {
        this.locatia = locatia;
    }
}