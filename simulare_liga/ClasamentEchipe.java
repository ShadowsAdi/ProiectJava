package simulare_liga;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.Map;

import static simulare_liga.Main.getEchipe;

public class ClasamentEchipe extends JFrame{
    private JTable Clasament;
    private JTable Live;
    private JScrollPane ScrollTableLive;
    private JScrollPane ScrollTableClasament;
    private JPanel AppPanel;

    private final Map<String, Echipa> echipeInstance = getEchipe();

    ClasamentEchipe() {
        setTitle("Simulare Liga 1 - Proiect Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(AppPanel);

        int rowCount = echipeInstance.size();
        String[][] data = new String[rowCount][2];

        int i = 0;
        for (Echipa echipa : echipeInstance.values()) {
            data[i][0] = echipa.getNume();
            data[i][1] = String.valueOf(echipa.getPuncte());
            i++;
        }

        String[] clasamentColumns = { "Echipa", "Puncte" };
        DefaultTableModel clasamentModel = new DefaultTableModel(data, clasamentColumns) {
             @Override
             public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Clasament.setModel(clasamentModel);

        String[] liveColumns = { "Gazda", "Oaspete", "Scor" };

        String[][] live = new String[][]{
            {"Steaua", "Dinamo", "0-0"}
        };

        DefaultTableModel liveModel = new DefaultTableModel(live, liveColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Live.setModel(liveModel);

        ScrollTableClasament.setViewportView(Clasament);
        ScrollTableLive.setViewportView(Live);

        addTableClickListener(Clasament);
        addTableClickListener(Live);

        pack();
        setVisible(true);
    }

    private static void addTableClickListener(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());
                if (row >= 0 && column >= 0) {
                    Object value = table.getValueAt(row, column);
                    System.out.println("Selectat:  " + value);
                }
            }
        });
    }

    /*public static void main(String[] args) {
        new ClasamentEchipe();
    }*/
}
