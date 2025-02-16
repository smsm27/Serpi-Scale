package view.tabellaView;


import model.CaselleSpeciali.Scala;
import model.CaselleSpeciali.Serpente;
import model.casella.Casella;
import model.casella.CasellaFlyweight;
import model.casella.CasellaGrafica;
import model.casella.CasellaGraficaFactory;
import model.tabella.TabellaBuilder;
import model.tabella.TabellaFactory;
import model.tabella.TabellaStato;
import tools.ScalaMapper;
import tools.SerpenteMapper;
import view.casellaView.swing.CasellaGraficaSwing;
import view.casellaView.swing.CasellaGraficaSwingModificabile;
import view.ScalaSwing;
import view.SerpenteSwing;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Tabella {

    private static List<CasellaGraficaSwing> caselleList = new ArrayList<>();
    private static List<SerpenteSwing> serpenti = new ArrayList<>();
    private static List<ScalaSwing> scale = new ArrayList<>();
    private static List<Casella> tabella=new ArrayList<>();

    //Set
    JFrame frame;

    private static final String TIPO ="swing";
    public static void main(String[] args) {

        // Creazione del JFrame
        JFrame frame = new JFrame("Gestione Gioco da Tavolo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        // Creazione del menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opzioni");

        JMenuItem creaMappaItem = new JMenuItem("Crea Mappa");
        JMenuItem giocaItem = new JMenuItem("Gioca");
        JMenuItem addSerpenteItem = new JMenuItem("Add Serpente");
        JMenuItem removeSerpenteItem = new JMenuItem("Remove Serpente");
        JMenuItem addScalaItem = new JMenuItem("Add ScalaSwing");
        JMenuItem removeScalaItem = new JMenuItem("Remove Scala");
        JMenuItem salvaItem = new JMenuItem("Salva Tabella");
        JMenuItem caricaItem = new JMenuItem("Carica Tabella");

        menu.add(creaMappaItem);
        menu.add(giocaItem);
        menu.add(addSerpenteItem);
        menu.add(addScalaItem);
        menu.add(salvaItem);
        menu.add(caricaItem);
        menuBar.add(menu);

        frame.setJMenuBar(menuBar);

        // Pannello principale per la visualizzazione
        JLayeredPane panel = new JLayeredPane();
        frame.add(panel);


        // Gestione azioni menu
        creaMappaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getInfoForNewTabella(panel, frame);
            }
        });

        salvaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salva(frame);
            }
        });

        caricaItem.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               caricaMappa(frame, panel);
           }
        });

        //gioca
        giocaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        //Add SerpenteSwing
        addSerpenteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostraDialogCreazione(panel, "serpente");
            }
        });
        //Add ScalaSwing
        addScalaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostraDialogCreazione(panel, "scala");
            }
        });
        // Visualizzazione
        frame.setVisible(true);
    }

    private static void getInfoForNewTabella(JLayeredPane panel, JFrame frame) {
        // Mostra una finestra di dialogo per configurare la tabella
        JTextField righeField = new JTextField("10");
        JTextField colonneField = new JTextField("10");
        JTextField dimXField = new JTextField("50");
        JTextField dimYField = new JTextField("50");

        Object[] messaggio = {
                "Numero di righe:", righeField,
                "Numero di colonne:", colonneField,
                "Dimensione casella X:", dimXField,
                "Dimensione casella Y:", dimYField
        };

        int opzione = JOptionPane.showConfirmDialog(
                frame,
                messaggio,
                "Configura Tabella",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (opzione == JOptionPane.OK_OPTION) {
            try {
                int righe = Integer.parseInt(righeField.getText());
                int colonne = Integer.parseInt(colonneField.getText());
                int dimX = Integer.parseInt(dimXField.getText());
                int dimY = Integer.parseInt(dimYField.getText());
                CasellaFlyweight casellaFlyweight=new CasellaFlyweight(dimX,dimY,null);
//                // Usa il builder per creare la tabella
//                TabellaBuilder tabellaBuilder =  new TabellaBuilder.Builder(righe*colonne,casellaFlyweight)
//                        .righe(righe)
//                        .colonne(colonne)
//                        .build();
                TabellaFactory tabellaFactory=new TabellaFactory(casellaFlyweight);
                List<Casella> caselle= tabellaFactory.creaTabellone(righe);

                panel.removeAll();

                AddTabella(caselle, panel);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Inserire valori validi!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void AddTabella(List<Casella> griglia, JLayeredPane panel) {
        tabella = griglia;
        for (Casella casella : griglia) {
            CasellaGraficaSwingModificabile casellaGraficaSwingModificabile = (CasellaGraficaSwingModificabile) CasellaGraficaFactory.createCasellaGrafica(casella,TIPO);
                caselleList.add(casellaGraficaSwingModificabile);
            assert casellaGraficaSwingModificabile != null;
            panel.add(casellaGraficaSwingModificabile, JLayeredPane.DEFAULT_LAYER);
        }
        panel.revalidate();
        panel.repaint();
    }

    private static void mostraDialogCreazione(JLayeredPane panel, String tipo) {
        JTextField partenzaField = new JTextField();
        JTextField destinazioneField = new JTextField();

        Object[] message = {
                "Indice casella partenza:", partenzaField,
                "Indice casella destinazione:", destinazioneField
        };

        int option = JOptionPane.showConfirmDialog(null, message,
                "Crea ", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int partenza = Integer.parseInt(partenzaField.getText());
                int destinazione = Integer.parseInt(destinazioneField.getText());

                if ( (partenza > destinazione && tipo.equals("serpente")) || (destinazione > partenza && tipo.equals("scala")) ) {

                    CasellaGraficaSwingModificabile casellaPartenza = trovaCasellaDaIndice(partenza);
                    CasellaGraficaSwingModificabile casellaDestinazione = trovaCasellaDaIndice(destinazione);


                    if (casellaPartenza != null && casellaDestinazione != null) {
                        Casella p=tabella.get(partenza);
                        switch (tipo) {
                            case "serpente":

                                p.setDestinazione(tabella.get(destinazione));
                                p.setCasellaState(Casella.CasellaState.SERPENTE);
                                SerpenteSwing serpenteSwing = new SerpenteSwing(casellaPartenza, casellaDestinazione);
                                serpenti.add(serpenteSwing);
                                panel.add(serpenteSwing, JLayeredPane.DRAG_LAYER);
                                panel.repaint();
                                break;
                            case "scala":

                                p.setDestinazione(tabella.get(destinazione));
                                p.setCasellaState(Casella.CasellaState.SCALA);

                                ScalaSwing scala = new ScalaSwing(casellaPartenza, casellaDestinazione);
                                scale.add(scala);
                                panel.add(scala, JLayeredPane.DRAG_LAYER);
                                panel.repaint();
                                break;
                        }

                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "L'indice non corrispondono, e un/a: "+tipo,
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Inserire valori numerici validi",
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static CasellaGraficaSwingModificabile trovaCasellaDaIndice(int indice) {
        //controllo se tabella non null
        return (CasellaGraficaSwingModificabile) caselleList.get(indice);
    }

    private void gioca(JLayeredPane panel, JFrame frame) {
//        // Logica per caricare una mappa salvata o predefinita
//        panel.removeAll();
//
//        List<String> opzioni = GestioneTabelle.ottieniNomiTabelle("src/main/save");
//        String scelta = (String) JOptionPane.showInputDialog(
//                frame,
//                "Seleziona una mappa:",
//                "Caricamento Mappa",
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                opzioni.toArray(),
//                opzioni.getFirst()
//        );
//
//        if (scelta != null) {
//            getInfoForNewTabella(panel, frame);
//        } else {
//            //Logica per caricare una mappa salvata
//            List<CasellaGraficaSwingOnlyRead> casellaGraficaSwingList = GestioneTabelle.caricaTabella("src/main/save" + "/" + scelta);
//            //}
//
//            assert casellaGraficaSwingList != null;
//            readTabella(casellaGraficaSwingList, panel);

    }


    private static void salva(JFrame frame){
        if (tabella.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nessuna tabella da salvare!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nomeFile = JOptionPane.showInputDialog(frame,
                "Inserisci il nome del file per salvare la tabella:");
        if (nomeFile != null && !nomeFile.trim().isEmpty()) {
            try {
                // Crea directory se non esiste
                File directory = new File("src/main/save");
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Crea oggetto per salvare lo stato
                TabellaStato stato = new TabellaStato();
                stato.setCaselle(tabella);
                List<Serpente> serpentiLogica = SerpenteMapper.fromGraficaToLogic(serpenti );
                stato.setSerpenti(serpentiLogica);
                List<Scala> scaleLogiche= ScalaMapper.fromGraficaToLogic(scale);
                stato.setScale(scaleLogiche);

                // Salva su file
                ObjectOutputStream out = new ObjectOutputStream(
                        new FileOutputStream("src/main/save/" + nomeFile + ".ser"));
                out.writeObject(stato);
                out.close();

                JOptionPane.showMessageDialog(frame, "Tabella salvata con successo!",
                        "Successo", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Errore durante il salvataggio: " + ex.getMessage(),
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void caricaMappa(JFrame frame, JLayeredPane panel) {
        File directory = new File("src/main/save");
        if (!directory.exists() || !directory.isDirectory()) {
            JOptionPane.showMessageDialog(frame, "Nessuna mappa salvata trovata!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".ser"));
        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(frame, "Nessuna mappa salvata trovata!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] mapNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            mapNames[i] = files[i].getName().replace(".ser", "");
        }

        String scelta = (String) JOptionPane.showInputDialog(
                frame,
                "Seleziona una mappa da caricare:",
                "Carica Mappa",
                JOptionPane.QUESTION_MESSAGE,
                null,
                mapNames,
                mapNames[0]
        );

        if (scelta != null) {
            try {
                ObjectInputStream in = new ObjectInputStream(
                        new FileInputStream("src/main/save/" + scelta + ".ser"));
                TabellaStato stato = (TabellaStato) in.readObject();
                in.close();

                // Pulisci il pannello
                panel.removeAll();
                caselleList.clear();
                serpenti.clear();
                scale.clear();

                // Carica lo stato
                tabella = stato.getCaselle();

                //POssibile ottimizzazione nel caso debba solo disegnare la tabella
                //un solo CasellaGraficaSwingOnlyRead
                // Ricrea le caselle grafiche
                for (Casella casella : tabella) {
                    System.out.println(casella.getPosizione().toString());
                    CasellaGraficaSwingModificabile casellaGrafica = new CasellaGraficaSwingModificabile(casella);
                    caselleList.add(casellaGrafica);
                    panel.add(casellaGrafica, JLayeredPane.DEFAULT_LAYER);
                }

                serpenti=SerpenteMapper.fromLogicToSwing(stato.getSerpenti(),caselleList);
                scale= ScalaMapper.fromLogicToGrafica(stato.getScale(),caselleList);

                for (SerpenteSwing o: serpenti){
                    panel.add(o, JLayeredPane.DRAG_LAYER);
                }
                for (ScalaSwing o: scale){
                    panel.add(o, JLayeredPane.DRAG_LAYER);
                }
                panel.revalidate();
                panel.repaint();

                JOptionPane.showMessageDialog(frame, "Mappa caricata con successo!",
                        "Successo", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(frame, "Errore durante il caricamento: " + ex.getMessage(),
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}



