package model.casella;

import lombok.Getter;
import lombok.Setter;
import view.CasellaGraficaSwing;

import java.awt.geom.Point2D;

@Getter
@Setter
public class Casella {

    private Point2D posizione;
    private int indice;
    private CasellaFlyweight casellaFlyweight;
    private CasellaGrafica casellaGrafica;
    private Casella destinazione=null;
    private CasellaState casellaState;

    public Casella(CasellaFlyweight casellaFlyweight, int indice, Point2D posizione) {
        this.casellaFlyweight=casellaFlyweight;
        this.indice=indice;
        this.posizione=posizione;
        this.casellaState=CasellaState.NORMALE;
        casellaGrafica=new CasellaGraficaSwing(this);
    }

    public double getLarghezza(){
        return casellaFlyweight.getLarghezza();
    }
    public double getAltezza(){
        return casellaFlyweight.getAltezza();
    }
    public String getImageURL(){
        return casellaFlyweight.getImageURL();
    }

    public enum CasellaState{
        NORMALE,
        SCALA,
        SERPENTE
    }
}
