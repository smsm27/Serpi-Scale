package tools;

public enum StatoGioco {
    FIRST_LOAD,  // Caricamento Tabella giocatori (Superfluo?)
    IN_ATTESA,   // Aspetta input utente (es. lancio dadi)
    MOVIMENTO,   // Pedina in movimento (blocca input)
    EFFETTO      // Gestione effetti casella (es. scala/serpente)
}
