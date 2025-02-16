package view;

import lombok.Getter;
import model.controller.ControllerPlayerImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControllerPlayerImplSwing extends ControllerPlayerImpl {
    @Getter
    private JLayeredPane panel;

    private final JButton lancioDadoButton;
    private JLabel currentPlayerLabel;



    public ControllerPlayerImplSwing() {
        panel = new JLayeredPane();
        panel.setSize(800,100);
        panel.setLayout(new BorderLayout());

        // Etichetta per il giocatore corrente
        currentPlayerLabel = new JLabel("Giocatore corrente: " );
        currentPlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Bottone per il lancio dei dadi

        lancioDadoButton = new JButton("Lancia Dadi");
        lancioDadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lanciaDadi();
            }
        });

        panel.add(currentPlayerLabel, BorderLayout.NORTH);
        panel.add(lancioDadoButton, BorderLayout.SOUTH);

    }

    public void setCurrentPlayerLabel(){
        currentPlayerLabel.setText("Giocatore corrente: " + getGiocatoreCorrente().getNome() +" Indice corrente:  "+ getGiocatoreCorrente().getIndiceCurr() );
    }



    @Override
    public void disattivaBottone(){
        lancioDadoButton.setVisible(false);
    }
    @Override
    public void attivaBottone(){
        lancioDadoButton.setVisible(true);
    }


}
