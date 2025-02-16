package model;

import lombok.Getter;
import lombok.Setter;

import model.Giocatore.Giocatore;
import model.casella.Casella;
import model.mediator.MediatorImpl;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GiocoManager {
    // Enum per gli stati del turno
    public enum StatoTurno {
        IN_ATTESA_LANCIO,   // In attesa che il giocatore lanci i dadi
        MOVIMENTO,          // Giocatore sta muovendo il suo pedone
        AZIONE_CASELLA,     // Gestione delle azioni speciali della casella
        FINE_TURNO,// Turno completato, pronto per passare al prossimo giocatore
        GIOCO_FINITO
    }

    @Getter
    private Giocatore giocatoreCurr;

    @Getter
    @Setter
    private StatoTurno statoTurno;

    // Lavoro solo su indici
    private List<Casella> tabella;
    private List<Giocatore> giocatoriOrdinati;

    @Getter
    @Setter
    private int prossimiPassi;

    @Setter
    private MediatorImpl mediator;

    private int turno = 0;

    @Getter
    private int turniAggiuntivi = 0;

    public GiocoManager(List<Giocatore> giocatori, List<Casella> tabella) {
        this.giocatoriOrdinati = determinaOrdineGiocatori(giocatori);
        this.giocatoreCurr = giocatoriOrdinati.getFirst();
        this.tabella = tabella;
        this.statoTurno = StatoTurno.IN_ATTESA_LANCIO;
    }

    public List<Giocatore> determinaOrdineGiocatori(List<Giocatore> giocatori) {
        List<Giocatore> ordineGiocatori = new ArrayList<>(giocatori);
        Random random = new Random();

        // Fisher-Yates shuffle
        for (int i = ordineGiocatori.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            // Scambia gli elementi
            Giocatore temp = ordineGiocatori.get(i);
            ordineGiocatori.set(i, ordineGiocatori.get(j));
            ordineGiocatori.set(j, temp);
        }
        return ordineGiocatori;
    }

    public Giocatore cambiaGiocatoreCurr() {
        // Resetta i turni aggiuntivi quando cambia il giocatore
        turniAggiuntivi = 0;

        // Incrementa il turno solo dopo aver completato un giro completo
        turno = (turno + 1) % giocatoriOrdinati.size();

        this.giocatoreCurr = giocatoriOrdinati.get(turno);
        this.statoTurno = StatoTurno.IN_ATTESA_LANCIO;
        return giocatoreCurr;
    }

    public void spostaGiocatoreCurr(int indice) {
        this.giocatoreCurr.setPosizione(tabella.get(indice).getPosizione());
        this.giocatoreCurr.setIndiceCurr(indice);
    }

    public Casella getCasellaCorrente() {
        return tabella.get(giocatoreCurr.getIndiceCurr());
    }

    public Casella getCasellaDestinazione() {
        int indiceD = giocatoreCurr.getIndiceCurr() + prossimiPassi;
        if(indiceD < tabella.size()) {
            System.out.println("Vado verso " + indiceD);
            return tabella.get(indiceD);
        }else{
            return tabella.getLast();
        }


    }

    public Casella getCasellaFromIndice(int indice) {
        return tabella.get(indice);
    }

    public void lancioDadi(int risultato) {
        if (statoTurno != StatoTurno.IN_ATTESA_LANCIO) {
            throw new IllegalStateException("Impossibile lanciare i dadi in questo momento");
        }

        prossimiPassi = risultato;
        statoTurno = StatoTurno.MOVIMENTO;
    }

    public void eseguiAzioneSuCasella() {
        Casella c = getCasellaCorrente();
        System.out.println("--> azione parte 2"+c.getCasellaState().toString());

        switch (c.getCasellaState()) {
            case SCALA -> {
                prossimiPassi = c.getDestinazione().getIndice() - c.getIndice();
                mediator.notifyPlayerMove();
                statoTurno = StatoTurno.AZIONE_CASELLA;
            }
            case SERPENTE -> {
                prossimiPassi = c.getDestinazione().getIndice() - giocatoreCurr.getIndiceCurr();
                mediator.notifyPlayerMove();
                statoTurno = StatoTurno.AZIONE_CASELLA;
            }
            case NORMALE -> {
                prossimiPassi = 0;
                statoTurno = StatoTurno.FINE_TURNO;
                mediator.notifyTurnChange();
            }
            case FINALE ->{
                prossimiPassi = 0;
                statoTurno= StatoTurno.GIOCO_FINITO;
                mediator.giocoFinito();

            }
            default -> throw new IllegalStateException("Stato non gestito: " + c.getCasellaState());
        }
    }

    public boolean verificaDoppioSei(int lancio1, int lancio2) {
        return lancio1 == 6 && lancio2 == 6;
    }

    public void incrementaTurniAggiuntivi() {
        turniAggiuntivi++;
    }

    public boolean haTurniAggiuntivi() {
        return turniAggiuntivi > 0;
    }

}
