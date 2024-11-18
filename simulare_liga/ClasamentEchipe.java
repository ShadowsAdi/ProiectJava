package simulare_liga;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.List;

import static simulare_liga.Main.getEchipe;
import static simulare_liga.Main.getMeciuri;

public class ClasamentEchipe extends JFrame{
    private JTable Clasament;
    private JTable Live;
    private JScrollPane ScrollTableClasament;
    private JScrollPane ScrollTableLive;
    private JPanel AppPanel;

    private static JDialog ClasamentDialog;

    private static Map<String, Echipa> echipeInstance = null;
    private static Meciuri meciuriInstance = null;

    ClasamentEchipe() {
        setTitle("Simulare Liga 1 - Proiect Java");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(AppPanel);

        updateTables();

        startAutoRefresh();

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

    private void updateTables() {
        echipeInstance = getEchipe();
        int rowCount = echipeInstance.size();
        String[][] data = new String[rowCount][3];

        int i = 0;
        for (Echipa echipa : echipeInstance.values()) {
            data[i][0] = String.valueOf(i);
            data[i][1] = echipa.getNume();
            data[i][2] = String.valueOf(echipa.getPuncte());
            i++;
        }

        String[] clasamentColumns = {"Loc", "Echipa", "Puncte"};
        DefaultTableModel clasamentModel = new DefaultTableModel(data, clasamentColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Clasament.setModel(clasamentModel);

        String[] liveColumns = {"Meci ID", "Gazda", "Oaspete", "Scor"};

        meciuriInstance = getMeciuri();
        // TODO: Hashmap pentru indentificarea meciurilor din retur.
        // daca key-ul + valoarea exista, meciul este in retur .
        // EX: 1 + 2 = tur, 2 + 1 = retur, sau poate alta metoda aparent.

        // Aici o sa fie si comparatorul.
        String[][] dataLive = new String[0][4];
        if (meciuriInstance != null) {
            Map<Integer, PairMeci> meciuriMap = meciuriInstance.getMeciuriMap();

            rowCount = meciuriMap.size();

            System.out.println("rowCount: " + rowCount);
            dataLive = new String[rowCount][4];

            i = 0;
            for (Integer key : meciuriMap.keySet()) {
                PairMeci meci = meciuriMap.get(key);
                dataLive[i][0] = String.valueOf(key);
                dataLive[i][1] = meci.getEc1().getNume();
                dataLive[i][2] = meci.getEc2().getNume();
                dataLive[i][3] = meci.getEc1().getGoluriDate() + "-" + meci.getEc2().getGoluriDate();
                i++;
            }
        }

        DefaultTableModel liveModel = new DefaultTableModel(dataLive, liveColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Live.setModel(liveModel);
    }

    private void startAutoRefresh() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (;;) {
                    Thread.sleep(5000);
                    publish();
                }
            }

            @Override
            protected void process(List<Void> chunks) {
                try {
                    updateTables();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void addTableClickListener(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());
                Object value = table.getValueAt(row, column);

                if (row >= 0 && column >= 0) {
                    if(echipeInstance.containsKey(value) || /*meciuriInstance.getMeciuriMap().get(row)
                            .getEc1().getNume().contains(value.toString())*/ table.equals(Live)) {
                        if(table.equals(Clasament)) {
                           ClasamentDialog.getContentPane().removeAll();
                            JLabel dialogText = getDialogText(false, row, table.getColumnName(column));
                            ClasamentDialog.getContentPane().add(dialogText);
                            ClasamentDialog.pack();
                            ClasamentDialog.setLocation(e.getLocationOnScreen());
                            ClasamentDialog.setVisible(true);
                        }
                        else if(table.equals(Live)) {
                            ClasamentDialog.getContentPane().removeAll();
                            JLabel dialogText = getDialogText(true, row, table.getColumnName(column));
                            ClasamentDialog.getContentPane().add(dialogText);
                            ClasamentDialog.pack();
                            ClasamentDialog.setLocation(e.getLocationOnScreen());
                            ClasamentDialog.setVisible(true);
                        }
                    }
                }
                else
                {
                    if (ClasamentDialog != null) {
                        ClasamentDialog.setVisible(false);
                    }
                }
            }
        });
    }

    private static JLabel getDialogText(boolean isLive, int row, String column) {
        JLabel dialogText = new JLabel();

        String[][] data;
        if (!isLive) {
            data = new String[echipeInstance.size()][2];
            int i = 0;
            for (Echipa echipa : echipeInstance.values()) {
                data[i][0] = echipa.getNume();
                data[i][1] = String.valueOf(echipa.getPuncte());
                i++;
            }

            dialogText.setText("<html><div style='color:blue; font-size:12px;'>Nume Echipa: "+ data[row][0] + "</div><br>" +
                    "<div style='color:green; font-size:12px;'>Puncte: " + data[row][1] + "</div><br></html>");
        }
        else
        {
            Map<Integer, PairMeci> meciuriMap = meciuriInstance.getMeciuriMap();
            data = new String[meciuriMap.size()][5];
            int i = 0;
            for (Integer key : meciuriMap.keySet()) {
            PairMeci meci = meciuriMap.get(key);
            Echipa echipa = column.equals("Gazda") ? meci.getEc1() : meci.getEc2();
            data[i][0] = echipa.getNume();
            data[i][1] = String.valueOf(echipa.getPuncte());
            data[i][2] = String.valueOf(echipa.getGoluriDate());
            data[i][3] = String.valueOf(echipa.getGoluriPrimite());
            data[i][4] = echipa.getLocatia();
            i++;
        }

        dialogText.setText("<html><div style='color:blue; font-size:12px;'>Nume Echipa: "+ data[row][0] + "</div><br>" +
                    "<div style='color:green; font-size:12px;'>Puncte: " + data[row][1] + "</div><br>" +
                    "<div style='color:red; font-size:12px;'>Goluri date: " + data[row][2] + "</div><br>" +
                    "<div style='color:purple; font-size:12px;'>Goluri primite: " + data[row][3] + "</div><br>" +
                    "<div style='color:orange; font-size:12px;'>Locatia: "+ data[row][4] +"</div></html>");
        }

        dialogText.setFont(new Font("Arial", Font.PLAIN, 12));
        return dialogText;
    }
}
