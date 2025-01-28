package model.casella;

public interface CasellaGrafica {

    void inizializza(Casella casella);
    void setNumeroLabel();
    void aggiungiMenuContestuale();
    void aggiungiDragListener();
    void spostaCasella();
    void setImmagine();
    void cambiaImmagine();
    void cambiaDimensioni();
}
