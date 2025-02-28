package backend;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
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
    private final static String dateipfad = "src/medien.txt"; // Pfad zu der medien datein, welche den kompletten medienbestand beinhaltet
    public static List<Medium> medienListe = new ArrayList<>();


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
        // Diese Methode darf nicht einfach so gecalled werden! , sonst verhängt sich das Programm in einer doppel update schleife, was zu riesigen Daten in der medien.txt datei führt
        // Deswegen nur in for schleife um durch die medien.txt datei zu loopen :D

        // Ich würde die Methode beim Start aufrufen, den Rückgabewert komplett entfernen und nochmal am Ende von updateMedien aufrufen

        // MedienListe reseten, damit nichts doppelt eingelesen wird
        medienListe.clear(); // Die Liste wird komplett gelöscht, da sie ab hier komplett neu angelegt wird

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

        // Medienliste aus datei gelesen
        return medienListe;
    }


    /**
     * Aktualisiert die Datei {@code medien.txt} mit dem aktuellen Stand der {@code medienListe}.
     * Die Datei wird komplett überschrieben, um die aktuellen Medieninformationen zu speichern.
     */
    public static void updateMedien() {

        // Sortiert die Liste alphabetisch nach Titeln
        medienListe.sort(Comparator.comparing(medium -> medium.titel));

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



        // HINWEIS! Hier ist noch ein Bug: da der constructor medien mit standort und ohne ausleihe datum init zulässt müssen wir hier
        // sichergehen, dass wir das erst tryen, sonst läuft man in ein error...

        // Wo liegt gesuchtes Buch in Tabelle
        for(Medium medium : ladeMedien()) {
            if(medium.titel.equals(mediumTitel)) {
                if (medium.standplatz == null) {
                    System.out.println("Medium ist immoment schon ausgeliehen!");
                }
                medienListe.remove(medium); // Löscht "alte" Information
                medium.ausleihe_datum = neuesAusleihDatum;
                medium.rueckgabe_datum = neuesAusleihDatum.plusDays(30);
                medium.standplatz = "";
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

        updateMedien();
    }


    public static void neuesMediumHinzufuegen(String titel, String autor, String standplatz, Medientyp typ) {
        // erstellt ein neues Medium mit den angegebenen Parametern
        Medium neuesMedium = new Medium(titel, autor, standplatz, typ);

        for (Medium medium : medienListe) {
            if (medium.titel.equals(neuesMedium.titel)) {
                System.out.println("Medium existiert schon im Bestand!");
                return; // bricht ab wenn Medium schon existiert
            }

        }

        // fügt das neue Medium der Liste hinzu
        medienListe.add(neuesMedium);

        // aktualisiert die Datei mit der aktualisierten Liste
        updateMedien();
        System.out.println("Neues Medium hinzugefügt!");
    }


    public static void vorhandenesMediumAusmustern(String titel_zum_ausmustern) {

        // Wo liegt gesuchtes Buch in Tabelle
        for(Medium medium : ladeMedien()){
            if(medium.titel.equals(titel_zum_ausmustern)) {
              
                if (medium.rueckgabe_datum.isBefore(LocalDate.now() )) {
                    System.out.println("Das Medium ist " +
                                        medium.rueckgabe_datum.until(LocalDate.now(), ChronoUnit.DAYS) +
                                        " Tag(e) überfällig.");
                } 
                  
               if (medium.ausleihe_datum != null) {
                    // Sofern ein medium ausgeliehen ist ( ausliehedatum ist nur wenn ausgeliehen ein LocalDate Objekt, ansonsten ein String "null" )
                    System.out.println("Medium ist immomemt ausgeliehen, es wir ausgemustert sobald es zurückgegeben wurde.");

                    // Logik für ausmustrern sobald zurückgegeben...

               } else {
                    // Sofern ein medium nicht ausgeliehen ist, kann es sofort ausgemustert werden
                    medienListe.remove(medium);
                    System.out.println("Das Medium mit dem Titel: " + medium.titel + " wurde erfolgreich ausgemustert.");
               }
                break;
            } else {
                System.out.println("Medium wurde nicht gefunden!");
            }
        }
      
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

    /**
     * Methode zum Filtern der verfügbaren Medien
     *
     * @param typ Medientyp nach welchem gefiltert werden soll
     * @return Alle Medien, die im Bestand sind, alphabetisch sortiert als String im html Format
     */
    public static String verfügbareMedien(Medientyp typ){

        List<Medium> sortedList = new ArrayList<>(); // temporäre Liste für alle Medien, die zum Filter passen
        StringBuilder sb = new StringBuilder();

        for (Medium medium : ladeMedien()){

            // Medien, bei denen der Standplatz nicht null ist, sind im Bestand
            if(medium.standplatz != null){
                // Je nach Parameter werden entweder alle oder nur ein bestimmter Medientyp übernommen
                if(typ == null || typ == medium.medientyp) {
                    sortedList.add(medium);
                }
            }

        }

        // Nach Alphabet sortieren
        sortedList.sort(Comparator.comparing(medium -> medium.titel));

        // String aus allen gefilterten Medien wird im html Format gebaut, damit Line Breaks im JLabel möglich sind
        sb.append("<html><body>");
        for (Medium medium : sortedList){
            sb.append("<p>Titel: ")
                    .append(medium.titel)
                    .append(", Standplatz: ")
                    .append(medium.standplatz)
                    .append("</p><br>");
        }
        sb.append("</body></html>");

        return sb.toString();
    }

    /**
     * Methode zum Filtern der ausgeliehenen Medien
     *
     * @param typ Medientyp nach welchem gefiltert werden soll
     * @return Alle ausgeliehenen Medien alphabetisch sortiert als String im html Format
     */
    public static String ausgelieheneMedien(Medientyp typ){

        List<Medium> sortedList = new ArrayList<>(); // temporäre Liste für alle Medien, die zum Filter passen
        StringBuilder sb = new StringBuilder();

        for (Medium medium : ladeMedien()){

            // Medien, bei denen der Standplatz null ist, sind ausgeliehen
            if(medium.standplatz == null){
                // Je nach Parameter werden entweder alle oder nur ein bestimmter Medientyp übernommen
                if(typ == null || typ == medium.medientyp) {
                    sortedList.add(medium);
                }
            }

        }

        // Nach Alphabet sortieren
        sortedList.sort(Comparator.comparing(medium -> medium.titel));

        // String aus allen gefilterten Medien wird im html Format gebaut, damit Line Breaks im JLabel möglich sind
        sb.append("<html><body>");
        for (Medium medium : sortedList){
            sb.append("<p>Titel: ")
                    .append(medium.titel)
                    .append(", Rückgabedatum: ")
                    .append(medium.rueckgabe_datum)
                    .append("</p><br>");
        }
        sb.append("</body></html>");

        return sb.toString();
    }

    public static String überfälligeMedien(){

        List<Medium> sortedList = new ArrayList<>(); // temporäre Liste für alle Medien, die zum Filter passen
        StringBuilder sb = new StringBuilder();

        for (Medium medium : ladeMedien()){

            // Medien, deren Rückgabedatum vor dem heutigen sind
            if(medium.rueckgabe_datum != null){
                if(medium.rueckgabe_datum.isBefore(LocalDate.now())){
                    sortedList.add(medium);
                }
            }

        }

        // Nach Alphabet sortieren
        sortedList.sort(Comparator.comparing(medium -> medium.rueckgabe_datum));

        // String aus allen gefilterten Medien wird im html Format gebaut, damit Line Breaks im JLabel möglich sind
        sb.append("<html><body>");
        for (Medium medium : sortedList){
            sb.append("<p>Titel: ")
                    .append(medium.titel)
                    .append(", Rückgabedatum: ")
                    .append(medium.rueckgabe_datum)
                    .append(", Tag(e) überfällig: ")
                    .append(medium.rueckgabe_datum.until(LocalDate.now(), ChronoUnit.DAYS))
                    .append("</p><br>");
        }
        sb.append("</body></html>");

        return sb.toString();
    }

    public static void standplatzÄndern(String zuÄnderndesMedium, String neuerStandplatz){

        ladeMedien();

        // Zulässiger Standplatz: min. 1 Kleinbuchstabe oder Zahl + Bindestrich + min. 1 Kleinbuchstabe oder Zahl
        if(!neuerStandplatz.matches("^[a-z0-9]+-[a-z0-9]+$")){
            System.out.println("Der Name des Standplatzes ist nicht zulässig!");
            return;
        }

        for(Medium medium : medienListe){
            if(medium.standplatz.equals(neuerStandplatz)){
                System.out.println("Der gewählte Standplatz ist schon belegt!");
                return;
            }
        }

        for (Medium medium : medienListe){
            if(medium.titel.equals(zuÄnderndesMedium)){
                medium.standplatz = neuerStandplatz;
                break;
            }
        }

        updateMedien();

    }

}
