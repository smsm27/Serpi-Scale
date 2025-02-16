package view.tabellaView;

import model.casella.Casella;
import view.ScalaSwing;
import view.SerpenteSwing;
import view.casellaView.swing.CasellaGraficaSwing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CreateTabellaViewSwing implements CreateTabellaView {


    private JFrame frame;
    private JLayeredPane panel;
    private List<CasellaGraficaSwing> caselleList = new ArrayList<>();
    private List<SerpenteSwing> serpenti = new ArrayList<>();
    private List<ScalaSwing> scale = new ArrayList<>();
    private List<Casella> tabella = new ArrayList<>();
    private static final String TIPO = "swing";

    public CreateTabellaViewSwing() {
        inizializza();
    }

    @Override
    public void inizializza() {
        // Creazione del JFrame
        frame = new JFrame("Gestione Gioco da Tavolo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        // Creazione del menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opzioni");

        JMenuItem creaMappaItem = new JMenuItem("Crea Mappa");
        JMenuItem giocaItem = new JMenuItem("Gioca");
        JMenuItem addSerpenteItem = new JMenuItem("Add Serpente");
        JMenuItem addScalaItem = new JMenuItem("Add Scala");
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
        panel = new JLayeredPane();
        frame.add(panel);

        // Gestione azioni menu
        creaMappaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getInfoForNewTabella();
            }
        });

        salvaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeFile = JOptionPane.showInputDialog(frame,
                        "Inserisci il nome del file per salvare la tabella:");
                if (nomeFile != null && !nomeFile.trim().isEmpty()) {
                    salvaTabellaStato(nomeFile);
                }
            }
        });

        caricaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostraDialogCaricamento();
            }
        });

        addSerpenteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostraDialogCreazione("serpente");
            }
        });

        addScalaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostraDialogCreazione("scala");
            }
        });

        // Visualizzazione
        frame.setVisible(true);
    }

    @Override
    public void mostraTabellaGrafica(List<Casella> caselle) {

    }

    @Override
    public void aggiungiElementoGrafico(String tipo, int partenza, int destinazione) {

    }

    @Override
    public void salvaTabellaStato(String nomeFile) {

    }

    @Override
    public void caricaTabellaStato(String nomeFile) {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void mostraMessaggio(String messaggio, String titolo, int tipoMessaggio) {

    }
}
