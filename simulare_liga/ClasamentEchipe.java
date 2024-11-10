package simulare_liga;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

import static simulare_liga.Main.getEchipe;

public class ClasamentEchipe extends JFrame{
    private JTable Clasament;
    private JTable Live;
    private JScrollPane ScrollTableClasament;
    private JScrollPane ScrollTableLive;
    private JPanel AppPanel;

    private static JDialog ClasamentDialog;

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

        ClasamentDialog = new JDialog();
        ClasamentDialog.setTitle("Informatii Echipa");
        ClasamentDialog.getRootPane().setBorder(new LineBorder(Color.GRAY, 3));
        ClasamentDialog.setUndecorated(true);
        ClasamentDialog.setSize(200, 200);
    }

    private static void addTableClickListener(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());
                if (row >= 0 && column >= 0) {
                    Object colName = table.getColumnName(column);
                    // Daca nu apasam pe un element din coloanele "Echipa", "Gazda" sau "Oaspete", nu afisam dialogul
                    if(!(colName.equals("Echipa") || colName.equals("Gazda") || colName.equals("Oaspete"))) {
                        return;
                    }

                    ClasamentDialog.getContentPane().removeAll();
                    JLabel dialogText = getDialogText();
                    ClasamentDialog.getContentPane().add(dialogText);
                    ClasamentDialog.pack();
                    ClasamentDialog.setLocation(e.getLocationOnScreen());
                    ClasamentDialog.setVisible(true);
                } else {
                    if (ClasamentDialog != null) {
                        ClasamentDialog.setVisible(false);
                    }
                }
            }
        });
    }

    private static JLabel getDialogText() {
        JLabel dialogText = new JLabel(
                            "<html><div style='color:blue; font-size:12px;'>Nume Echipa: test</div><br>" +
                            "<div style='color:green; font-size:12px;'>Puncte: 7</div><br>" +
                            "<div style='color:red; font-size:12px;'>Goluri date: 3</div><br>" +
                            "<div style='color:purple; font-size:12px;'>Goluri primite: 2</div><br>" +
                            "<div style='color:orange; font-size:12px;'>Locatia: test</div></html>"
        );
        dialogText.setFont(new Font("Arial", Font.PLAIN, 12));
        return dialogText;
    }

    /*public static void main(String[] args) {
        new ClasamentEchipe();
    }*/
}
