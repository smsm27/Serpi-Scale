package view;


import model.casella.Casella;
import model.casella.CasellaFlyweight;
import model.tabella.TabellaBuilder;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Tabella {
    private static List<SerpenteSwing> serpenti = new ArrayList<>();
    private static List<CasellaGraficaSwing> caselleList = new ArrayList<>();
    private static List<Scala> scale = new ArrayList<>();
    private static List<Casella> tabella=new ArrayList<>();
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
        JMenuItem addSerpenteItem = new JMenuItem("Add SerpenteSwing");
        JMenuItem removeSerpenteItem = new JMenuItem("Remove SerpenteSwing");
        JMenuItem addScalaItem = new JMenuItem("Add Scala");
        JMenuItem removeScalaItem = new JMenuItem("Remove Scala");
        menu.add(creaMappaItem);
        menu.add(giocaItem);
        menu.add(addSerpenteItem);
        menu.add(addScalaItem);
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
        //Add Scala
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
                // Usa il builder per creare la tabella
                TabellaBuilder tabellaBuilder =  new TabellaBuilder.Builder(righe*colonne,casellaFlyweight)
                        .righe(righe)
                        .colonne(colonne)
                        .build();

                panel.removeAll();

                AddTabella(tabellaBuilder, panel);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Inserire valori validi!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void AddTabella(TabellaBuilder griglia, JLayeredPane panel) {
        tabella = griglia.getTabella();
        for (Casella casella : tabella) {
            CasellaGraficaSwing casellaGraficaSwing = (CasellaGraficaSwing) casella.getCasellaGrafica();
                caselleList.add(casellaGraficaSwing);
            panel.add(casellaGraficaSwing, JLayeredPane.DEFAULT_LAYER);
        }
        panel.revalidate();
        panel.repaint();
    }

    private static void readTabella(List<CasellaGraficaSwing> casellaGraficaSwingList, JLayeredPane panel) {

        // Ci starebbe un bel Visitor
        for (CasellaGraficaSwing casellaGraficaSwing : casellaGraficaSwingList) {
            Casella curr = casellaGraficaSwing.getCasella();

            // Controlla se la casella è un serpente
            if (curr.getCasellaState() == Casella.CasellaState.SERPENTE) {
                CasellaGraficaSwing destinazione = trovaCasellaDaIndice( curr.getDestinazione().getIndice());
                SerpenteSwing serpenteSwing = new SerpenteSwing(casellaGraficaSwing,  destinazione);
                serpenti.add(serpenteSwing);
                panel.add(serpenteSwing, JLayeredPane.DRAG_LAYER);
            }
            // Controlla se la casella è una scala
            else if (curr.getCasellaState()== Casella.CasellaState.SCALA) {
                CasellaGraficaSwing destinazione = trovaCasellaDaIndice( curr.getDestinazione().getIndice());
                Scala scala = new Scala(casellaGraficaSwing, destinazione);
                scale.add(scala); // Aggiungi alla lista delle scale
                panel.add(scala, JLayeredPane.DRAG_LAYER);
            }

            // Aggiungi la casella grafica al pannello
            panel.add(casellaGraficaSwing, JLayeredPane.DEFAULT_LAYER);
        }

        // Aggiorna il pannello solo una volta
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

                if (partenza > destinazione) {

                    CasellaGraficaSwing casellaPartenza = trovaCasellaDaIndice(partenza);
                    CasellaGraficaSwing casellaDestinazione = trovaCasellaDaIndice(destinazione);


                    if (casellaPartenza != null && casellaDestinazione != null) {
                        switch (tipo) {
                            case "serpente":
                                tabella.get(partenza).setDestinazione(tabella.get(destinazione));
                                SerpenteSwing serpenteSwing = new SerpenteSwing(casellaPartenza, casellaDestinazione);
                                serpenti.add(serpenteSwing);
                                panel.add(serpenteSwing, JLayeredPane.DRAG_LAYER);
                                panel.repaint();
                            case "scala":
                                tabella.get(destinazione).setDestinazione(tabella.get(partenza));

                                Scala scala = new Scala(casellaPartenza, casellaDestinazione);
                                scale.add(scala);
                                panel.add(scala, JLayeredPane.DRAG_LAYER);
                                panel.repaint();
                        }

                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "L'indice di partenza deve essere maggiore della destinazione",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Inserire valori numerici validi",
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static CasellaGraficaSwing trovaCasellaDaIndice(int indice) {
        //controllo se tabella non null
        return caselleList.get(indice);
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
//            List<CasellaGraficaSwing> casellaGraficaSwingList = GestioneTabelle.caricaTabella("src/main/save" + "/" + scelta);
//            //}
//
//            assert casellaGraficaSwingList != null;
//            readTabella(casellaGraficaSwingList, panel);

        }


}



