package model.tabella;

import model.Giocatore.Giocatore;
import model.GiocoManager;
import model.casella.Casella;
import model.casella.CasellaGrafica;
import model.mediator.Mediator;
import tools.Posizione;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public  abstract class TabellaViewImpl implements APICreaPartita  {




    @Override
    public Giocatore setGiocatore(String nome, Posizione posizione, Color color) {
        return new Giocatore(nome, posizione, color);
    }

    @Override
    public GiocoManager setGiocoManager(List<Giocatore> giocatori, List<Casella> tabella) {
        return new GiocoManager(giocatori, tabella);
    }

    public abstract void mostraRisDadi(int risultato);

    public abstract void spostaGiocatoreCurr(Posizione posizione);

    public abstract void setGiocatoreCurr(Giocatore giocatore);

    public abstract void animaMossaGiocatore(List<Posizione> posizioniIntermedie);

    public abstract void mostraVincitore(Giocatore giocatoreCurr) ;
}

