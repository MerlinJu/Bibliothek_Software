

import java.time.LocalDate;
import java.util.List;

public class Main extends Bibliothek {


    public static void main(String[] args) {
        System.out.println("Programm gestartet!");

        //Buch buch = new Buch("Harry Potter", "J.K. Rowling", "2-h", LocalDate.parse("2025-12-02"),
        //LocalDate.parse("2025-12-02").plusDays(30));


        //System.out.println("Buch: " + buch.titel + " (" + buch.medientyp + ")");

        //mediumAusleihen("Mensch", LocalDate.parse("2025-01-07"));

        mediumZurückgeben("Mensch");

        System.out.println(ladeMedienAusDatei());


        System.out.println("neues Medium Hinzufügen...");
        neuesMediumHinzufuegen("testTITEL", "testAUTOR", "1-f", Medientyp.BUCH);

        for (Medium medium : ladeMedienAusDatei()) {
            System.out.println(medium);
        }

    }
}