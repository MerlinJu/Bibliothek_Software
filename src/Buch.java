import java.time.LocalDate;

public class Buch extends Medium {
    public Buch(String titel, String autor, String standplatz, LocalDate ausleihe_datum, LocalDate rueckgabe_datum) {
        super(titel, autor, standplatz, Medientyp.BUCH, ausleihe_datum, rueckgabe_datum);
    }
}