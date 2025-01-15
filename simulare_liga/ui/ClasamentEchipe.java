package simulare_liga.ui;

import simulare_liga.Database;
import simulare_liga.Echipa;
import simulare_liga.Meciuri;
import simulare_liga.PairMeci;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import static simulare_liga.Main.*;
import static simulare_liga.Meciuri.getMeciuriMap;
import static simulare_liga.ui.ModernTable.setupModernStyle;
import static simulare_liga.ui.ModernScrollPane.setupModernStyle;

public class ClasamentEchipe extends JFrame {
    // ( 1 ) de aici
    private JTable Clasament;
    private JTable Live;
    private JScrollPane ScrollTableClasament;
    private JScrollPane ScrollTableLive;
    private JPanel AppPanel;
    private JFormattedTextField inputText;
    private JButton butonRandomizare;
    private JButton inserareDateButton;
    private JTextArea outputText;
    private static JDialog ClasamentDialog;
    // ( 1 ) pana aici
    // sunt elementele grafice ale aplicatiei definite in Swing form designer

    // aici se stocheaza HashMap-urile echipelor si meciurilor in desfasurare
    private static Map<String, Echipa> echipeInstance = null;
    private static Meciuri meciuriInstance = null;

    private static final Map<String, Echipa> Echipe = getEchipe();

    private int currMatchIndex = 0;
    private final List<String> listaNumeEchipe = new ArrayList<>(Echipe.keySet());

    private int nrDeMeciuri = 0;
    private final Map<Integer, PairMeci> meciOffset = getMeciuriMap();

    // in clasa Main, funtia main, se apeleaza constructorl acestei clase pentru a porni interfata grafica
    public ClasamentEchipe() {
        setTitle("Simulare Liga 1 - Proiect Java");
        // daca se inchide fereastra grafica, se inchide si aplicatia din rulare
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // se seteaza dimensiunea ferestrei
        AppPanel.setBackground(new Color(240, 242, 245));
        AppPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        setContentPane(AppPanel);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // se apeleaza functia care construieste si actualizeaza datele din tabele
        updateTables();

        buttonsActions();

        outputText.setEditable(false);
        outputText.setLineWrap(true);
        outputText.setWrapStyleWord(true);
        outputText.setVisible(false);
        inputText.setVisible(false);

        // se apeleaza functia care actualizeaza datele din tabele la fiecare 5 secunde ( SwingWorker == Thread / Runnable kind of )
        startAutoRefresh();

        // se adauga scroll la tabele in cazul in care elementele depasesc dimensiunea ferestrei
        setupModernStyle(ScrollTableClasament);
        setupModernStyle(ScrollTableLive);
        ScrollTableClasament.setViewportView(Clasament);
        ScrollTableLive.setViewportView(Live);

        setupModernStyle(Clasament);
        setupModernStyle(Live);

        // se adauga un event listener pentru fiecare tabel, care afiseaza informatii despre echipa selectata la click
        addTableClickListener(Clasament);
        addTableClickListener(Live);

        // se adauga toate elementele definite la fereastra principala si se afiseaza fereastra
        pack();
        setVisible(true);

        Timer timer = new Timer(500, _ -> {
            if (ClasamentDialog != null && !ClasamentDialog.hasFocus()) {
                ClasamentDialog.dispose();
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    private List<Echipa> order_by_puncte(Map<String, Echipa> echipe) {
        // Convertim valorile HashMap-ului într-o listă
        List<Echipa> listaEchipe = new ArrayList<>(echipe.values());
        // Sortăm lista descrescător după puncte
        listaEchipe.sort((e1, e2) -> Integer.compare(e2.getPuncte(), e1.getPuncte()));
        return listaEchipe;
    }

    private void adjustColumnWidths(JTable table) {
        final int margin = 10; // Add some margin to the width

        for (int col = 0; col < table.getColumnCount(); col++) {
            TableColumn column = table.getColumnModel().getColumn(col);
            int maxWidth;

            // Check header width
            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Component headerComponent = headerRenderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, 0, col);
            maxWidth = headerComponent.getPreferredSize().width;

            // Check cell widths
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, col);
                Component cellComponent = cellRenderer.getTableCellRendererComponent(table, table.getValueAt(row, col), false, false, row, col);
                maxWidth = Math.max(maxWidth, cellComponent.getPreferredSize().width);
            }

            // Adjust column width
            column.setPreferredWidth(maxWidth + margin);
        }
    }

    // functia care actualizeaza datele din tabele
    private void updateTables() {
        // preluam HashMap-urile echipelor din clasa Main.main
        echipeInstance = getEchipe();

        // numarul de randuri din tabelul Clasament va fi egal cu numarul de echipe din HashMap
        int rowCount = echipeInstance.size();
        // se initializeaza un array bidimensional de stringuri pentru a stoca datele din HashMap
        // rowCount = numarul de echipe, 3 coloane: Loc, Echipa, Puncte
        String[][] data = new String[rowCount][8];

        List<Echipa> orderedEchipeInstance = order_by_puncte(echipeInstance);
        int i = 0;
        // se parcurge HashMap-ul si se adauga datele in array-ul bidimensional
        for (Echipa echipa : orderedEchipeInstance) {

            // prima coloana = indexul echipei in HashMap
            data[i][0] = String.valueOf(i+1);
            // a doua coloana = numele echipei
            data[i][1] = echipa.getNume();
            // a treia coloana = punctele echipei
            data[i][2] = String.valueOf(echipa.getPuncte());
            // a 4a coloana = nr de goluri inscrise
            data[i][3] = String.valueOf(echipa.getGoluriDate());
            // a 5a coloana = nr de goluri primite
            data[i][4] = String.valueOf(echipa.getGoluriPrimite());
            // a 6a coloana = victorii
            data[i][5] = String.valueOf(echipa.getVictorii());
            // a7a coloana = egaluri
            data[i][6] = String.valueOf(echipa.getEgaluri());
            // a8a coloana = infrangeri
            data[i][7] = String.valueOf(echipa.getInfrangeri());
            i++;
        }

        // se creeaza un model de tabel cu datele din array-ul bidimensional
        String[] clasamentColumns = {"Loc", "Echipa", "Puncte", "Goluri Date", "Goluri Primite", "Victorii", "Egaluri", "Infrangeri"};
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

        adjustColumnWidths(Clasament);
        // se repeta aceeasi procedura pentru tabelul Live
        String[] liveColumns = {"Meci ID", "Gazda", "Oaspete", "Scor", "Locatie"};

        meciuriInstance = getMeciuri();
        // TODO: Hashmap pentru indentificarea meciurilor din retur.
        // daca key-ul + valoarea exista, meciul este in retur .
        // EX: 1 + 2 = tur, 2 + 1 = retur, sau poate alta metoda aparent.
        // Aici o sa fie si comparatorul.
        String[][] dataLive = new String[0][4];
        if (meciuriInstance != null) {
            Map<Integer, PairMeci> meciuriMap = getMeciuriMap();

            rowCount = meciuriMap.size();

            dataLive = new String[rowCount][5];

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
                dataLive[i][3] = meci.getGoluriDateEC1() + "-" + meci.getGoluriDateEC2();
                // locatia unde se tine meciul
                dataLive[i][4] = meci.getEc1().getLocatia();
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

    private void buttonsActions() {
        inputText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    inserareDateButton.doClick(); // Simulate a button click
                }
            }
        });

        inserareDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputText.setVisible(true);
                inserareDateButton.setVisible(true);
                outputText.setVisible(true);
                butonRandomizare.setVisible(false);

                redirectSystemOutToTextArea(outputText);

                if (currMatchIndex == 0) {
                    outputText.setText("");
                    promptNextMatch();
                    currMatchIndex++;
                } else {
                    String text = inputText.getText();
                    if (!text.trim().isEmpty() && text.matches("\\d+-\\d+")) {
                        String[] scores = text.split("-");
                        int firstScore = Integer.parseInt(scores[0].trim());
                        int secondScore = Integer.parseInt(scores[1].trim());

                        updateMatchScore(firstScore, secondScore);

                        currMatchIndex++;
                        outputText.setText("");
                        inputText.setText("");
                        if (currMatchIndex < listaNumeEchipe.size() * (listaNumeEchipe.size() - 1)) {
                            promptNextMatch();
                        } else {
                            System.out.println("Scorurile tuturor meciurilor au fost completate!\n");
                            inputText.setVisible(false);
                            inserareDateButton.setVisible(false);
                            outputText.setForeground(new Color(67, 34, 214));
                            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                                @Override
                                protected Void doInBackground() throws InterruptedException {
                                    Thread.sleep(5000);
                                    outputText.setVisible(false);
                                    insertIntoDB();
                                    return null;
                                }
                            };

                            worker.execute();
                        }
                    } else {
                        System.out.println("Format scor valid: X-Y\n");
                    }
                }
            }

            private void promptNextMatch() {
                int i = currMatchIndex / (listaNumeEchipe.size() - 1);
                int j = currMatchIndex % (listaNumeEchipe.size() - 1);
                if (j >= i) {
                    j++;
                }
                String team1 = listaNumeEchipe.get(i);
                String team2 = listaNumeEchipe.get(j);

                outputText.append("Introdu scorul intre \"" + team1 + "\" vs \"" + team2 + "\" \n(format valid: X-Y):\n");
            }

            private void updateMatchScore(int firstScore, int secondScore) {
                int i = currMatchIndex / (listaNumeEchipe.size() - 1);
                int j = currMatchIndex % (listaNumeEchipe.size() - 1);
                if (j >= i) {
                    j++;
                }

                String team1 = listaNumeEchipe.get(i);
                String team2 = listaNumeEchipe.get(j);

                Echipa echipa1 = Echipe.get(team1);
                Echipa echipa2 = Echipe.get(team2);

                echipa1.setGoluriDate(echipa1.getGoluriDate() + firstScore);
                echipa1.setGoluriPrimite(echipa1.getGoluriPrimite() + secondScore);
                echipa2.setGoluriDate(echipa2.getGoluriDate() + secondScore);
                echipa2.setGoluriPrimite(echipa2.getGoluriPrimite() + firstScore);

                PairMeci meci = new PairMeci(echipa1, echipa2);
                meciOffset.put(nrDeMeciuri, meci);

                meci.setGoluriDateEC1(firstScore);
                meci.setGoluriDateEC2(secondScore);
                Meciuri.calcPuncte(firstScore, secondScore, echipa1, echipa2);

                nrDeMeciuri++;
            }
        });

        butonRandomizare.addActionListener(_ -> {

            inputText.setVisible(false);
            inserareDateButton.setVisible(false);
            butonRandomizare.setVisible(false);
            outputText.setVisible(false);

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    Meciuri meciuriVar = getMeciuri();
                    try {
                        meciuriVar.setScoreRandom();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    insertIntoDB();
                    System.out.println("Randomization completed!");
                }
            };

            worker.execute();
        });
    }

    private void redirectSystemOutToTextArea(JTextArea textArea) {
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) {
                SwingUtilities.invokeLater(() -> {
                    textArea.append(String.valueOf((char) b));
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                });
            }
        };

        PrintStream printStream = new PrintStream(outputStream, true);
        System.setOut(printStream);
        System.setErr(printStream);
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
                    Thread.sleep(1100);
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

                // se initializeaza fereastra de dialog care afiseaza informatii despre echipa selectata din orice tabel
                ClasamentDialog = createDialog();

                // se verifica daca randul si coloana sunt valide
                if (row >= 0 && column >= 0) {
                    // se verifica daca valoarea de la randul si coloana la care s-a dat click este o echipa
                    // mai lucrez la treaba aia cu meciuriInstance.getMeciuriMap etc
                    if(echipeInstance.containsKey(value)) {
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
            }
        });
    }

    private static JDialog createDialog(){
        // se initializeaza fereastra de dialog care afiseaza informatii despre echipa selectata din orice tabel
        if(ClasamentDialog != null) {
            ClasamentDialog.dispose();
        }

        ClasamentDialog = new JDialog();
        ClasamentDialog.setTitle("Informatii Echipa");
        // se seteaza un border pentru fereastra de dialog
        ClasamentDialog.getRootPane().setBorder(new LineBorder(Color.GRAY, 3));
        // se seteaza fereastra de dialog sa nu aiba bara de titlu ( minimize, maximize, close si iconita aia java )
        ClasamentDialog.setUndecorated(true);

        ClasamentDialog.setSize(200, 200);

        return ClasamentDialog;
    }

    // functia care formateaza informatiile despre echipa selectata
    private static JLabel getDialogText(boolean isLive, int row, String column) {
        // se creeaza un label pentru textul care va fi afisat in fereastra de dialog

        // de uitat asta
        JLabel dialogText = new JLabel();

        // se preiau datele despre echipe si meciuri din HashMap-uri
        String[][] data;
        // daca tabelul este "Clasament", se preiau datele despre echipe
        if (!isLive) {
            // se preiau datele despre echipe din HashMap si se adauga in array-ul bidimensional
            // echipeInstance.size() = numarul de echipe, 2 coloane: Nume Echipa, Locatie
            data = new String[echipeInstance.size()][2];
            int i = 0;
            // se parcurge HashMap-ul si se adauga datele in array-ul bidimensional
            for (Echipa echipa : echipeInstance.values()) {
                // prima coloana = numele echipei
                data[i][0] = echipa.getNume();
                // a doua coloana = locatia echipei
                data[i][1] = echipa.getLocatia();
                i++;
            }

            // se adauga textul in fereastra de dialog cu informatiile despre echipa selectata din tabelul "Clasament"
            // se formateaza textul cu HTML si se adauga in JLabel-ul "dialogText"
            dialogText.setText("<html><div style='color:blue; font-size:12px;'>Nume Echipa: "+ data[row][0] + "</div><br>" +
                    "<div style='color:green; font-size:12px;'>Locatia: " + data[row][1] + "</div><br></html>");
        }
        // daca tabelul este "Live", se preiau datele despre meciuri
        else
        {
            // se preiau datele despre meciuri din HashMap si se adauga in array-ul bidimensional
            Map<Integer, PairMeci> meciuriMap = getMeciuriMap();
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

    private void insertIntoDB() {
        Database db = Database.getInstance();
        Connection conn = db.getConnection();
        updateEchipe(conn);
        meciuriInstance = getMeciuri();
        if (meciuriInstance != null) {
            Map<Integer, PairMeci> meciuriMap = getMeciuriMap();
            for (Integer key : meciuriMap.keySet()) {
                PairMeci meci = meciuriMap.get(key);
                // query-ul de inserare in baza de date
                String query = "INSERT INTO `Meciuri` (`ID_Gazda`, `ID_Oaspete`, `Scor_gazda`, `Scor_oapete`) VALUES (?, ?, ?, ?)";
                try {
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setInt(1, meci.getEc1().getId());
                        pstmt.setInt(2, meci.getEc2().getId());
                        pstmt.setInt(3, meci.getGoluriDateEC1());
                        pstmt.setInt(4, meci.getGoluriDateEC2());
                        pstmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
