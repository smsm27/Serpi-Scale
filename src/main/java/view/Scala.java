package view;



import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Scala extends JPanel {
    private final CasellaGraficaSwing partenza;
    private final CasellaGraficaSwing destinazione;
    private BufferedImage scala;

    public Scala(CasellaGraficaSwing partenza, CasellaGraficaSwing destinazione) {
        this.partenza = partenza;
        this.destinazione = destinazione;

        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));

        caricaImmagini();
        aggiornaPosizione();
        aggiungiListenerCaselle();
    }

    private void caricaImmagini() {
        try {
            scala = ImageIO.read(new File("src/main/img/scala.png"));

        } catch (IOException e) {
            System.err.println("Errore nel caricamento delle immagini: " + e.getMessage());
        }
    }
    private void aggiungiListenerCaselle() {
        partenza.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(java.awt.event.ComponentEvent e) {
                aggiornaPosizione();
            }
        });

        destinazione.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(java.awt.event.ComponentEvent e) {
                aggiornaPosizione();
            }
        });
    }

    private void aggiornaPosizione() {
        // Calcola la posizione e dimensione del pannello per coprire entrambe le caselle
        int x = Math.min(partenza.getX(), destinazione.getX());
        int y = Math.min(partenza.getY(), destinazione.getY());
        int width = Math.abs(partenza.getX() - destinazione.getX()) + partenza.getWidth();
        int height = Math.abs(partenza.getY() - destinazione.getY()) + partenza.getHeight();

        setBounds(x, y, width, height);
        repaint(); // Ridisegna il serpente
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Centro della casella testa
        int startX = partenza.getX() - getX() + partenza.getWidth() / 2;
        int startY = partenza.getY() - getY() + partenza.getHeight() / 2;

        // Centro della casella coda
        int endX = destinazione.getX() - getX() + destinazione.getWidth() / 2;
        int endY = destinazione.getY() - getY() + destinazione.getHeight() / 2;

        // Calcolo vettore e angolo
        int dx = endX - startX;
        int dy = endY - startY;
        double angle = Math.atan2(dy, dx);
        double length = Math.sqrt(dx * dx + dy * dy);

        // Salva il contesto grafico
        AffineTransform oldTransform = g2.getTransform();

       //  Disegna il corpo
        g2.translate(startX, startY);
        g2.rotate(angle);

        if(scala != null) {
            Rectangle2D rect = new Rectangle2D.Double(0, -5, length, 10);
            TexturePaint texturePaint = new TexturePaint(scala,
                    new Rectangle2D.Double(0, -5, 20, 10));
            g2.setPaint(texturePaint);
            g2.fill(rect);
        }


        // Ripristina il contesto grafico
        g2.setTransform(oldTransform);
    }

}
