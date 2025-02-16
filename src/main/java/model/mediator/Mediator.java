package model.mediator;

import model.Giocatore.Giocatore;
import model.casella.Casella;
import model.controller.ControllerPlayerImpl;
import model.tabella.TabellaViewImpl;

import java.util.List;

public interface Mediator {

    void start();
    void notifyDiceRoll(int risultato);
    void notifyPlayerMove( );
    void notifyTurnChange();
    void registerController(ControllerPlayerImpl controller);
    void registerView(TabellaViewImpl view);
    void registerGameManager(List<Giocatore> giocatori, List<Casella> tabella);

    void notifyPlayerStop();
}
