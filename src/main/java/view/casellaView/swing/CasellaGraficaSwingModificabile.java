package view.casellaView.swing;


import lombok.Getter;
import lombok.Setter;
import model.casella.Casella;
import model.casella.CasellaGrafica;
import tools.Posizione;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;




@Getter
@Setter
public class CasellaGraficaSwingModificabile extends CasellaGraficaSwing  {

    private Casella casella;
    private JLabel numeroLabel; // Etichetta per il numero della casella
    private ImageIcon immagine;


    public CasellaGraficaSwingModificabile(Casella casella) {
        this.casella = casella;
        // Impostazioni di base
        setBounds((int)casella.getPosizione().getX(),(int) casella.getPosizione().getY(),(int) casella.getLarghezza(),(int) casella.getAltezza());
        setLayout(null);

        // Aggiungi il numero come JLabel
        setNumeroLabel();

        // Aggiungi il menu contestuale
        aggiungiMenuContestuale();

        // Aggiungi listener per il trascinamento
        aggiungiDragListener();
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

    @Override
    public void setNumeroLabel(){
        numeroLabel = new JLabel(String.valueOf(casella.getIndice()), SwingConstants.CENTER);
        numeroLabel.setBounds(0, 0, 20, 20); // Occupa tutta la casella
        numeroLabel.setForeground(Color.BLACK); // Colore del testo
        numeroLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Font personalizzato
        add(numeroLabel);
    }

    //  menu
    @Override
    public void aggiungiMenuContestuale() {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem modificaPosizione = new JMenuItem("Modifica Posizione");
        JMenuItem modificaImmagine = new JMenuItem("Modifica Immagine");
        JMenuItem modificaDimensioni = new JMenuItem("Modifica Dimensioni");

        // Listener per ogni azione
        modificaPosizione.addActionListener(e -> spostaCasella());
        modificaImmagine.addActionListener(e -> cambiaImmagine());
        modificaDimensioni.addActionListener(e -> cambiaDimensioni());

        menu.add(modificaPosizione);
        menu.add(modificaImmagine);
        menu.add(modificaDimensioni);

        // Mostra il menu al clic destro
        this.setComponentPopupMenu(menu);
    }

    // listener
    @Override
    public void aggiungiDragListener() {
        MouseAdapter dragAdapter = new MouseAdapter() {
            private Point offset;

            @Override
            public void mousePressed(MouseEvent e) {
                offset = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point nuovoPunto = SwingUtilities.convertPoint(CasellaGraficaSwingModificabile.this, e.getPoint(), getParent());
                setLocation(nuovoPunto.x - offset.x, nuovoPunto.y - offset.y);
                casella.setPosizione(new Posizione(nuovoPunto.x - offset.x, nuovoPunto.y-offset.y));
            }
        };

        this.addMouseListener(dragAdapter);
        this.addMouseMotionListener(dragAdapter);
    }

    // Azione: Sposta Casella
    @Override
    public void spostaCasella() {
        String nuovoX = JOptionPane.showInputDialog(this, "Nuova X:", casella.getPosizione().getX());
        String nuovoY = JOptionPane.showInputDialog(this, "Nuova Y:", casella.getPosizione().getY());

        try {
            // Aggiorno coordinate della casella
            int nuovaX = Integer.parseInt(nuovoX);
            int nuovaY = Integer.parseInt(nuovoY);
            // Aggiorno posizione grafica del componente
            setLocation(nuovaX, nuovaY);
            casella.setPosizione(new Posizione(nuovaX,nuovaY));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Inserire valori numerici validi.");
        }
    }

    // Azione: Cambia Immagine
    @Override
    public void cambiaImmagine() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            immagine = new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath());
            repaint();
        }
    }

    // Azione: Cambia Dimensioni
    @Override
    public void cambiaDimensioni() {
        String nuovaLarghezza = JOptionPane.showInputDialog(this, "Nuova Larghezza:", casella.getLarghezza());
        String nuovaAltezza = JOptionPane.showInputDialog(this, "Nuova Altezza:", casella.getAltezza());

        try {
            casella.getCasellaFlyweight().setLarghezza(Double.parseDouble(nuovaLarghezza));
            casella.getCasellaFlyweight().setAltezza(Double.parseDouble(nuovaAltezza));
            setSize((int)casella.getLarghezza(),(int) casella.getAltezza());
            repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Inserire valori numerici validi.");
        }
    }
}
