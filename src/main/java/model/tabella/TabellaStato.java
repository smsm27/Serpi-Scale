package model.tabella;

import lombok.Getter;
import lombok.Setter;
import model.CaselleSpeciali.Scala;
import model.casella.Casella;
import model.CaselleSpeciali.Serpente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TabellaStato implements Serializable {
    private List<Casella> caselle=new ArrayList<Casella>();
    private List<Serpente> serpenti=new ArrayList<>();
    private List<Scala> scale=new ArrayList<>();



}