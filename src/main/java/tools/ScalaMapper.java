package tools;

import model.CaselleSpeciali.Scala;

import view.casellaView.swing.CasellaGraficaSwing;
import view.casellaView.swing.CasellaGraficaSwingOnlyRead;
import view.casellaView.swing.CasellaGraficaSwingModificabile;
import view.ScalaSwing;


import java.util.ArrayList;
import java.util.List;

public class ScalaMapper {

    public static List<Scala> fromGraficaToLogic(List<ScalaSwing> grafica) {
        List<Scala> scale = new ArrayList<Scala>();
        for (ScalaSwing scalaGra : grafica) {
            Scala s=new Scala(scalaGra.getPartenza().getCasella().getIndice(),scalaGra.getDestinazione().getCasella().getIndice());
            scale.add(s);
        }
        return scale;
    }

    public static List<ScalaSwing> fromLogicToGrafica(List<Scala> logic, List<? extends CasellaGraficaSwing> grafica) {
        List<ScalaSwing> scale = new ArrayList<ScalaSwing>();
        for (Scala scala : logic) {
            ScalaSwing serpenteSwing=new ScalaSwing( grafica.get(scala.getCasellaArrivo()),  grafica.get(scala.getCasellaPartenza()));
            scale.add(serpenteSwing);
        }
        return scale;
    }


}
