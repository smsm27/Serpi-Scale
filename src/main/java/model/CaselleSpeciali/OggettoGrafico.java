package model.CaselleSpeciali;

import model.casella.Casella;
import model.casella.CasellaGrafica;

public interface OggettoGrafico {
    void caricaImmagini();
    void aggiungiListenerCaselle();
    void aggiornaPosizione();
    CasellaGrafica getPartenza();
    CasellaGrafica getDestinazione();
}
