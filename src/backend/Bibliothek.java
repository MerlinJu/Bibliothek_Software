package backend;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import frontend.popups.NeuesMediumPopup;

/**
 * Die Klasse {@code Bibliothek} verwaltet den Medienbestand und ermöglicht das Laden von Medien
 * aus einer Datei. Sie liest eine Datei, die Medieninformationen im CSV-Format enthält, und erstellt
 * eine Liste von {@link Medium}-Objekten.
 * <p>
 * Die Medieninformationen beinhalten den Titel, Autor, Standplatz und den Medientyp.
 * </p>
 */
public class Bibliothek {
    private final static String dateipfad = "src/medien.txt"; // Pfad zu der Mediendatei, welche den kompletten Medienbestand beinhaltet

    /**
     * Liste, welche immer den aktuellen Stand der {@code medien.txt} beinhaltet
     */
    private static List<Medium> medienListe = new ArrayList<>();

    /**
     * Liste mit Medien, die ausgemustert werden sollen, allerdings noch ausgeliehen sind
     */
    private static List<Medium> medienZumAusmustern = new ArrayList<>();

    // HINWEIS ausleihe Zeit, diese kann später dann evnetuell durch die EInstellungen abgeändert werden!
    private static int ausleiheZeit_tage = 30;


    /**
     * <p>Die Methode {@code ladeMedien()} lädt die Medien aus der Datei {@code medien.txt} und
     * erstellt eine Liste von {@link Medium}-Objekten.</p>
     * <p>Jede Zeile der Datei sollte die folgenden Informationen im CSV-Format enthalten:
     * Titel, Autor, Medientyp.
     * Zusätzlich enthalten die Zeilen entweder einen Standplatz oder ein Ausleih- sowie Rückgabedatum.</p>
     * <p>Diese Methode soll beim Start des Programms ausgeführt werden.</p>
     */
    public static void ladeMedienAusDatei() {

        // Die Liste wird komplett gelöscht, da sie ab hier komplett neu angelegt wird
        medienListe.clear();

        // Der BufferedReader liest die medien.txt
        try (BufferedReader br = new BufferedReader(new FileReader(dateipfad) )) {
            String zeile;

            // Für jede Zeile wird ein Medienobjekt erstellt
            while ((zeile = br.readLine()) != null) {
                String[] teile = zeile.split(";");

                // Da ein Medienobjekt 4 oder 5 Informationen haben kann, werden hier beide Fälle abgedeckt
                if (teile.length == 4) {		//Bedingung für nicht ausgeliehene Medien
                    String titel = teile[0];
                    String autor = teile[1];
                    String standplatz = teile[2];
                    Medientyp typ = Medientyp.valueOf(teile[3]);

                    Medium medium = new Medium(titel, autor, standplatz, typ); // Objekt wird erstellt
                    medienListe.add(medium); // Objekt wird der ArrayList hinzugefügt
                }

                else if (teile.length == 5) {	//Bedingung für ausgeliehene Medien
                    String titel = teile[0];
                    String autor = teile[1];
                    Medientyp typ = Medientyp.valueOf(teile[2]);
                    LocalDate ausleihe_datum = LocalDate.parse(teile[3]);
                    LocalDate rueckgabe_datum = LocalDate.parse(teile[4]);

                    Medium medium = new Medium(titel, autor, typ, ausleihe_datum, rueckgabe_datum); // Objekt wird erstellt
                    medienListe.add(medium); // Objekt wird der ArrayList hinzugefügt
                }

            }

            // Fängt einen möglichen Fehler (verpflichtend bei Nutzung von BufferedReader)
        }catch (IOException e) {
            System.out.println("Fehler beim Laden der Datei: " + e.getMessage());
        }
    }

    // getter methode um im frontend auf die Meiden Liste zuzugreifen
    public static List<Medium> getMedienListe() {
        return medienListe;
    }


