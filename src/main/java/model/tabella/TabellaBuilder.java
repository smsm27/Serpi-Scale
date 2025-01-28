package model.tabella;


import lombok.Getter;
import lombok.Setter;
import model.casella.Casella;
import model.casella.CasellaFactory;
import model.casella.CasellaFlyweight;
import model.casella.CasellaGrafica;
import view.CasellaGraficaSwing;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TabellaBuilder {

    private final CasellaFactory casellaFactory;
    private final List<Casella> tabella;
    private final int numero_caselle;
    private final int righe, colonne; //Settabili dall'utente (interazione client con builder setter())

    public TabellaBuilder(Builder builder) {
        this.casellaFactory = builder.casellaFactory;
        this.numero_caselle = builder.numero_caselle;
        this.righe = builder.righe;
        this.colonne = builder.colonne;
        this.tabella = builder.tabella;
    }

    public static class Builder {
        private final CasellaFactory casellaFactory;
        private final List<Casella> tabella=new ArrayList<Casella>();
        private final int numero_caselle;
        CasellaFlyweight casellaFlyweight;
        //Optional
        private int righe = 10;
        private int colonne = 10;


        public Builder(int numero_caselle, CasellaFlyweight casellaFlyweight) {
            this.numero_caselle = numero_caselle;
            this.casellaFlyweight = casellaFlyweight;
            this.casellaFactory=new CasellaFactory(casellaFlyweight);
        }//builder

        public Builder righe(int righe){
            this.righe=righe;
            return this;
        }
        public Builder colonne(int colonne){
            this.colonne=colonne;
            return this;
        }

        public TabellaBuilder build(){
            int i=0;
            for(int r=0; r<righe; r++)
                for(int c=0; c<colonne; c++){
                    this.tabella.add(casellaFactory.createCasella(i,new Point2D.Double(r*casellaFlyweight.getLarghezza(),c*casellaFlyweight.getAltezza()),"Normale"));
                    i++;
                }
            return new TabellaBuilder(this);
        }
    }
}
