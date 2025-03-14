package backend;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Die Klasse {@code Bibliothek} verwaltet den Medienbestand und ermöglicht das Laden von Medien
 * aus und in eine Datei. Sie liest eine Datei, die Medieninformationen im CSV-Format enthält, und erstellt
 * eine Liste von {@link Medium}-Objekten.
 * <p>
 * Die Medieninformationen beinhalten den Titel, Autor, Standplatz und den Medientyp.
 * </p>
 */
public class Bibliothek {

    // Pfad zu der Mediendatei, welche den kompletten Medienbestand beinhaltet
    private final static String dateipfad = "src/medien.txt";

    /**
     * <p>Liste von {@link Medium}-Objekten, in welcher die Attribute dieser Objekte durch Methoden in der
     * {@link Bibliothek}-Klasse verändert werden können.</p>
     * <p>Die Objektinformationen in der {@code medien.txt} Datei werden bei Start des Programms mit
     * {@link #ladeMedienAusDatei()} in die {@code medienListe} geladen und beim Schließen des Programms mit
     * {@link #schreibeMedienInDatei()} werden alle Objektattribute in selbige Datei überschrieben.</p>
     */
    private static List<Medium> medienListe = new ArrayList<>();

    /**
     * <p>Getter Methode um vom Frontend auf die Medien Liste zuzugreifen.</p>
     * @return {@link #medienListe}
     */
    public static List<Medium> getMedienListe() {
        return medienListe;
    }

    private static int ausleihdauer = 30;   // Dauer einer Ausleihperiode


    /**
     * <p>Lädt die Inhalte pro Zeile aus der Datei {@code medien.txt} und erstellt {@link Medium}-Objekte, welche zur
     * {@link #medienListe} hinzugefügt werden.
     * Eine Zeile enthält alle Attribute des Objektes getrennt mit einem Semikolon.</p>
     * <p>Diese Methode wird nur beim Start des Programms ausgeführt.</p>
     */
    public static void ladeMedienAusDatei() {

        // Der BufferedReader liest die medien.txt
        try (BufferedReader br = new BufferedReader(new FileReader(dateipfad) )) {
            String zeile;

            // Für jede Zeile wird ein Medienobjekt erstellt
            while ((zeile = br.readLine()) != null) {
                String[] teile = zeile.split(";");

                // Überladener Medium-Konstruktor kann 5 oder 6 Parameter entgegennehmen
                if (teile.length == 5) {		//Bedingung für nicht ausgeliehenes Medium
                    String titel = teile[0];
                    String autor = teile[1];
                    String standplatz = teile[2];
                    Medientyp typ = Medientyp.valueOf(teile[3]);
                    Status status = Status.valueOf(teile[4]);

                    Medium medium = new Medium(titel, autor, standplatz, typ, status); // Objekt wird erstellt
                    medienListe.add(medium); // Objekt wird der ArrayList hinzugefügt
                }

                else if (teile.length == 6) {	//Bedingung für ausgeliehenes Medium
                    String titel = teile[0];
                    String autor = teile[1];
                    Medientyp typ = Medientyp.valueOf(teile[2]);
                    LocalDate ausleihe_datum = LocalDate.parse(teile[3]);
                    LocalDate rueckgabe_datum = LocalDate.parse(teile[4]);
                    Status status = Status.valueOf(teile[5]);

                    Medium medium = new Medium(titel, autor, typ, ausleihe_datum, rueckgabe_datum, status); // Objekt wird erstellt
                    medienListe.add(medium); // Objekt wird der ArrayList hinzugefügt
                }

            }

            // Fängt einen möglichen Fehler (verpflichtend bei Nutzung von BufferedReader)
        }catch (IOException e) {
            System.out.println("Fehler beim Laden der Datei: " + e.getMessage());
        }
    }

