package model.mediator;


import model.Giocatore.Giocatore;
import model.GiocoManager;
import model.casella.Casella;
import model.controller.ControllerPlayerImpl;
import model.tabella.TabellaViewImpl;
import tools.Posizione;

import java.util.ArrayList;
import java.util.List;


public class MediatorImpl implements Mediator {

    private ControllerPlayerImpl controller;
    private TabellaViewImpl view;
    private GiocoManager manager;

    @Override
    public void start() {
        if(controller == null || view == null || manager == null)
            throw new NullPointerException();

        Giocatore giocatore = manager.getGiocatoreCurr();
        view.setGiocatoreCurr(giocatore);
        controller.setGiocatoreCorrente(giocatore);

        // Imposta lo stato iniziale del turno
        manager.setStatoTurno(GiocoManager.StatoTurno.IN_ATTESA_LANCIO);
        controller.attivaBottone();
    }

    @Override
    public void notifyDiceRoll(int risultato) {
        // Gestisci il lancio dei dadi
        manager.lancioDadi(risultato);
        view.mostraRisDadi(risultato);
        controller.disattivaBottone();
    }

    private void muoviGiocatore(int indiceDestinazione) {
        int indicePartenza = manager.getCasellaCorrente().getIndice();
        List<Posizione> posizioniIntermedie = new ArrayList<>();

        // Calcola il percorso
        int step = indicePartenza < indiceDestinazione ? 1 : -1;
        for (int i = indicePartenza; i != indiceDestinazione; i += step) {
            posizioniIntermedie.add(manager.getCasellaFromIndice(i).getPosizione());
        }
        // Aggiungi la posizione finale
        posizioniIntermedie.add(manager.getCasellaFromIndice(indiceDestinazione).getPosizione());


        // Aggiorna la posizione nel model
        manager.spostaGiocatoreCurr(indiceDestinazione);

        // Esegui l'animazione
        view.animaMossaGiocatore(posizioniIntermedie);
    }

    @Override
    public void notifyPlayerMove() {
        // Muovi il giocatore verso la destinazione
        muoviGiocatore(manager.getCasellaDestinazione().getIndice());

        // Aggiorna lo stato del turno
        manager.setStatoTurno(GiocoManager.StatoTurno.AZIONE_CASELLA);
    }

    @Override
    public void notifyTurnChange() {
        // Gestisci la fine del turno
        if (manager.haTurniAggiuntivi()) {
            // Se ci sono turni aggiuntivi, non cambiare giocatore
            manager.incrementaTurniAggiuntivi();
            manager.setStatoTurno(GiocoManager.StatoTurno.IN_ATTESA_LANCIO);
            controller.attivaBottone();
        } else {
            // Cambia giocatore
            Giocatore giocatore = manager.cambiaGiocatoreCurr();
            view.setGiocatoreCurr(giocatore);
            controller.setGiocatoreCorrente(giocatore);
            controller.attivaBottone();
        }
    }

    @Override
    public void notifyPlayerStop() {
        // Esegui l'azione sulla casella corrente
        manager.eseguiAzioneSuCasella();
    }

    @Override
    public void registerController(ControllerPlayerImpl controller) {
        this.controller = controller;
        controller.setMediator(this);
    }

    @Override
    public void registerView(TabellaViewImpl view) {
        this.view = view;
    }

    @Override
    public void registerGameManager(List<Giocatore> giocatori, List<Casella> tabella) {
        this.manager = new GiocoManager(giocatori, tabella);
        manager.setMediator(this);
    }

    public void giocoFinito() {
        view.mostraVincitore(manager.getGiocatoreCurr());

    }
}
