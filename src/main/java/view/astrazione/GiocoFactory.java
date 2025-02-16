package view.astrazione;

import model.Giocatore.Giocatore;
import model.tabella.TabellaStato;

import java.util.List;

public interface GiocoFactory {

    TabellaStato caricaTabella();
    void salvaTabella();
    void inizializzaCasellaFactory();
    void inizializzaController();
    void mostraRisultatoDado(int risultato);
    void caricaGiocatori(List<Giocatore> giocatori);
    Giocatore getInfoPlayers();

}
