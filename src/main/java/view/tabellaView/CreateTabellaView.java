package view.tabellaView;

import model.casella.Casella;

import java.util.List;

public interface CreateTabellaView {

    void inizializza();
    void mostraTabellaGrafica(List<Casella> caselle);
    void aggiungiElementoGrafico(String tipo, int partenza, int destinazione);
    void salvaTabellaStato(String nomeFile);
    void caricaTabellaStato(String nomeFile);
    void refresh();
    void mostraMessaggio(String messaggio, String titolo, int tipoMessaggio);
}
