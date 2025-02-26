

import java.time.LocalDate;
import java.util.List;

public class Main extends Bibliothek {


    public static void main(String[] args) {
        System.out.println("Programm gestartet!");


        //mediumAusleihen("Mensch", LocalDate.parse("2025-01-07"));

        mediumZurückgeben("Mensch", "1-d");

        //System.out.println(ladeMedienAusDatei());


//        System.out.println("neues Medium Hinzufügen...");
//        neuesMediumHinzufuegen("testTITEL", "testAUTOR", "1-f", Medientyp.BUCH);
//
//        for (Medium medium : ladeMedienAusDatei()) {
//            System.out.println(medium);
//        }

    }
}