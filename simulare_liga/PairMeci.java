package simulare_liga;

// clasa "PairMeci" este folosita pentru a retine perechile de echipe care se intalnesc in cadrul unui meci
// folosita ca si value in HashMap-ul "meciuriMap" din clasa "Meciuri"
public class PairMeci {
    private Echipa ec1;
    private Echipa ec2;
    public int GoluriDateEC1;
    public int GoluriDateEC2;

    public PairMeci(Echipa ec1, Echipa ec2) {
        this.ec1 = ec1;
        this.ec2 = ec2;
    }

    public Echipa getEc1() {
        return ec1;
    }

    public Echipa getEc2() {
        return ec2;
    }

    public void setGoluriDateEC1(int goluri){
        this.GoluriDateEC1 = goluri;
    }

    public int getGoluriDateEC1(){
        return GoluriDateEC1;
    }

    public void setGoluriDateEC2(int goluri){
        this.GoluriDateEC2 = goluri;
    }

    public int getGoluriDateEC2(){
        return GoluriDateEC2;
    }

}