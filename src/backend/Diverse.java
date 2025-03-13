package backend;

import java.time.LocalDate;

public class Diverse extends Medium {
    public Diverse(String titel, String autor, LocalDate ausleihe_datum, LocalDate rueckgabe_datum, Status status) {
        super(titel, autor, Medientyp.DIVERSE, ausleihe_datum, rueckgabe_datum, status);
    }

    public Diverse(String titel, String autor, String standplatz, Status bemerkung) {
        super(titel, autor, standplatz, Medientyp.DIVERSE, bemerkung);
    }
}
