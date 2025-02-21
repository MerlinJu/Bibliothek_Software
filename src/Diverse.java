import java.time.LocalDate;

public class Diverse extends Medium {
    public Diverse(String titel, String autor, String standplatz, LocalDate ausleihe_datum) {
        super(titel, autor, standplatz, Medientyp.DIVERSE, ausleihe_datum);
    }
}
