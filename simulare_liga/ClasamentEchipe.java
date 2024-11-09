package simulare_liga;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.util.Map;

import static simulare_liga.Main.getEchipe;

public class ClasamentEchipe extends JFrame{
    private JTable Clasament;
    private JScrollPane scrollPane1;
    private JTable Live;

    private final Map<String, Echipa> echipeInstance = getEchipe();

    ClasamentEchipe() {
        setTitle("Simulare Liga 1 - Proiect Java");

        int rowCount = echipeInstance.size();
        String[][] data = new String[rowCount][2];

        int i = 0;
        for (Echipa echipa : echipeInstance.values()) {
            data[i][0] = echipa.getNume();
            data[i][1] = String.valueOf(echipa.getPuncte());
            i++;
        }

        String[] clasamentColumns = { "Echipa", "Puncte" };

        Clasament = new JTable(data, clasamentColumns);
        Clasament.setBounds(30, 40, 200, 300);

        JScrollPane clasamentSP = new JScrollPane(Clasament);
        String[] liveColumns = { "Gazda", "Oaspete", "Scor" };

        String[][] live = new String[][]{
            {"Steaua", "Dinamo", "0-0"}
        };

        Live = new JTable(live, liveColumns);
        Live.setBounds(280, 40, 200, 300);
        JScrollPane liveSP = new JScrollPane(Live);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        panel.add(clasamentSP);
        panel.add(liveSP);

        add(panel);

        setSize(500, 200);
        setVisible(true);
    }

    /*public static void main(String[] args) {
        new ClasamentEchipe();
    }*/
}
