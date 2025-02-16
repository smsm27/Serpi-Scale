package model.CaselleSpeciali;



import lombok.Getter;

import java.io.Serializable;
@Getter
public class Serpente implements Serializable {
    private final int casellaPartenza;
    private final int casellaArrivo;


    public Serpente(int casellaPartenza, int casellaArrivo) {
        this.casellaPartenza = casellaPartenza;
        this.casellaArrivo = casellaArrivo;
    }


}
