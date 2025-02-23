import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse {@code Bibliothek} verwaltet den Medienbestand und ermöglicht das Laden von Medien
 * aus einer Datei. Sie liest eine Datei, die Medieninformationen im CSV-Format enthält, und erstellt
 * eine Liste von {@link Medium}-Objekten.
 * <p>
 * Die Medieninformationen beinhalten den Titel, Autor, Standplatz und den Medientyp.
 * </p>
 */
public class Bibliothek {
    private final static String dateipfad = "src/medien.txt"; // Pfad zu der medien Datei, welche den kompletten Medienbestand beinhaltet.
    static List<Medium> medienListe = new ArrayList<>();


    /**
     * Lädt die Medien aus der Datei {@code medien.txt} und erstellt eine Liste von {@link Medium}-Objekten.
     * <p>
     * Jede Zeile der Datei sollte die folgenden Informationen im CSV-Format enthalten:
     * Titel, Autor, Standplatz, Medientyp.
     * </p>
     *
     * @return Eine Liste von {@link Medium}-Objekten, die die geladenen Medien repräsentieren
     */
    public static List<Medium> ladeMedienAusDatei() {

        try (BufferedReader br = new BufferedReader(new FileReader(dateipfad) )) {
            String zeile;
            while ((zeile = br.readLine()) != null) {
                String[] teile = zeile.split(";");
                if (teile.length == 5) {
                    String titel = teile[0];
                    String autor = teile[1];
                    String standplatz = teile[2];
                    Medientyp typ = Medientyp.valueOf(teile[3]);
                    LocalDate ausleihe_datum = LocalDate.parse(teile[4]);

                    Medium medium = new Medium(titel, autor, standplatz, typ,
                            ausleihe_datum);
                    medienListe.add(medium);
                }
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Datei: " + e.getMessage());
        }
        return medienListe;
    }


    /**
     * Aktualisiert die Datei {@code medien.txt} mit dem aktuellen Stand der {@code medienListe}.
     * Die Datei wird komplett überschrieben, um die aktuellen Medieninformationen zu speichern.
     */
    public static void updateMedienInDatei() {

        // updated alle medien in der medienListe in die medien.txt datei bei Aufruf der Methode
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(dateipfad))){
            for(Medium medium : medienListe) {
                bw.write(medium.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Datei: " + e.getMessage());
        }
    }
  

    // Medium ausleihen: Hier kann im frontend eine Auswahl der vorhandenen Medien angezeigt werden, um Tippfehler zu vermeiden.

    public static void mediumAusleihen(String mediumTitel, LocalDate neuesAusleihDatum) {

        boolean mediumFound = false;

        // Wo liegt gesuchtes Buch in Tabelle
        for(Medium medium : ladeMedienAusDatei()) {
            if(medium.titel.equals(mediumTitel)) {
                medienListe.remove(medium); // Löscht "alte" Information
                medium.ausleihe_datum = neues_ausleihe_datum;
                medium.rueckgabe_datum = neues_ausleihe_datum.plusDays(30); // Hier wird drauf gerechnet anstatt im Medium Konstruktor

                medium.standplatz = "n.a";
                System.out.println("Rückgabedatum: " + neuesAusleihDatum.plusDays(30));
                medienListe.add(medium); // Fügt "neue" Information ein
                mediumFound = true;
                break;
            }
        }

        // Fehlermeldung, falls gesuchtes Medium nicht gefunden wird
        if(!mediumFound) {
            System.out.println("Medium nicht gefunden");
        }

        updateMedienInDatei();
    }


    public static void neuesMediumHinzufuegen(String titel, String autor, String standplatz, Medientyp typ) {
        // erstellt ein neues Medium mit den angegebenen Parametern
        Medium neuesMedium = new Medium(titel, autor, standplatz, typ, null, null);

        // fügt das neue Medium der Liste hinzu
        medienListe.add(neuesMedium);

        // aktualisiert die Datei mit der aktualisierten Liste
        updateMedienInDatei();

    }

    public static void mediumZurückgeben(String mediumZurück){

        boolean mediumFound = false;

        // Wo liegt gesuchtes Buch in Tabelle
        for(Medium medium : ladeMedienAusDatei()){
            if(medium.titel.equals(mediumZurück)){
                medienListe.remove(medium); // Löscht "alte" Information
                if (medium.ausleihe_datum.plusDays(30).isBefore(LocalDate.now())){
                    System.out.println("Das Medium ist " + medium.ausleihe_datum.plusDays(30).until(LocalDate.now(), ChronoUnit.DAYS) + " Tag(e) überfällig.");
                }
                medium.ausleihe_datum = LocalDate.parse("1970-01-01"); // Hier muss noch ein Weg gefunden werden, das Ausleihdatum wegzubekommen.
                medium.standplatz = "*Standplatz*";
                medienListe.add(medium); // Fügt "neue" Information ein
                mediumFound = true;
                break;
            }
        }

        // Fehlermeldung, falls gesuchtes Medium nicht gefunden wird
        if(!mediumFound) {
            System.out.println("Medium nicht gefunden");
        }

        speichereMedienInDatei();

    }

}
