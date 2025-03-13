package backend;

import java.time.LocalDate;

public class Datenträger extends Medium {
    public Datenträger(String titel, String autor, LocalDate ausleihe_datum, LocalDate rueckgabe_datum, Status status) {
        super(titel, autor, Medientyp.DATENTRÄGER, ausleihe_datum, rueckgabe_datum, status);
    }

    public Datenträger(String titel, String autor, String standplatz, Status bemerkung) {
        super(titel, autor, standplatz, Medientyp.DATENTRÄGER, bemerkung);
    }
}

