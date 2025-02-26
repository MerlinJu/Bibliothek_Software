// Medium Basisklasse mit allen wichtigen Attributen von einem Medium

import java.time.LocalDate; // Java time klasse um LocalDate zu nutzen

public class Medium {
    public String titel;
    public String autor;
    public String standplatz;
    public Medientyp medientyp;
    public LocalDate ausleihe_datum;
    public LocalDate rueckgabe_datum;

    // Konstruktor für nicht ausgeliehenes Medium (ohne Standplatz)
    public Medium(String titel, String autor, Medientyp medientyp, LocalDate ausleihe_datum, LocalDate rueckgabe_datum) {
        this.titel = titel;
        this.autor = autor;
        this.standplatz = null;
        this.medientyp = medientyp;
        this.ausleihe_datum = ausleihe_datum;
        this.rueckgabe_datum = rueckgabe_datum;
    }

    //Konstruktor für ausgeliehenes Medium (ohne Ausleih- und Rückgabedatum)
    public Medium(String titel, String autor, String standplatz, Medientyp medientyp){
        this.titel = titel;
        this.autor = autor;
        this.standplatz = standplatz;
        this.medientyp = medientyp;
        this.ausleihe_datum = null;
        this.rueckgabe_datum = null;
    }

    @Override
    public String toString() {

        return  titel +
                ";" + autor +
                (standplatz != null ? ";" + standplatz: "") +
                ";" + medientyp +
                (ausleihe_datum != null ? ";" + ausleihe_datum: "") +
                (rueckgabe_datum != null ? ";" + rueckgabe_datum : "");
    }

}
