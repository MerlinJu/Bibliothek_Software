// Medium Basisklasse mit allen wichtigen Attributen von einem Medium

import java.time.LocalDate; // Java time klasse um LocalDate zu nutzen

public class Medium {
    public String titel;
    public String autor;
    public String standplatz;
    public Medientyp medientyp;
    public LocalDate ausleihe_datum;
    // Hinweis, ich habe das Rückgabedatum komplett entfernt, da es unerklärliche Fehler damit gab

    public Medium(String titel, String autor, String standplatz, Medientyp medientyp,
                  LocalDate ausleihe_datum) {
        this.titel = titel;
        this.autor = autor;
        this.standplatz = standplatz;
        this.medientyp = medientyp;
        this.ausleihe_datum = ausleihe_datum;
    }

    @Override
    public String toString() {
        return titel + ";" +
                autor + ";" +
                standplatz + ";" +
                medientyp + ";" +
                ausleihe_datum;
    }

}
