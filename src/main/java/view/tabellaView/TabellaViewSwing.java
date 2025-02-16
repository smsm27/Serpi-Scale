package view.tabellaView;

import model.Giocatore.Giocatore;
import model.GiocoManager;
import model.casella.Casella;
import model.mediator.MediatorImpl;
import model.tabella.TabellaStato;
import model.tabella.TabellaViewImpl;
import tools.Colori;
import tools.Posizione;
import tools.ScalaMapper;
import tools.SerpenteMapper;
import view.*;
import view.casellaView.swing.CasellaGraficaSwingOnlyRead;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabellaViewSwing extends TabellaViewImpl {
    private JFrame frame;
    private JLayeredPane panel;
    private Colori colori;
    private MediatorImpl mediator=new MediatorImpl();

    private GiocatoreGraficaSwing giocatoreCurr;

    private static HashMap<Color,GiocatoreGraficaSwing > giocatoriList=new HashMap<>();
    private static List<CasellaGraficaSwingOnlyRead> caselleList = new ArrayList<>();
    private static List<SerpenteSwing> serpenti = new ArrayList<>();
    private static List<ScalaSwing> scale = new ArrayList<>();

    public TabellaViewSwing() {
        //Set frame panel
        colori = new Colori();
        frame = new JFrame("Serpi e Scale");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        panel = new JLayeredPane();
        frame.add(panel);

        //Inizalizza tabella
        TabellaStato stato = caricaMappaDaLocale();
        assert stato != null;
        readTabellaStato(stato);

        //inizializza i giocatori
        List<Giocatore> giocatori = getInfoplayers();
        caricaGiocatori(giocatori);

        //Set controller
        ControllerPlayerImplSwing controller = new ControllerPlayerImplSwing();
        frame.add(controller.getPanel(), BorderLayout.SOUTH);
        frame.setVisible(true);

        //set mediator
        mediator.registerView(this);
        mediator.registerGameManager(giocatori,stato.getCaselle());
        mediator.registerController(controller);
        mediator.start();
    }

    private void caricaGiocatori(List<Giocatore> giocatori) {
        for (Giocatore g : giocatori) {
            GiocatoreGraficaSwing giocatore = new GiocatoreGraficaSwing(g);
            giocatore.setBounds((int)g.getPosizione().getX(),
                    (int)g.getPosizione().getY(),
                    30, 30); // Imposta dimensioni e posizione
            giocatoriList.put(g.getColor(),giocatore);
            panel.add(giocatore, JLayeredPane.MODAL_LAYER );
        }
    }

    private void readTabellaStato(TabellaStato stato) {

        // Ricrea le caselle grafiche
        for (Casella casella : stato.getCaselle()) {
            System.out.println(casella.getPosizione().toString());
            CasellaGraficaSwingOnlyRead casellaGrafica = new CasellaGraficaSwingOnlyRead(casella);
            System.out.println(casellaGrafica.getCasella().getIndice());
            caselleList.add(casellaGrafica);
            panel.add(casellaGrafica, JLayeredPane.DEFAULT_LAYER);
        }

        serpenti= SerpenteMapper.fromLogicToSwing(stato.getSerpenti(),caselleList);
        scale= ScalaMapper.fromLogicToGrafica(stato.getScale(),caselleList);

        for (SerpenteSwing o: serpenti){
            panel.add(o, JLayeredPane.PALETTE_LAYER);
        }
        for (ScalaSwing o: scale){
            panel.add(o, JLayeredPane.PALETTE_LAYER);
        }
        panel.revalidate();
        panel.repaint();
    }

    private List<Giocatore> getInfoplayers() {
        JTextField nGiocatoriField = new JTextField("4");
        Object[] messaggio = {
                "Numero di giocatori", nGiocatoriField
        };
        int opzione = JOptionPane.showConfirmDialog(
                frame,
                messaggio,
                "configura Giocatori",
                JOptionPane.OK_CANCEL_OPTION
        );
        if (opzione == JOptionPane.OK_OPTION) {
            try {
                int numeroGiocatori = Integer.parseInt(nGiocatoriField.getText());
                if (numeroGiocatori < 0 || numeroGiocatori > 4) {
                    JOptionPane.showMessageDialog(panel, "Numero di Giocatori non valido, Max 4");
                } else {
                    List<Giocatore> giocatori = new ArrayList<Giocatore>();
                    for (int i = 0; i < numeroGiocatori; i++) {
                        Giocatore giocatore = getInfoPlayer();
                        giocatori.add(giocatore);
                    }

                    return giocatori;

                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Inserire valori validi!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }

        return List.of();
    }

    private Giocatore getInfoPlayer() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 5, 5));

        // Campo nome
        JTextField nomeField = new JTextField(10);
        inputPanel.add(new JLabel("Nome: "));
        inputPanel.add(nomeField);

        // Menu a tendina per i colori
        String[] nomiColori = new String[colori.getColori().size()];
        int i = 0;
        for (String s : colori.getColori().keySet()) {
            nomiColori[i] = s;
            i++;
        }
        JComboBox<String> coloreCombo = new JComboBox<>(nomiColori);
        inputPanel.add(new JLabel("Colore: "));
        inputPanel.add(coloreCombo);

        int result = JOptionPane.showConfirmDialog(frame,
                inputPanel,
                "Inserisci i dati del giocatore",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText();
            String coloreSelezionato = (String) coloreCombo.getSelectedItem();
            Giocatore g = new Giocatore(nome, caselleList.getFirst().getCasella().getPosizione(), colori.getColori().get(coloreSelezionato));
            colori.getColori().remove(coloreSelezionato);
            return g;
        }

        return null; // Se l'utente annulla
    }

    private  TabellaStato caricaMappaDaLocale() {
        File directory = new File("src/main/save");
        if (!directory.exists() || !directory.isDirectory()) {
            JOptionPane.showMessageDialog(frame, "Nessuna mappa salvata trovata!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".ser"));
        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(frame, "Nessuna mappa salvata trovata!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return null;
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
                if(stato != null) return stato;


            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void mostraRisDadi(int risultato) {
        int option = JOptionPane.showOptionDialog(panel,
                "Hai fatto: " + risultato,
                "Lancio",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"OK"},
                "OK");
        //notifica su ok
        if (option == 0) {
            mediator.notifyPlayerMove();
        }
    }

    @Override
    public void setGiocatoreCurr(Giocatore giocatore) {
        giocatoreCurr = giocatoriList.get(giocatore.getColor());
        // Aggiorna visivamente il giocatore corrente
        frame.setTitle("Turno di: " + giocatore.getNome());
    }

    @Override
    public void spostaGiocatoreCurr(Posizione posizione) {
        giocatoreCurr.spostaGiocatore(posizione);
        panel.repaint();
    }

    //Scritto da claude
    @Override
    public void animaMossaGiocatore(List<Posizione> posizioni) {
        Timer timer = new Timer(200, null); // 200ms tra ogni movimento
        final int[] currentStep = {0};

        timer.addActionListener(e -> {
            if (currentStep[0] < posizioni.size()) {
                spostaGiocatoreCurr(posizioni.get(currentStep[0]));
                currentStep[0]++;
            } else {
                timer.stop();
                mediator.notifyPlayerStop();
            }
        });

        timer.start();
    }

    @Override
    public void mostraVincitore(Giocatore giocatoreCurr) {
        int option = JOptionPane.showOptionDialog(panel,
                "Hai vinto: " + giocatoreCurr.getNome(),
                "Lancio",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"OK"},
                "OK");
        //notifica su ok
        if (option == 0) {

        }
    }

}


