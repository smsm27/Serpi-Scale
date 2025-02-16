package model.casella;

import lombok.Getter;
import lombok.Setter;
import tools.Posizione;

import java.io.Serializable;

@Getter
@Setter
public class Casella implements Serializable {

    private Posizione posizione;
    private int indice;
    private CasellaFlyweight casellaFlyweight;

    private Casella destinazione=null;
    private CasellaState casellaState;

    public Casella(CasellaFlyweight casellaFlyweight, int indice, Posizione posizione) {
        this.casellaFlyweight=casellaFlyweight;
        this.indice=indice;
        this.posizione=posizione;
        this.casellaState=CasellaState.NORMALE;

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
        SERPENTE,
        FINALE
    }
}