    /**
     * <p>Überschreibt die {@code medien.txt} mit dem aktuellen Stand der {@link #medienListe}.</p>
     * <p>Diese Methode wird nur beim Schließen des Programms ausgeführt.</p>
     */
    public static void schreibeMedienInDatei() {

        // Beendet die Ausführung der Methode, falls die medienListe leer ist, um eine Leere Überschreibung zu vermeiden
        if (medienListe.isEmpty()) {
            System.out.println("Keine Medien zu speichern");
            return;
        }

        // Sortiert die Liste alphabetisch nach Titeln
        medienListe.sort(Comparator.comparing(medium -> medium.titel));

        // Der BufferedReader schreibt die medien.txt komplett neu
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(dateipfad))){
            for(Medium medium : medienListe) {
                // Schreibt alle Attribute eines Medium-Objektes im CSV-Format in eine Zeile
                bw.write(medium.toString());
                bw.newLine();
            }
            // Fängt einen möglichen Fehler (verpflichtend bei Nutzung von BufferedWriter)
            } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Datei: " + e.getMessage());
        }
    }

    /**
     * <p>Leiht ein vorhandenes Medium aus, indem es einem Medium ein Ausleih- sowie Rückgabedatum zuweist und den
     * Standplatz entfernt.</p>
     * @param mediumTitel Titel des Mediums, das ausgeliehen werden soll
     * @param ausleihDatum Datum der Ausleihe
     * @return Meldung über Rückgabedatum oder fehlgeschlagene Ausleihe
     */
    public static String mediumAusleihen(String mediumTitel, LocalDate ausleihDatum) {

        for (int i = 0; i < medienListe.size(); i++) {
            Medium medium = medienListe.get(i);

            // Überspringt nicht zutreffende Medien, um unnötige Iterationen zu vermeiden
            if(!medium.titel.equals(mediumTitel)){
                continue;
            }

            if (!medium.status.equals(Status.VORHANDEN)) {
                return "Medium ist bereits ausgeliehen!";
            }

            // Erstellt eine neue "ausgeliehene" Version des Mediums
            Medium ausgeliehenesMedium = new Medium(
                    medium.titel,
                    medium.autor,
                    medium.medientyp,
                    ausleihDatum,
                    ausleihDatum.plusDays(ausleihdauer),
                    Status.AUSGELIEHEN
            );

            // Ersetze das Medium in der Liste mit den neuen Attributen
            medienListe.set(i, ausgeliehenesMedium);

            return "Rückgabedatum: " + ausgeliehenesMedium.rueckgabeDatum;
        }

        // Fehlermeldung, falls gesuchtes Medium nicht gefunden wird
        return "Medium nicht gefunden!";
    }


    /**
     * <p>Fügt ein ausgeliehenes Medium wieder dem Bestand hinzu, indem es einem Medium ein Ausleih- und Rückgabedatum
     * entfernt sowie einen neuen Standplatz zuweist.</p>
     * <p>Ist das zurückgegebene Medium überfällig, wird eine Meldung ausgegeben.</p>
     * <p>Es findet eine Prüfung statt, ob ein Medium nach der Rückgabe ausgemustert werden soll.</p>
     * @param mediumZurückTitel Medium, welches zurückgegeben werden soll
     * @param neuerStandplatz Standplatz, an welchem das Medium platziert werden soll
     */
    public static String mediumZurückgeben(String mediumZurückTitel, String neuerStandplatz){
        String message;

        for(int i = 0; i < medienListe.size(); i++){
            Medium medium = medienListe.get(i);

            // Überspringt nicht zutreffende Medien, um unnötige Iterationen zu vermeiden
            if(!medium.titel.equals(mediumZurückTitel)){
                continue;
            }

            // Überprüfung auf Überfälligkeit
            if (medium.rueckgabeDatum.isBefore(LocalDate.now())){
                message = "Medium zu spät zurückgegeben";
            } else{
                message = "Medium erfolgreich zurückgegeben";
            }

            // Ist das Medium zum Ausmustern vorgemerkt?
            if(medium.status == Status.AUSGELIEHEN_VORGEMERKT) {
                // Entfernt das Medium aus der Liste
                medienListe.remove(i);

                message += " und ausgemustert";
            } else {
                // Erstellt eine neue "ausgeliehene" Version des Mediums
                Medium zurueckgegebenesMedium = new Medium(
                        medium.titel,
                        medium.autor,
                        neuerStandplatz,
                        medium.medientyp,
                        Status.VORHANDEN
                );

                // Ersetze das Medium in der Liste
                medienListe.set(i, zurueckgegebenesMedium);
            }
            return message;
        }
        // Fehlermeldung, falls gesuchtes Medium nicht gefunden wird
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

        // Überprüft, ob Medium bereits vorhanden ist
        for (Medium medium : medienListe) {
            if (medium.titel.equals(titelNeu)) {
                return "Medium existiert schon im Bestand!";
            }
        }

        // Überprüft den Namen des Standplatzes
        if(standplatzUngültig(standplatzNeu)) {
            return "Der Standplatz ist nicht zulässig!"; // Bricht ab bei ungültigem Format oder belegtem Standort
        }

        // Erstellt ein neues Medium mit den angegebenen Parametern
        Medium neuesMedium = new Medium(titelNeu, autorNeu, standplatzNeu, typNeu, Status.VORHANDEN);

        // fügt das neue Medium der Liste hinzu
        medienListe.add(neuesMedium);

        // aktualisiert die Datei mit der aktualisierten Liste
        //schreibeMedienInDatei();

        return "Neues Medium hinzugefügt!";
    }

    /**
     * <p>Entfernt ein verfügbares Medium aus der {@link #medienListe}.</p>
     * @param titel_zum_ausmustern Titel des Mediums, welches ausgemustert werden soll
     */
    public static String vorhandenesMediumAusmustern(String titel_zum_ausmustern) {

        // Wo liegt gesuchtes Buch in Tabelle
        for(Medium medium : medienListe){

            if (!medium.titel.equals(titel_zum_ausmustern)){
                continue;
            }

            switch (medium.status){

                case VORHANDEN:
                    medienListe.remove(medium);
                    //schreibeMedienInDatei();

                    System.out.println("Das Medium mit dem Titel: " + medium.titel + " wurde erfolgreich ausgemustert.");
                    return "Das Medium mit dem Titel: " + medium.titel + "wurde erfolgreich ausgemustert.";

                case AUSGELIEHEN:
                    medium.status = Status.AUSGELIEHEN_VORGEMERKT;
                    //schreibeMedienInDatei();

                    System.out.println("Medium ist momentan ausgeliehen. Es wird ausgemustert, sobald es zurückgegeben wurde.");
                    return "Medium ist momentan ausgeliehen. Es wird ausgemustert, sobald es zurückgegeben wurde.";

                case AUSGELIEHEN_VORGEMERKT:
                    return "Das Medium ist bereits zum Ausmustern vorgemerkt.";

            }
        }

        System.out.println("Medium wurde nicht gefunden!");
        return "Medium wurde nicht gefunden!";
    }
              

    /**
     * <p>{@code verfügbareMedien()} filtert die verfügbaren Medien.</p>
     * @param typ Medientyp nach welchem gefiltert werden soll
     * @return Alle Medien, die im Bestand sind, alphabetisch sortiert als Liste mit Medium Objekten
     */
    public static List<Medium> verfügbareMedien(Medientyp typ){
        List<Medium> sortedList = new ArrayList<>(); // temporäre Liste für alle Medien, die zum Filter passen

        for (Medium medium : medienListe){

            if(medium.status != Status.VORHANDEN){
                continue;
            }

            // Je nach Parameter werden entweder alle oder nur ein bestimmter Medientyp übernommen
            if(typ == null || typ == medium.medientyp) {
                sortedList.add(medium);
            }
        }

        // Nach Alphabet sortieren
        sortedList.sort(Comparator.comparing(medium -> medium.titel));
        return sortedList;
    }


    /**
     * <p>{@code verfügbareMedien()} filtert die ausgeliehenen Medien.</p>
     * @param typ Medientyp nach welchem gefiltert werden soll
     * @return Alle ausgeliehenen Medien alphabetisch sortiert als Liste mit Medium Objekten
     */
    public static List<Medium> ausgelieheneMedien(Medientyp typ){
        List<Medium> sortedList = new ArrayList<>(); // temporäre Liste für alle Medien, die zum Filter passen

        for (Medium medium : medienListe){

            if(medium.status == Status.VORHANDEN){
                continue;
            }

            // Je nach Parameter werden entweder alle oder nur ein bestimmter Medientyp übernommen
            if(typ == null || typ == medium.medientyp) {
                sortedList.add(medium);
            }
        }

        // Nach Alphabet sortieren
        sortedList.sort(Comparator.comparing(medium -> medium.titel));
        return sortedList;
    }


    /**
     * <p>{@code überfälligeMedien()} überprüft das Rückgabedatum aller ausgeliehenen Medien und filtert nach überfälligen Medien.</p>
     * @return List aus allen überfälligen Medien Objekten
     */
    public static List<Medium> überfälligeMedien(){
        List<Medium> sortedList = new ArrayList<>(); // temporäre Liste für alle Medien, die zum Filter passen

        for (Medium medium : medienListe){
            // Medien, die ausgeliehen sind und deren Rückgabedatum vor dem heutigen sind
            if(medium.status != Status.VORHANDEN && medium.rueckgabeDatum.isBefore(LocalDate.now())){
                sortedList.add(medium);
            }
        }

        // Nach Alphabet sortieren
        sortedList.sort(Comparator.comparing(medium -> medium.rueckgabeDatum));
        return sortedList;
    }


    public static String istMediumÜberfällig(Medium medium) {
        System.out.println(überfälligeMedien());
        for (Medium m : überfälligeMedien()) {
            if (m.equals(medium)) { // Objekt ebene vergleichen
                return medium.rueckgabeDatum.until(LocalDate.now(), ChronoUnit.DAYS) +
                        " Tag(e) überfällig";
            }
        }

        return "Medium ist nicht überfällig";
    }

    /**
     * <p>{@code standplatzÄndern} ändert den Standplatz eines Mediums im Bestand.</p>
     * @param zuÄnderndesMedium Medium Titel, von welchem der Standplatz geändert werden soll
     * @param neuerStandplatz Neuer Standplatz, an welchem das Medium platziert werden soll
     */
    public static String standplatzÄndern(String zuÄnderndesMedium, String neuerStandplatz){

        // Überprüft den Namen des Standplatzes
        if(standplatzUngültig(neuerStandplatz)) {
            return "Der angegebene Standplatz ist schon belegt oder ungültig!"; // Bricht ab bei ungültigem Format oder belegtem Standort
        }

        // Wo liegt gesuchtes Medium in der medienListe?
        for (Medium medium : medienListe){

            if(medium.titel.equals(zuÄnderndesMedium)){
                medium.standplatz = neuerStandplatz; // Ändert den Standplatz
                return "Standplatz erfolgreich geändert!";
            }
        }

        System.out.println("Medium wurde nicht gefunden!"); // Wird nur ausgegeben, wenn die for-Schleife ohne Ergebnis durchläuft
        return "Medium wurde nicht gefunden!";
    }
  

    /**
     * <p>{@code standplatzValide} überprüft einen String, ob dieser ein neuer Standort sein kann.</p>
     * <p>Geprüft wird Folgendes:</p>
     * <li>Ist das Format korrekt? (min. 1 Kleinbuchstabe oder Zahl + Bindestrich + min. 1 Kleinbuchstabe oder Zahl)</li>
     * <li>Ist dieser Standplatz bereits belegt?</li><br>
     * @param standplatz Standplatz welcher überprüft werden soll
     * @return Boolean Wert, ob der Name des Standplatzes valide ist
     */
    public static boolean standplatzUngültig(String standplatz) {

        // Prüfung auf korrektes Format
        if(!standplatz.matches("^[a-z0-9]+-[a-z0-9]+$")){
            System.out.println("Der Name des Standplatzes ist nicht zulässig!");
            return true;
        }

        // Prüfung, ob Standplatz belegt ist
        for(Medium medium : medienListe){
            if(medium.standplatz != null && medium.standplatz.equals(standplatz)){
                System.out.println("Der gewählte Standplatz ist schon belegt!");
                return true;
            }
        }

        // Wenn das Format korrekt ist und der Standplatz nicht belegt ist, wird diese Stelle im Code erreicht
        return false;
    }

}