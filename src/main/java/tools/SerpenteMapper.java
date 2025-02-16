package tools;

import model.CaselleSpeciali.OggettoGrafico;

import model.CaselleSpeciali.Serpente;
import view.SerpenteSwing;
import view.casellaView.swing.CasellaGraficaSwing;
import view.casellaView.swing.CasellaGraficaSwingOnlyRead;

import java.util.ArrayList;
import java.util.List;

public  class SerpenteMapper {

    public static List<Serpente> fromGraficaToLogic(List<? extends OggettoGrafico> grafica) {
        List<Serpente> serpenti = new ArrayList<Serpente>();
        for (OggettoGrafico serpeGra : grafica) {
            Serpente s=new Serpente(serpeGra.getPartenza().getCasella().getIndice(),serpeGra.getDestinazione().getCasella().getIndice());
            serpenti.add(s);
        }
        return serpenti;
    }

    public static List<SerpenteSwing> fromLogicToSwing(List<Serpente> logic, List<? extends CasellaGraficaSwing> grafica) {
        List<SerpenteSwing> serpenti = new ArrayList<>();
        for (Serpente serp : logic) {
            SerpenteSwing serpenteSwing=
                    new SerpenteSwing( grafica.get(serp.getCasellaPartenza()),  grafica.get(serp.getCasellaArrivo()));
            serpenti.add(serpenteSwing);
        }
        return serpenti;
    }
}
