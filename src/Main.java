

import java.time.LocalDate;
import java.util.List;

public class Main extends Bibliothek {


    public static void main(String[] args) {
        System.out.println("Programm gestartet!");

        // Alle Medien auflisten
        for (Medium medium : ladeMedien()) {
            System.out.println(medium);
        }

        System.out.println("neues Medium Hinzufügen...");
        neuesMediumHinzufuegen("testTITEL", "testAUTOR", "1-g", Medientyp.BUCH);

    }
}