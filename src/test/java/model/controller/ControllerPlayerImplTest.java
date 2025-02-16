package model.controller;






import model.Giocatore.Giocatore;
import model.GiocoManager;
import model.casella.Casella;
import model.casella.CasellaFlyweight;
import model.tabella.TabellaFactory;
import model.tabella.TabellaViewImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import view.tabellaView.TabellaViewSwing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControllerPlayerImplTest {
    private ControllerPlayerImpl controller;
    private TabellaViewImpl view;
    private GiocoManager manager;

    @BeforeEach
    public void setup() {
        int nCaselle = 25;
        CasellaFlyweight fly = new CasellaFlyweight(100, 100, null);
        TabellaFactory tabellaFactory = new TabellaFactory(fly);

        List<Casella> caselle = tabellaFactory.creaTabellone(nCaselle);

        List<Giocatore> giocatori = new ArrayList<>();
        giocatori.add(new Giocatore("TestPlayer1", caselle.getFirst().getPosizione(), Color.RED));

        view = new TabellaViewSwing();
        manager = new GiocoManager(giocatori, caselle);
        controller = spy(new ControllerPlayerImpl(view, manager));
    }

    @Test
    public void testPartitaCompletaConDadoFisso() {
        // Setup del gioco
        int nCaselle = 25;
        CasellaFlyweight fly = new CasellaFlyweight(100, 100, null);
        TabellaFactory tabellaFactory = new TabellaFactory(fly);
        List<Casella> caselle = tabellaFactory.creaTabellone(nCaselle);

        // Crea due giocatori per il test
        List<Giocatore> giocatori = new ArrayList<>();
        giocatori.add(new Giocatore("Player1", caselle.getFirst().getPosizione(), Color.RED));
        giocatori.add(new Giocatore("Player2", caselle.getFirst().getPosizione(), Color.BLUE));

        // Mock della view invece di creare una vera TabellaViewSwing
        TabellaViewImpl view = mock(TabellaViewImpl.class);
        GiocoManager manager = new GiocoManager(giocatori, caselle);
        ControllerPlayerImpl controller = spy(new ControllerPlayerImpl(view, manager));

        // Forza il risultato del dado a 3
        doReturn(3).when(controller).getRisDadi();

        // Simula alcuni turni di gioco
        for(int i = 0; i < 5; i++) {
            System.out.println("Turno " + (i+1));
            System.out.println("Giocatore corrente: " + manager.getGiocatoreCurr().getNome());

            int posizioneIniziale = manager.getGiocatoreCurr().getIndiceCurr();
            controller.lanciaDadi();
            System.out.println("Posizione iniziale: " + posizioneIniziale);
            System.out.println("Nuova posizione: " + manager.getGiocatoreCurr().getIndiceCurr());
            System.out.println("------------------------");

            controller.setGiocatoreCorrente();
        }
    }


    @Test
    public void testMovimentoGiocatore() {
        int posizioneIniziale = manager.getGiocatoreCurr().getIndiceCurr();

        // Forza il risultato del dado a 3
        doReturn(3).when(controller).getRisDadi();

        controller.lanciaDadi();
        assertEquals(posizioneIniziale + 3, manager.getGiocatoreCurr().getIndiceCurr());
    }

    @Test
    public void testCambioGiocatore() {
        Giocatore giocatoreIniziale = manager.getGiocatoreCurr();

        // Forza il risultato del dado a 3
        doReturn(3).when(controller).getRisDadi();

        controller.lanciaDadi();
        controller.setGiocatoreCorrente();
        assertNotEquals(giocatoreIniziale, manager.getGiocatoreCurr());
    }
}