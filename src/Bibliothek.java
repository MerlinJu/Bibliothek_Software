import java.awt.datatransfer.SystemFlavorMap;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Bibliothek Klasse, um den Medien Bestand zu verwalten und einzulesen aus der medien.txt file
public class Bibliothek {
    private final String dateipfad = "src/medien.txt"; // Pfad zu der medien datein, welche den kompletten medienbestand beinhaltet


    // Medien aus Datei laden
    public List<Medium> ladeMedienAusDatei() {
        List<Medium> medienListe = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dateipfad) )) {
            String zeile;
            while ((zeile = br.readLine()) != null) {
                String[] teile = zeile.split(";");
                if (teile.length == 4) {
                    String titel = teile[0];
                    String autor = teile[1];
                    String standplatz = teile[2];
                    Medientyp typ = Medientyp.valueOf(teile[3]);

                    Medium medium = new Medium(titel, autor, standplatz, typ);
                    medienListe.add(medium);
                }
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Datei: " + e.getMessage());
        }
        return medienListe;
    }








}
