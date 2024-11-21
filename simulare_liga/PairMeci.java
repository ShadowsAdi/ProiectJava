package simulare_liga;

// clasa "PairMeci" este folosita pentru a retine perechile de echipe care se intalnesc in cadrul unui meci
// folosita ca si value in HashMap-ul "meciuriMap" din clasa "Meciuri"
public class PairMeci {
    private Echipa ec1;
    private Echipa ec2;

    public PairMeci(Echipa ec1, Echipa ec2) {
        this.ec1 = ec1;
        this.ec2 = ec2;
    }

    public Echipa getEc1() {
        return ec1;
    }

    public void setEc1(Echipa ec1) {
        this.ec1 = ec1;
    }

    public Echipa getEc2() {
        return ec2;
    }

    public void setEc2(Echipa ec2) {
        this.ec2 = ec2;
    }
}