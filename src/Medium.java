// Medium Basisklasse mit allen wichtigen Attributen von einem Medium

import java.time.LocalDate; // Java time klasse um LocalDate zu nutzen

public class Medium {
    public String titel;
    public String autor;
    public String standplatz;
    public Medientyp medientyp;
    public LocalDate ausleihe_datum;
    public LocalDate rueckgabe_datum;

    public Medium(String titel, String autor, String standplatz, Medientyp medientyp) {
        this.titel = titel;
        this.autor = autor;
        this.standplatz = standplatz;
        this.medientyp = medientyp;
    }
}
