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

    // ( 1 ) de aici
    private JTable Clasament;
    private JTable Live;
    private JScrollPane ScrollTableClasament;
    private JScrollPane ScrollTableLive;
    private JPanel AppPanel;
    private static JDialog ClasamentDialog;
    // ( 1 ) pana aici
    // sunt elementele grafice ale aplicatiei definite in Swing form designer

    // aici se stocheaza HashMap-urile echipelor si meciurilor in desfasurare
    private static Map<String, Echipa> echipeInstance = null;
    private static Meciuri meciuriInstance = null;

    // in clasa Main, funtia main, se apeleaza constructorl acestei clase pentru a porni interfata grafica
    ClasamentEchipe() {
        setTitle("Simulare Liga 1 - Proiect Java");
        // daca se inchide fereastra grafica, se inchide si aplicatia din rulare
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // se seteaza dimensiunea ferestrei
        setContentPane(AppPanel);

        // se apeleaza functia care construieste si actualizeaza datele din tabele
        updateTables();

        // se apeleaza functia care actualizeaza datele din tabele la fiecare 5 secunde ( SwingWorker == Thread / Runnable kind of )
        startAutoRefresh();

        // se adauga scroll la tabele in cazul in care elementele depasesc dimensiunea ferestrei
        ScrollTableClasament.setViewportView(Clasament);
        ScrollTableLive.setViewportView(Live);

        // se adauga un event listener pentru fiecare tabel, care afiseaza informatii despre echipa selectata la click
        addTableClickListener(Clasament);
        addTableClickListener(Live);

        // se adauga toate elementele definite la fereastra principala si se afiseaza fereastra
        pack();
        setVisible(true);

        // se initializeaza fereastra de dialog care afiseaza informatii despre echipa selectata din orice tabel
        ClasamentDialog = new JDialog();
        ClasamentDialog.setTitle("Informatii Echipa");
        // se seteaza un border pentru fereastra de dialog
        ClasamentDialog.getRootPane().setBorder(new LineBorder(Color.GRAY, 3));
        // se seteaza fereastra de dialog sa nu aiba bara de titlu ( minimize, maximize, close si iconita aia java )
        ClasamentDialog.setUndecorated(true);

        ClasamentDialog.setSize(200, 200);
    }

    // functia care actualizeaza datele din tabele
    private void updateTables() {
        // preluam HashMap-urile echipelor din clasa Main.main
        echipeInstance = getEchipe();

        // numarul de randuri din tabelul Clasament va fi egal cu numarul de echipe din HashMap
        int rowCount = echipeInstance.size();
        // se initializeaza un array bidimensional de stringuri pentru a stoca datele din HashMap
        // rowCount = numarul de echipe, 3 coloane: Loc, Echipa, Puncte
        String[][] data = new String[rowCount][3];

        int i = 0;
        // se parcurge HashMap-ul si se adauga datele in array-ul bidimensional
        for (Echipa echipa : echipeInstance.values()) {
            // prima coloana = indexul echipei in HashMap
            data[i][0] = String.valueOf(i);
            // a doua coloana = numele echipei
            data[i][1] = echipa.getNume();
            // a treia coloana = punctele echipei
            data[i][2] = String.valueOf(echipa.getPuncte());
            i++;
        }

        // se creeaza un model de tabel cu datele din array-ul bidimensional
        String[] clasamentColumns = {"Loc", "Echipa", "Puncte"};
        // PARAMETRII: DefaultTableModel( datele din randuri, coloane )
        DefaultTableModel clasamentModel = new DefaultTableModel(data, clasamentColumns) {
            @Override
            // se seteaza ca celulele sa nu fie editabile pentru tabelul Clasament
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // se seteaza modelul de tabel pentru tabelul Clasament
        Clasament.setModel(clasamentModel);

        // se repeta aceeasi procedura pentru tabelul Live
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

            dataLive = new String[rowCount][4];

            i = 0;
            // se parcurge HashMap-ul de meciuri si se adauga datele in array-ul bidimensional
            for (Integer key : meciuriMap.keySet()) {
                // key = indexul meciului in HashMap
                PairMeci meci = meciuriMap.get(key);
                // prima coloana = indexul meciului in HashMap
                dataLive[i][0] = String.valueOf(key);
                // a doua coloana = numele echipei gazda
                dataLive[i][1] = meci.getEc1().getNume();
                // a treia coloana = numele echipei oaspete
                dataLive[i][2] = meci.getEc2().getNume();
                // a patra coloana = scorul meciului
                dataLive[i][3] = meci.getEc1().getGoluriDate() + "-" + meci.getEc2().getGoluriDate();
                i++;
            }
        }

        DefaultTableModel liveModel = new DefaultTableModel(dataLive, liveColumns) {
            @Override
            // se seteaza ca celulele sa nu fie editabile pentru tabelul Live
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // se seteaza modelul de tabel pentru tabelul Live
        Live.setModel(liveModel);
    }

    // functia care actualizeaza datele din tabele la fiecare 5 secunde
    private void startAutoRefresh() {
        // se creeaza un SwingWorker care ruleaza in background si actualizeaza datele din tabele la fiecare 5 secunde ( 5000 ms )
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // se asteapta 5 secunde si se apeleaza functia de actualizare a datelor din tabele
                for (;;) {
                    // se asteapta 5 secunde in firul de executie ( thread-ul ) curent
                    Thread.sleep(5000);
                    // se apeleaza functia de actualizare a datelor din tabele
                    publish();
                }
            }

            @Override
            // se apeleaza functia de actualizare a datelor din tabele, inca suntem in interiorul SwingWorker-ului
            protected void process(List<Void> chunks) {
                try {
                    // se apeleaza functia de actualizare a datelor din tabele
                    updateTables();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        // se "programmeaza" ( Schedule ) SwingWorker-ul sa ruleze in background
        worker.execute();
    }

    // functia care adauga un event listener pentru fiecare tabel, care afiseaza informatii despre echipa selectata la click
    private void addTableClickListener(JTable table) {
        // se adauga un event listener pentru click pe tabel
        table.addMouseListener(new MouseAdapter() {
            @Override
            // se apeleaza functia de afisare a informatiilor despre echipa selectata la click
            public void mouseClicked(MouseEvent e) {
                // se preia randul si coloana la care s-a dat click
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());
                // se preia valoarea de la randul si coloana la care s-a dat click
                Object value = table.getValueAt(row, column);

                // se verifica daca randul si coloana sunt valide
                if (row >= 0 && column >= 0) {
                    // se verifica daca valoarea de la randul si coloana la care s-a dat click este o echipa
                    // mai lucrez la treaba aia cu meciuriInstance.getMeciuriMap etc
                    if(echipeInstance.containsKey(value) || /*meciuriInstance.getMeciuriMap().get(row)
                            .getEc1().getNume().contains(value.toString())*/ table.equals(Live)) {

                        // se afiseaza fereastra de dialog creata in constructorul clasei "ClasamentEchipe" cu
                        // informatiile despre echipa selectata
                        // daca tabelul este "Clasament", se afiseaza informatii despre echipa selectata
                        if(table.equals(Clasament)) {
                            // se sterge continutul ferestrei de dialog
                           ClasamentDialog.getContentPane().removeAll();
                           // se creeaza un text in fereastra de dialog cu informatiile despre meciul selectat
                            JLabel dialogText = getDialogText(false, row, table.getColumnName(column));
                            // se adauga textul in fereastra de dialog
                            ClasamentDialog.getContentPane().add(dialogText);
                            // se "impacheteaza" fereastra de dialog cu textul adaugat
                            ClasamentDialog.pack();
                            // se seteaza pozitia ferestrei de dialog la pozitia mouse-ului
                            ClasamentDialog.setLocation(e.getLocationOnScreen());
                            // se afiseaza fereastra de dialog
                            ClasamentDialog.setVisible(true);
                        }
                        // daca tabelul este "Live", se afiseaza informatii despre meciul selectat
                        else if(table.equals(Live)) {
                            // se sterge continutul ferestrei de dialog
                            ClasamentDialog.getContentPane().removeAll();
                            // se creeaza un text in fereastra de dialog cu informatiile despre meciul selectat
                            JLabel dialogText = getDialogText(true, row, table.getColumnName(column));
                            // se adauga textul in fereastra de dialog
                            ClasamentDialog.getContentPane().add(dialogText);
                            // se "impacheteaza" fereastra de dialog cu textul adaugat
                            ClasamentDialog.pack();
                            // se seteaza pozitia ferestrei de dialog la pozitia mouse-ului
                            ClasamentDialog.setLocation(e.getLocationOnScreen());
                            // se afiseaza fereastra de dialog
                            ClasamentDialog.setVisible(true);
                        }
                    }
                }
                // daca randul si coloana nu sunt valide, se ascunde fereastra de dialog
                else
                {
                    if (ClasamentDialog != null) {
                        ClasamentDialog.setVisible(false);
                    }
                }
            }
        });
    }

    // functia care formateaza informatiile despre echipa selectata
    private static JLabel getDialogText(boolean isLive, int row, String column) {
        // se creeaza un label pentru textul care va fi afisat in fereastra de dialog
        JLabel dialogText = new JLabel();

        // se preiau datele despre echipe si meciuri din HashMap-uri
        String[][] data;
        // daca tabelul este "Clasament", se preiau datele despre echipe
        if (!isLive) {
            // se preiau datele despre echipe din HashMap si se adauga in array-ul bidimensional
            // echipeInstance.size() = numarul de echipe, 2 coloane: Nume Echipa, Puncte
            data = new String[echipeInstance.size()][2];
            int i = 0;
            // se parcurge HashMap-ul si se adauga datele in array-ul bidimensional
            for (Echipa echipa : echipeInstance.values()) {
                // prima coloana = numele echipei
                data[i][0] = echipa.getNume();
                // a doua coloana = punctele echipei
                data[i][1] = String.valueOf(echipa.getPuncte());
                i++;
            }

            // se adauga textul in fereastra de dialog cu informatiile despre echipa selectata din tabelul "Clasament"
            // se formateaza textul cu HTML si se adauga in JLabel-ul "dialogText"
            dialogText.setText("<html><div style='color:blue; font-size:12px;'>Nume Echipa: "+ data[row][0] + "</div><br>" +
                    "<div style='color:green; font-size:12px;'>Puncte: " + data[row][1] + "</div><br></html>");
        }
        // daca tabelul este "Live", se preiau datele despre meciuri
        else
        {
            // se preiau datele despre meciuri din HashMap si se adauga in array-ul bidimensional
            Map<Integer, PairMeci> meciuriMap = meciuriInstance.getMeciuriMap();
            // meciuriMap.size() = numarul de meciuri, 5 coloane: Nume Echipa, Puncte, Goluri date, Goluri primite, Locatia meciului
            data = new String[meciuriMap.size()][5];
            int i = 0;
            // se parcurge HashMap-ul si se adauga datele in array-ul bidimensional
            for (Integer key : meciuriMap.keySet()) {
            // key = indexul meciului in HashMap
            PairMeci meci = meciuriMap.get(key);
            // se preia echipa gazda sau oaspete in functie de coloana selectata
            Echipa echipa = column.equals("Gazda") ? meci.getEc1() : meci.getEc2();
            // prima coloana = numele echipei
            data[i][0] = echipa.getNume();
            // a doua coloana = punctele echipei
            data[i][1] = String.valueOf(echipa.getPuncte());
            // a treia coloana = golurile date de echipa
            data[i][2] = String.valueOf(echipa.getGoluriDate());
            // a patra coloana = golurile primite de echipa
            data[i][3] = String.valueOf(echipa.getGoluriPrimite());
            // a cincea coloana = locatia meciului
            data[i][4] = echipa.getLocatia();
            i++;
        }

        // se adauga textul in fereastra de dialog cu informatiile despre meciul selectat din tabelul "Live"
        // se formateaza textul cu HTML si se adauga in JLabel-ul "dialogText"
        dialogText.setText("<html><div style='color:blue; font-size:12px;'>Nume Echipa: "+ data[row][0] + "</div><br>" +
                    "<div style='color:green; font-size:12px;'>Puncte: " + data[row][1] + "</div><br>" +
                    "<div style='color:red; font-size:12px;'>Goluri date: " + data[row][2] + "</div><br>" +
                    "<div style='color:purple; font-size:12px;'>Goluri primite: " + data[row][3] + "</div><br>" +
                    "<div style='color:orange; font-size:12px;'>Locatia: "+ data[row][4] +"</div></html>");
        }

        // se seteaza fontul si dimensiunea textului din JLabel-ul "dialogText"
        dialogText.setFont(new Font("Arial", Font.PLAIN, 12));

        // se returneaza JLabel-ul cu textul formatat
        return dialogText;
    }
}
