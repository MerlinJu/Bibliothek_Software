import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Programm gestartet!");

        Bibliothek bibliothek = new Bibliothek();

        List<Medium> medien = bibliothek.ladeMedienAusDatei();
        for (Medium medium : medien) {
            System.out.println(medium);
        }

    }
}