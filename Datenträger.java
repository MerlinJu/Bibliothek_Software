import java.time.LocalDate;

public class Datenträger extends Medium {
    public Datenträger(String titel, String autor, String standplatz, LocalDate ausleihe_datum, LocalDate rueckgabe_datum) {
        super(titel, autor, standplatz, Medientyp.DATENTRÄGER, ausleihe_datum, rueckgabe_datum);
    }
}
