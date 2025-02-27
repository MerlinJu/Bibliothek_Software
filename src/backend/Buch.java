package backend;

import java.time.LocalDate;

public class Buch extends Medium {
    public Buch(String titel, String autor, LocalDate ausleihe_datum, LocalDate rueckgabe_datum) {
        super(titel, autor, Medientyp.BUCH, ausleihe_datum, rueckgabe_datum);
    }

    public Buch(String titel, String autor, String standplatz) {
        super(titel, autor, standplatz, Medientyp.BUCH);
    }
}