    /**
     * <p>{@code updateMedien()} überschreibt die {@code medien.txt} mit dem aktuellen Stand der {@link #medienListe}.</p>
     *
     * <p>Diese Methode wird immer dann aufgerufen, wenn die {@link #medienListe} geändert wird.</p>
     */
    private static void updateMedienInDatei() {
        System.out.println(medienListe);

        // returnd falls die medienListe leer ist, um eine Leere Ueberschreibung zu vermeiden
        if (medienListe.isEmpty()) {
            System.out.println("Keine Medien zu speichern");
            return;
        }

        // Sortiert die Liste alphabetisch nach Titeln
        medienListe.sort(Comparator.comparing(medium -> medium.titel));

        // Der BufferedReader schreibt die medien.txt komplett neu
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(dateipfad))){
            for(Medium medium : medienListe) {
                bw.write(medium.toString());
                bw.newLine();
            }
            // Fängt einen möglichen Fehler (verpflichtend bei Nutzung von BufferedWriter)
            } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Datei: " + e.getMessage());
        }
    }

    /**
     * Weist einem Medium ein Ausleih- sowie Rückgabedatum zu und entfernt den Standplatz.
     * @param mediumTitel Titel des Mediums, das ausgeliehen werden soll
     * @param ausleihDatum Datum der Ausleihe
     */
    public static String mediumAusleihen(String mediumTitel, LocalDate ausleihDatum) {
        String message = "";
        boolean mediumFound = false;

        for (int i = 0; i < medienListe.size(); i++) {
            Medium medium = medienListe.get(i);

            if (medium.titel.equals(mediumTitel)) {
                if (medium.ausleihe_datum != null) {
                    System.out.println("Medium ist bereits ausgeliehen!");
                    return "Medium ist bereits ausgeliehen!";
                }

                // Erstelle eine neue "ausgeliehene" Version des Mediums
                Medium ausgeliehenesMedium = new Medium(
                        medium.titel,
                        medium.autor,
                        medium.medientyp,
                        ausleihDatum,
                        ausleihDatum.plusDays(ausleiheZeit_tage)
                );

                // Ersetze das Medium in der Liste
                medienListe.set(i, ausgeliehenesMedium);

                System.out.println("Rückgabedatum: " + ausgeliehenesMedium.rueckgabe_datum);
                message = "Rückgabedatum: " + ausgeliehenesMedium.rueckgabe_datum;

                mediumFound = true;
                break;
            }
        }

        // Fehlermeldung, falls gesuchtes Medium nicht gefunden wird
        if(!mediumFound) {
            System.out.println("Medium nicht gefunden");
            message = "Medium nicht gefunden!";
        }

        updateMedienInDatei();

        return message;
    }


    /**
     * <p>{@code mediumZurückgeben()} fügt ein ausgeliehenes Medium wieder dem Bestand hinzu, indem es Ausleih- und Rückgabedatum
     * entfernt sowie einen neuen Standplatz zuweist.</p>
     * <p>Ist das zurückgegebene Medium überfällig, wird eine Meldung ausgegeben.</p>
     * <p>Es findet eine Prüfung statt, ob ein Medium nach der Rückgabe ausgemustert werden soll.</p>
     * @param mediumZurückTitel Medium, welches zurückgegeben werden soll
     * @param neuerStandplatz Standplatz, an welchem das Medium platziert werden soll
     */
    public static String mediumZurückgeben(String mediumZurückTitel, String neuerStandplatz){
        String message = "";

        // hier fehlt noch etwas: wenn ein Medium ausgemustert werden soll nach der rückgabe. Dann muss natürlich kein Standort mehr
        // als Parameter mitgegeben werden

        // Überprüft den Namen des Standplatzes
        if(!standplatzValide(neuerStandplatz)) {
            return "Standplatz ist nicht valide!"; // Bricht ab bei ungültigem Format oder belegtem Standort
        }

        // Wo liegt gesuchtes Medium in der medienListe?
        for(Medium medium : medienListe){

            if(medium.titel.equals(mediumZurückTitel)){

                medienListe.remove(medium); // Löscht "alte" Information

                // Überprüfung auf Überfälligkeit
                if (medium.rueckgabe_datum.isBefore(LocalDate.now())){
                    System.out.println("Das Medium ist " +
                            medium.rueckgabe_datum.until(LocalDate.now(), ChronoUnit.DAYS) +
                            " Tag(e) überfällig.");
                }

                // Ist das Medium zum Ausmustern vorgemerkt?
                if(medienZumAusmustern.contains(medium)) {	// Mustert das Medium aus
                    medienListe.remove(medium);
                    medienZumAusmustern.remove(medium);
                    updateMedienInDatei();
                    return "Medium erfolgreich zurückgegeben und ausgemustert!";

                } else {	// Fügt das Medium wieder in den Bestand ein
                    medium.ausleihe_datum = null;
                    medium.rueckgabe_datum = null;
                    medium.standplatz = neuerStandplatz;
                    medienListe.add(medium); // Fügt "neue" Information ein
                    updateMedienInDatei();
                    return "Medium erfolgreich zurückgegeben";
                }
            }
        }
        // Fehlermeldung, falls gesuchtes Medium nicht gefunden wird
        System.out.println("Medium nicht gefunden");
        return "Medium nicht gefunden";
    }


    /**
     * {@code neuesMediumHinzufuegen()} erstellt ein neues Medium und fügt es der {@link #medienListe} hinzu.
     * @param titelNeu Titel des neuen Mediums
     * @param autorNeu Autor des neuen Mediums
     * @param standplatzNeu Standplatz des neuen Mediums
     * @param typNeu Medientyp des neuen Mediums
     */
    public static String neuesMediumHinzufuegen(String titelNeu, String autorNeu, String standplatzNeu, Medientyp typNeu) {
        String message;

        // Erstellt ein neues Medium mit den angegebenen Parametern
        Medium neuesMedium = new Medium(titelNeu, autorNeu, standplatzNeu, typNeu);

        for (Medium medium : medienListe) {
            if (medium.titel.equals(neuesMedium.titel)) {
                message = "Medium existiert schon im Bestand!";
                System.out.println("Medium existiert schon im Bestand!");
                return message; // bricht ab wenn Medium schon existiert
            }
        }

        // Überprüft den Namen des Standplatzes
        if(!standplatzValide(standplatzNeu)) {
            message = "Der Standplatz ist nicht zulässig!";
            return message; // Bricht ab bei ungültigem Format oder belegtem Standort
        }

        // fügt das neue Medium der Liste hinzu
        medienListe.add(neuesMedium);

        // aktualisiert die Datei mit der aktualisierten Liste
        updateMedienInDatei();

        message = "Neues Medium hinzugefügt!";
        System.out.println("Neues Medium hinzugefügt!");
        return message;
    }

    /**
     *
     * @param titel_zum_ausmustern Titel des Medium, welches ausgemustert werden soll
     */
    public static void vorhandenesMediumAusmustern(String titel_zum_ausmustern) {

        // Wo liegt gesuchtes Buch in Tabelle
        for(Medium medium : medienListe){
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
      
        updateMedienInDatei();
    }
              

    /**
     * <p>{@code verfügbareMedien()} filtert die verfügbaren Medien.</p>
     * @param typ Medientyp nach welchem gefiltert werden soll
     * @return Alle Medien, die im Bestand sind, alphabetisch sortiert als Liste mit Medium Objekten
     */
    public static List<Medium> verfügbareMedien(Medientyp typ){
        List<Medium> sortedList = new ArrayList<>(); // temporäre Liste für alle Medien, die zum Filter passen
        // StringBuilder sb = new StringBuilder();

        for (Medium medium : medienListe){

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
        return sortedList;

        // String aus allen gefilterten Medien wird im html Format gebaut, damit Line Breaks im JLabel möglich sind
        //sb.append("<html><body>");
        //for (Medium medium : sortedList){
          //  sb.append("<p>Titel: ")
            //        .append(medium.titel)
              //      .append(", Standplatz: ")
                //    .append(medium.standplatz)
                  //  .append("</p><br>");
        //}
        //sb.append("</body></html>");

        //return sb.toString();
    }


    /**
     * <p>{@code verfügbareMedien()} filtert die ausgeliehenen Medien.</p>
     * @param typ Medientyp nach welchem gefiltert werden soll
     * @return Alle ausgeliehenen Medien alphabetisch sortiert als Liste mit Medium Objekten
     */
    public static List<Medium> ausgelieheneMedien(Medientyp typ){
        List<Medium> sortedList = new ArrayList<>(); // temporäre Liste für alle Medien, die zum Filter passen

        for (Medium medium : medienListe){
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
        return sortedList;
    }

    public static List<Medium> überfälligeMedien(){
        List<Medium> sortedList = new ArrayList<>(); // temporäre Liste für alle Medien, die zum Filter passen

        for (Medium medium : medienListe){
            // Medien, deren Rückgabedatum vor dem heutigen sind
            if(medium.rueckgabe_datum != null){
                if(medium.rueckgabe_datum.isBefore(LocalDate.now())){
                    sortedList.add(medium);
                }
            }
        }

        // Nach Alphabet sortieren
        sortedList.sort(Comparator.comparing(medium -> medium.rueckgabe_datum));
        return sortedList;
    }

    /**
     * <p>{@code standplatzÄndern} ändert den Standplatz eines Mediums im Bestand.</p>
     * @param zuÄnderndesMedium Medium, von welchem der Standplatz geändert werden soll
     * @param neuerStandplatz Neuer Standplatz, an welchem das Medium platziert werden soll
     */
    public static void standplatzÄndern(String zuÄnderndesMedium, String neuerStandplatz){

        // Überprüft den Namen des Standplatzes
        if(!standplatzValide(neuerStandplatz)) {
            return; // Bricht ab bei ungültigem Format oder belegtem Standort
        }

        // Wo liegt gesuchtes Medium in der medienListe?
        for (Medium medium : medienListe){

            if(medium.standplatz != null && medium.titel.equals(zuÄnderndesMedium)){
                medium.standplatz = neuerStandplatz; // Ändert den Standplatz
                updateMedienInDatei();
                return;
            }
        }
        System.out.println("Medium wurde nicht gefunden!"); // Wird nur ausgegeben, wenn die for-Schleife ohne Ergebnis durchläuft
    }
  

    /**
     * <p>{@code standplatzValide} überprüft einen String, ob dieser ein neuer Standort sein kann.</p>
     * <p>Geprüft wird Folgendes:</p>
     * <li>Ist das Format korrekt? (min. 1 Kleinbuchstabe oder Zahl + Bindestrich + min. 1 Kleinbuchstabe oder Zahl)</li>
     * <li>Ist dieser Standplatz bereits belegt?</li><br>
     * @param standplatz Standplatz welcher überprüft werden soll
     * @return Boolean Wert, ob der Name des Standplatzes valide ist
     */
    private static boolean standplatzValide(String standplatz) {

        // Prüfung auf korrektes Format
        if(!standplatz.matches("^[a-z0-9]+-[a-z0-9]+$")){
            System.out.println("Der Name des Standplatzes ist nicht zulässig!");
            return false;
        }

        // Prüfung, ob Standplatz belegt ist
        for(Medium medium : medienListe){
            if(medium.standplatz != null && medium.standplatz.equals(standplatz)){
                System.out.println("Der gewählte Standplatz ist schon belegt!");
                return false;
            }
        }

        // Wenn das Format korrekt ist und der Standplatz nicht belegt ist, wird diese Stelle im Code erreicht
        return true;
    }

}