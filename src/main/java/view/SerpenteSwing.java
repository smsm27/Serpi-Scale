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

public class SerpenteSwing extends JPanel  {
    private final CasellaGraficaSwing casellaTesta;
    private final CasellaGraficaSwing casellaCoda;
    private BufferedImage texture;
    private BufferedImage testaImage;
    private BufferedImage codaImage;

    public SerpenteSwing(CasellaGraficaSwing partenza, CasellaGraficaSwing destinazione) {
        this.casellaTesta = partenza;
        this.casellaCoda = destinazione;

        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));

        caricaImmagini();
        aggiornaPosizione();
        aggiungiListenerCaselle();
    }


    public void caricaImmagini() {
        try {
            texture = ImageIO.read(new File("src/main/img/serpenteBody.png"));
            testaImage = ImageIO.read(new File("src/main/img/serpenteTp.png"));
            codaImage = ImageIO.read(new File("src/main/img/serpenteC.png"));
        } catch (IOException e) {
            System.err.println("Errore nel caricamento delle immagini: " + e.getMessage());
        }
    }


    public void aggiungiListenerCaselle() {
        casellaTesta.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(java.awt.event.ComponentEvent e) {
                aggiornaPosizione();
            }
        });

        casellaCoda.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(java.awt.event.ComponentEvent e) {
                aggiornaPosizione();
            }
        });
    }


    public void aggiornaPosizione() {
        // Calcola la posizione e dimensione del pannello per coprire entrambe le caselle
        int x = Math.min(casellaTesta.getX(), casellaCoda.getX());
        int y = Math.min(casellaTesta.getY(), casellaCoda.getY());
        int width = Math.abs(casellaTesta.getX() - casellaCoda.getX()) + casellaTesta.getWidth();
        int height = Math.abs(casellaTesta.getY() - casellaCoda.getY()) + casellaTesta.getHeight();

        setBounds(x, y, width, height);
        repaint(); // Ridisegna il serpente
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Centro della casella testa
        int startX = casellaTesta.getX() - getX() + casellaTesta.getWidth() / 2;
        int startY = casellaTesta.getY() - getY() + casellaTesta.getHeight() / 2;

        // Centro della casella coda
        int endX = casellaCoda.getX() - getX() + casellaCoda.getWidth() / 2;
        int endY = casellaCoda.getY() - getY() + casellaCoda.getHeight() / 2;

        // Calcolo vettore e angolo
        int dx = endX - startX;
        int dy = endY - startY;
        double angle = Math.atan2(dy, dx);
        double length = Math.sqrt(dx * dx + dy * dy);

        // Salva il contesto grafico
        AffineTransform oldTransform = g2.getTransform();

        // Disegna il corpo
        g2.translate(startX, startY);
        g2.rotate(angle);

        if(texture != null) {
            Rectangle2D rect = new Rectangle2D.Double(0, -5, length, 10);
            TexturePaint texturePaint = new TexturePaint(texture,
                    new Rectangle2D.Double(0, -5, 20, 10));
            g2.setPaint(texturePaint);
            g2.fill(rect);
        }

        // Ripristina e disegna la testa
        g2.setTransform(oldTransform);
        if(testaImage != null) {
            g2.translate(startX, startY);
            g2.rotate(angle);
            g2.drawImage(testaImage,
                    -testaImage.getWidth()/2,
                    -testaImage.getHeight()/2,
                    null);
        }

        // Ripristina e disegna la coda
        g2.setTransform(oldTransform);
        if(codaImage != null) {
            g2.translate(endX, endY);
            g2.rotate(angle + Math.PI); // Ruota di 180° in più per la coda
            g2.drawImage(codaImage,
                    -codaImage.getWidth()/2,
                    -codaImage.getHeight()/2,
                    null);
        }

        // Ripristina il contesto grafico
        g2.setTransform(oldTransform);
    }
}


