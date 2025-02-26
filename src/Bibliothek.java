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
     * Titel, Autor, Medientyp.
     * Zusätzlich enthalten die Zeilen entweder einen Standplatz oder ein Ausleih- sowie Rückgabedatum.
     * </p>
     *
     * @return Eine Liste von {@link Medium}-Objekten, die die geladenen Medien repräsentieren
     */
    public static List<Medium> ladeMedien() {

        try (BufferedReader br = new BufferedReader(new FileReader(dateipfad) )) {
            String zeile;
            while ((zeile = br.readLine()) != null) {
                String[] teile = zeile.split(";");

                // Nicht ausgeliehen
                if (teile.length == 4) {
                    String titel = teile[0];
                    String autor = teile[1];
                    String standplatz = teile[2];
                    Medientyp typ = Medientyp.valueOf(teile[3]);

                    Medium medium = new Medium(titel, autor, standplatz, typ);
                    medienListe.add(medium);
                }

                // Ausgeliehen
                else if (teile.length == 5) {
                    String titel = teile[0];
                    String autor = teile[1];
                    Medientyp typ = Medientyp.valueOf(teile[2]);
                    LocalDate ausleihe_datum = LocalDate.parse(teile[3]);
                    LocalDate rueckgabe_datum = LocalDate.parse(teile[4]);

                    Medium medium = new Medium(titel, autor, typ, ausleihe_datum, rueckgabe_datum);
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
    public static void updateMedien() {

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

    /**
     * Weist einem Medium ein Ausleih- sowie Rückgabedatum zu und entfernt den Standplatz.
     * Speichert die Änderungen am Ende in der {@code medien.txt} Datei.
     */
    public static void mediumAusleihen(String mediumTitel, LocalDate neuesAusleihDatum) {

        boolean mediumFound = false;

        // Wo liegt gesuchtes Buch in Tabelle
        for(Medium medium : ladeMedien()) {
            if(medium.titel.equals(mediumTitel)) {
                medienListe.remove(medium); // Löscht "alte" Information
                medium.ausleihe_datum = neuesAusleihDatum;
                medium.rueckgabe_datum = neuesAusleihDatum.plusDays(30); // Hier wird drauf gerechnet anstatt im Medium Konstruktor
                medium.standplatz = null;
                System.out.println("Rückgabedatum: " + medium.rueckgabe_datum);
                medienListe.add(medium); // Fügt "neue" Information ein
                mediumFound = true;
                break;
            }
        }

        // Fehlermeldung, falls gesuchtes Medium nicht gefunden wird
        if(!mediumFound) {
            System.out.println("Medium nicht gefunden");
        }

        updateMedien();
    }


    public static void neuesMediumHinzufuegen(String titel, String autor, String standplatz, Medientyp typ) {
        // Lädt den aktuellen Stand der medien.txt Datei
        ladeMedien();

        // erstellt ein neues Medium mit den angegebenen Parametern
        Medium neuesMedium = new Medium(titel, autor, standplatz, typ);

        // fügt das neue Medium der Liste hinzu
        medienListe.add(neuesMedium);
        // aktualisiert die Datei mit der aktualisierten Liste
        updateMedien();

    }

    public static void mediumZurückgeben(String mediumZurück, String neuerStandplatz){

        boolean mediumFound = false;

        // Wo liegt gesuchtes Buch in Tabelle
        for(Medium medium : ladeMedien()){
            if(medium.titel.equals(mediumZurück)){
                medienListe.remove(medium); // Löscht "alte" Information
                if (medium.rueckgabe_datum.isBefore(LocalDate.now())){
                    System.out.println("Das Medium ist " +
                                        medium.rueckgabe_datum.until(LocalDate.now(), ChronoUnit.DAYS) +
                                        " Tag(e) überfällig.");
                }
                medium.ausleihe_datum = null;
                medium.rueckgabe_datum = null;
                medium.standplatz = neuerStandplatz;
                medienListe.add(medium); // Fügt "neue" Information ein
                mediumFound = true;
                break;
            }
        }

        // Fehlermeldung, falls gesuchtes Medium nicht gefunden wird
        if(!mediumFound) {
            System.out.println("Medium nicht gefunden");
        }

        updateMedien();

    }

}
