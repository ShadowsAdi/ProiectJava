package simulare_liga;

public class Echipa{

    private int puncte;
    private String nume;
    private String locatia;
    private int goluriPrimite;
    private int goluriDate;
    private int victorii;
    private int egaluri;
    private int infrangeri;

    Echipa(String nume, String locatie){
        this.nume = nume;
        this.locatia = locatie;
    }

    public int getPuncte() {
        return puncte;
    }

    public void setPuncte(int puncte) {
        this.puncte = this.puncte + puncte;
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

    public void setVictorii(){
        this.victorii = victorii + 1;
    }

    public int getVictorii(){
        return victorii;
    }

    public void setEgaluri(){
        this.egaluri = egaluri + 1;
    }

    public int getEgaluri(){
        return egaluri;
    }

    public void setInfrangeri(){
        this.infrangeri = infrangeri + 1;
    }

    public int getInfrangeri(){
        return infrangeri;
    }

}