package model.tabella;

import model.Giocatore.Giocatore;
import model.GiocoManager;
import model.casella.Casella;
import tools.Posizione;

import java.awt.*;
import java.util.List;

public interface APICreaPartita {


    Giocatore setGiocatore(String nome, Posizione posizione, Color color);

    GiocoManager setGiocoManager(List<Giocatore>giocatori, List<Casella>tabella);



}
