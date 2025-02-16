package view.casellaView.swing;

import lombok.Getter;
import lombok.Setter;
import model.casella.Casella;
import model.casella.CasellaGrafica;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
/*
Possibile ottimizzazione nel caso in cui devo solo stampare la tabella
e non devo modificarla (stato gioca)
 */
@Getter
@Setter
public class CasellaGraficaSwingOnlyRead extends CasellaGraficaSwing {

    private Casella casella;
    private JLabel numeroLabel; // Etichetta per il numero della casella
    private ImageIcon immagine;




    public CasellaGraficaSwingOnlyRead(Casella casella) {
        this.casella = casella;
        // Impostazioni di base
        setBounds((int) casella.getPosizione().getX(), (int) casella.getPosizione().getY(), (int) casella.getLarghezza(), (int) casella.getAltezza());
        setLayout(null);

        // Aggiungi il numero come JLabel
        setNumeroLabel();
    }
    // Disegna la casella
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (immagine != null) {
            g.drawImage(immagine.getImage(), 0, 0, (int)casella.getLarghezza(), (int)casella.getAltezza(), this);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, (int)casella.getLarghezza(), (int)casella.getAltezza());
        }
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, (int)casella.getLarghezza() - 1, (int)casella.getAltezza() - 1);
    }

    @Override
    public void setNumeroLabel() {
        numeroLabel = new JLabel(String.valueOf(casella.getIndice()), SwingConstants.CENTER);
        numeroLabel.setBounds(0, 0, 20, 20); // Occupa tutta la casella
        numeroLabel.setForeground(Color.BLACK); // Colore del testo
        numeroLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Font personalizzato
        add(numeroLabel);
    }

    @Override
    public void setImmagine() {
// Ottieni l'URL dall'oggetto Casella
        String urlImmagine = casella.getImageURL(); // Assicurati che Casella abbia un metodo getUrlImmagine()

        try {
            // 1. Converti la stringa in URI (gestisce codifica e validazione)
            URI uri = new URI(urlImmagine);

            // 2. Converti URI in URL
            URL url = uri.toURL();

            // 3. Carica l'immagine
            immagine = new ImageIcon(url);

        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
            immagine = new ImageIcon(); // Fallback vuoto
        } catch (Exception e) {
            e.printStackTrace();
            immagine = new ImageIcon("path/to/default/image.png"); // Fallback esplicito
        }
    }

}
