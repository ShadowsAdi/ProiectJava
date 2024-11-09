package simulare_liga;

public class Echipa{

    private int puncte;
    private String nume;
    private String locatia;
    private int goluriPrimite;
    private int goluriDate;
    Echipa(String nume, String locatia){
        this.nume = nume;
        this.locatia = locatia;
    }
    Echipa(String nume, int puncte){
        this.nume = nume;
        this.puncte = puncte;
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

    public int getGoluriDate() {
        return goluriDate;
    }

    public void setGoluriDate(int goluriDate) {
        this.goluriDate = goluriDate;
    }

    public int getGoluriPrimite() {
        return goluriPrimite;
    }

    public void setGoluriPrimite(int goluriPrimite) {
        this.goluriPrimite = goluriPrimite;
    }

    public String getLocatia() {
        return locatia;
    }

    public void setLocatia(String locatia) {
        this.locatia = locatia;
    }
}