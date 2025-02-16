package model.casella;

public interface CasellaGrafica {

    void inizializza(Casella casella);
    void setNumeroLabel();
    void aggiungiMenuContestuale();
    void aggiungiDragListener();
    void spostaCasella();
    Casella getCasella();
    void setImmagine();
    void cambiaImmagine();
    void cambiaDimensioni();
}
