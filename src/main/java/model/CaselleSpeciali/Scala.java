package model.CaselleSpeciali;

import lombok.Getter;

import java.io.Serializable;
@Getter
public class Scala implements Serializable {
    private final int casellaPartenza;
    private final int casellaArrivo;

    public Scala(int casellaPartenza, int casellaArrivo) {
        this.casellaPartenza = casellaPartenza;
        this.casellaArrivo = casellaArrivo;
    }


}
