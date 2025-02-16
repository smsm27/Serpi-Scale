package model.casella;

import view.casellaView.swing.CasellaGraficaSwingModificabile;

public class CasellaGraficaFactory {


    public static CasellaGrafica createCasellaGrafica(Casella casella, String tipo) {
        switch(tipo.toLowerCase()) {
            case "swing":
                return new CasellaGraficaSwingModificabile(casella);
            default:
                return null;
        }

    }
}
