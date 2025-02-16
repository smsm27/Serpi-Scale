//package view.astrazione;
//
//import model.CaselleSpeciali.Scala;
//import model.CaselleSpeciali.Serpente;
//import model.Giocatore.Giocatore;
//import model.casella.Casella;
//import model.tabella.TabellaStato;
//import tools.Colori;
//import tools.ScalaMapper;
//import tools.SerpenteMapper;
//import view.ControllerPlayerImplSwing;
//import view.GiocatoreGraficaSwing;
//import view.ScalaSwing;
//import view.SerpenteSwing;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import static view.tabellaView.Tabella.caselleList;
//
//public class SwingGiocoFactory implements GiocoFactory{
//    private JFrame frame;
//    private JLayeredPane panel;
//    private Colori colori;
//
//    private List<Casella> tabella=new ArrayList<>();
//
//    private static HashMap<Color,GiocatoreGraficaSwing > giocatoriList=new HashMap<>();
//    private static List<SerpenteSwing> serpenti = new ArrayList<>();
//    private static List<ScalaSwing> scale = new ArrayList<>();
//
//
//    @Override
//    public TabellaStato caricaTabella() {
//        File directory = new File("src/main/save");
//        if (!directory.exists() || !directory.isDirectory()) {
//            JOptionPane.showMessageDialog(frame, "Nessuna mappa salvata trovata!",
//                    "Errore", JOptionPane.ERROR_MESSAGE);
//            return null;
//        }
//
//        File[] files = directory.listFiles((dir, name) -> name.endsWith(".ser"));
//        if (files == null || files.length == 0) {
//            JOptionPane.showMessageDialog(frame, "Nessuna mappa salvata trovata!",
//                    "Errore", JOptionPane.ERROR_MESSAGE);
//            return null;
//        }
//
//        String[] mapNames = new String[files.length];
//        for (int i = 0; i < files.length; i++) {
//            mapNames[i] = files[i].getName().replace(".ser", "");
//        }
//
//        String scelta = (String) JOptionPane.showInputDialog(
//                frame,
//                "Seleziona una mappa da caricare:",
//                "Carica Mappa",
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                mapNames,
//                mapNames[0]
//        );
//
//        if (scelta != null) {
//            try {
//                ObjectInputStream in = new ObjectInputStream(
//                        new FileInputStream("src/main/save/" + scelta + ".ser"));
//                TabellaStato stato = (TabellaStato) in.readObject();
//                in.close();
//                if(stato != null) return stato;
//
//
//            } catch (ClassNotFoundException | IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public void salvaTabella() {
//        if (tabella.isEmpty()) {
//            JOptionPane.showMessageDialog(frame, "Nessuna tabella da salvare!",
//                    "Errore", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        String nomeFile = JOptionPane.showInputDialog(frame,
//                "Inserisci il nome del file per salvare la tabella:");
//        if (nomeFile != null && !nomeFile.trim().isEmpty()) {
//            try {
//                // Crea directory se non esiste
//                File directory = new File("src/main/save");
//                if (!directory.exists()) {
//                    directory.mkdirs();
//                }
//
//                // Crea oggetto per salvare lo stato
//                TabellaStato stato = new TabellaStato();
//                stato.setCaselle(tabella);
//                List<Serpente> serpentiLogica = SerpenteMapper.fromGraficaToLogic(serpenti );
//                stato.setSerpenti(serpentiLogica);
//                List<Scala> scaleLogiche= ScalaMapper.fromGraficaToLogic(scale);
//                stato.setScale(scaleLogiche);
//
//                // Salva su file
//                ObjectOutputStream out = new ObjectOutputStream(
//                        new FileOutputStream("src/main/save/" + nomeFile + ".ser"));
//                out.writeObject(stato);
//                out.close();
//
//                JOptionPane.showMessageDialog(frame, "Tabella salvata con successo!",
//                        "Successo", JOptionPane.INFORMATION_MESSAGE);
//            } catch (IOException ex) {
//                JOptionPane.showMessageDialog(frame, "Errore durante il salvataggio: " + ex.getMessage(),
//                        "Errore", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
//
//    @Override
//    public void inizializzaCasellaFactory() {
//
//    }
//
//    @Override
//    public void inizializzaController() {
//        ControllerPlayerImplSwing controller = new ControllerPlayerImplSwing();
//        frame.add(controller.getPanel(), BorderLayout.SOUTH);
//        frame.setVisible(true);
//    }
//
//    @Override
//    public void mostraRisultatoDado(int risultato) {
//        int option = JOptionPane.showOptionDialog(panel,
//                "Hai fatto: " + risultato,
//                "Lancio",
//                JOptionPane.DEFAULT_OPTION,
//                JOptionPane.INFORMATION_MESSAGE,
//                null,
//                new Object[]{"OK"},
//                "OK");
//        //notifica su ok
//        if (option == 0) {
//
//        }
//    }
//
//    @Override
//    public void caricaGiocatori(List<Giocatore> giocatori) {
//        for (Giocatore g : giocatori) {
//            GiocatoreGraficaSwing giocatore = new GiocatoreGraficaSwing(g);
//            giocatore.setBounds((int)g.getPosizione().getX(),
//                    (int)g.getPosizione().getY(),
//                    30, 30); // Imposta dimensioni e posizione
//            giocatoriList.put(g.getColor(),giocatore);
//            panel.add(giocatore, JLayeredPane.MODAL_LAYER );
//        }
//    }
//
//    @Override
//    public Giocatore getInfoPlayers() {
//        JPanel inputPanel = new JPanel();
//        inputPanel.setLayout(new GridLayout(2, 2, 5, 5));
//
//        // Campo nome
//        JTextField nomeField = new JTextField(10);
//        inputPanel.add(new JLabel("Nome: "));
//        inputPanel.add(nomeField);
//
//        // Menu a tendina per i colori
//        String[] nomiColori = new String[colori.getColori().size()];
//        int i = 0;
//        for (String s : colori.getColori().keySet()) {
//            nomiColori[i] = s;
//            i++;
//        }
//        JComboBox<String> coloreCombo = new JComboBox<>(nomiColori);
//        inputPanel.add(new JLabel("Colore: "));
//        inputPanel.add(coloreCombo);
//
//        int result = JOptionPane.showConfirmDialog(frame,
//                inputPanel,
//                "Inserisci i dati del giocatore",
//                JOptionPane.OK_CANCEL_OPTION);
//
//        if (result == JOptionPane.OK_OPTION) {
//            String nome = nomeField.getText();
//            String coloreSelezionato = (String) coloreCombo.getSelectedItem();
//            Giocatore g = new Giocatore(nome, caselleList.getFirst().getCasella().getPosizione(), colori.getColori().get(coloreSelezionato));
//            colori.getColori().remove(coloreSelezionato);
//            return g;
//        }
//
//        return null; // Se l'utente annulla
//    }
//}
