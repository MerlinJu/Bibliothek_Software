package backend;

public class Main extends Bibliothek {


    public static void main(String[] args) {
        System.out.println("Programm gestartet!");
//
//        // Alle Medien auflisten
//        for (Medium medium : ladeMedien()) {
//            System.out.println(medium);
//        }
//
//        System.out.println("neues Medium Hinzufügen...");
//        neuesMediumHinzufuegen("testTITEL", "testAUTOR", "1-g", Medientyp.BUCH);
//
//        LocalDate DatumHeute = LocalDate.now();
//        System.out.println("Ein Medium ausleihen...");
//        mediumAusleihen("Back in Black", DatumHeute);

        standplatzÄndern("Back in Black", "1-k&");
        standplatzÄndern("Thriller", "1-n");
    }
}