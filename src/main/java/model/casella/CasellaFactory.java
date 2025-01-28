package model.casella;

import lombok.Getter;
import view.CasellaGraficaSwing;

import java.awt.geom.Point2D;

@Getter
public class CasellaFactory {
    private final CasellaFlyweight casellaFlyweight;

    public CasellaFactory(CasellaFlyweight casellaFlyweight) {
        this.casellaFlyweight = casellaFlyweight;

    }
    public Casella createCasella(int indice, Point2D point, String tipo) {
        Casella casella = new Casella(casellaFlyweight, indice, point);
        switch(tipo) {
            case "Serpente":
                casella.setCasellaState(Casella.CasellaState.SERPENTE);
                break;
            case "Scala":
                casella.setCasellaState(Casella.CasellaState.SCALA);
                break;
        }
        return casella;
    }
}
