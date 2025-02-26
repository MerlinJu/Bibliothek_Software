// Medium Basisklasse mit allen wichtigen Attributen von einem Medium

import java.time.LocalDate; // Java time klasse um LocalDate zu nutzen

public class Medium {
    public String titel;
    public String autor;
    public String standplatz;
    public Medientyp medientyp;
    public LocalDate ausleihe_datum;
    public LocalDate rueckgabe_datum;

    public Medium(String titel, String autor, String standplatz, Medientyp medientyp,
                  LocalDate ausleihe_datum, LocalDate rueckgabe_datum) {
        this.titel = titel;
        this.autor = autor;
        this.standplatz = standplatz;
        this.medientyp = medientyp;
        this.ausleihe_datum = ausleihe_datum;
        this.rueckgabe_datum = rueckgabe_datum;
    }


    @Override
    public String toString() {
        return titel + ";" + autor + ";" + standplatz + ";" + medientyp + ";" +
                (ausleihe_datum != null ? ausleihe_datum : null) + ";" +
                (rueckgabe_datum != null ? rueckgabe_datum : null);
    }

}
