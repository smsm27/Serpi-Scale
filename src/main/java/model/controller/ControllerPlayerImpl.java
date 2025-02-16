package model.controller;

import lombok.Getter;


import lombok.Setter;
import model.Giocatore.Giocatore;
import model.mediator.MediatorImpl;

import java.util.Random;


public abstract class ControllerPlayerImpl implements ControllerPlayer {
    Random random;
    @Setter
    private MediatorImpl mediator;
    @Getter

    private Giocatore GiocatoreCorrente;

    public void setGiocatoreCorrente(Giocatore GiocatoreCorrente) {
        this.GiocatoreCorrente=GiocatoreCorrente;
        setCurrentPlayerLabel();
    }

    public ControllerPlayerImpl(  ) {
        random = new Random();
    }



    @Override
    public void lanciaDadi() {
        int risDadi = random.nextInt(6) + 1;
        mediator.notifyDiceRoll(risDadi);
    }


    public abstract void disattivaBottone();

    public abstract void attivaBottone();

    public abstract void setCurrentPlayerLabel();


}
