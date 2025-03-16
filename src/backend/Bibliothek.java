package backend;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <p>Verwaltet den Medienbestand durch folgende Funktionalitäten:</p>
 * <li>Den aktuellen Medienbestand aus {@code medien.txt} laden/speichern</li>
 * <li>Attribute von Medien ändern</li>
 * <li>Medien nach bestimmten Eigenschaften filtern</li>
 */
public class Bibliothek {

    // Pfad zu der Mediendatei, welche den kompletten Medienbestand beinhaltet
    private final static String dateipfad = "src/medien.txt";

    /**
     * <p>Liste von {@link Medium}-Objekten, deren Attribute durch Methoden in der {@link Bibliothek}-Klasse verändert
     * werden können.</p>
     * <p>Die Objektinformationen in der {@code medien.txt} Datei werden bei Start des Programms mit
     * {@link #ladeMedienAusDatei()} in diese Liste geladen und beim Schließen des Programms mit
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
     * @param mediumZurueckTitel Medium, welches zurückgegeben werden soll
     * @param neuerStandplatz Standplatz, an welchem das Medium platziert werden soll
     * @return Meldung über (nicht) erfolgte Rückgabe
     */
    public static String mediumZurueckgeben(String mediumZurueckTitel, String neuerStandplatz){
        String message;

        for(int i = 0; i < medienListe.size(); i++){
            Medium medium = medienListe.get(i);

            // Überspringt nicht zutreffende Medien, um unnötige Iterationen zu vermeiden
            if(!medium.titel.equals(mediumZurueckTitel)){
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
     * <p>Erstellt ein neues Medium und fügt es der {@link #medienListe} hinzu.</p>
     * @param titelNeu Titel des neuen Mediums
     * @param autorNeu Autor des neuen Mediums
     * @param standplatzNeu Standplatz des neuen Mediums
     * @param typNeu Medientyp des neuen Mediums
     * @return Meldung über (nicht) erfolgte Hinzufügung
     */
    public static String neuesMediumHinzufuegen(String titelNeu, String autorNeu, String standplatzNeu, Medientyp typNeu) {

        // Überprüft, ob Medium bereits vorhanden ist
        for (Medium medium : medienListe) {
            if (medium.titel.equals(titelNeu)) {
                return "Medium existiert schon im Bestand!";
            }
        }

        // Überprüft den Namen des Standplatzes
        if(standplatzUngueltig(standplatzNeu)) {
            return "Der Standplatz ist nicht zulässig!";
        }

        // Erstellt ein neues Medium mit den angegebenen Parametern
        Medium neuesMedium = new Medium(titelNeu, autorNeu, standplatzNeu, typNeu, Status.VORHANDEN);

        // Fügt das neue Medium der medienListe hinzu
        medienListe.add(neuesMedium);

        return "Neues Medium hinzugefügt!";
    }


    /**
     * <p>Entfernt ein verfügbares Medium aus der {@link #medienListe}.</p>
     * <p>Sollte ein Medium noch ausgeliehen sein, wird es zur Ausmusterung vorgemerkt.</p>
     * @param titelZumAusmustern Titel des Mediums, welches ausgemustert werden soll
     * @return Meldung mit Informationen über die (nicht) erfolgte Ausmusterung
     */
    public static String vorhandenesMediumAusmustern(String titelZumAusmustern) {

        // Wo liegt gesuchtes Buch in Tabelle
        for(Medium medium : medienListe){

            // Überspringt nicht zutreffende Medien, um unnötige Iterationen zu vermeiden
            if (!medium.titel.equals(titelZumAusmustern)){
                continue;
            }

            // Returned eine Meldung je nach Status
            switch (medium.status){

                case VORHANDEN:
                    medienListe.remove(medium);
                    return "Das Medium mit dem Titel: " + medium.titel + " wurde erfolgreich ausgemustert.";

                case AUSGELIEHEN:
                    medium.status = Status.AUSGELIEHEN_VORGEMERKT;
                    return "Medium ist momentan ausgeliehen. Es wird ausgemustert, sobald es zurückgegeben wurde.";

                case AUSGELIEHEN_VORGEMERKT:
                    return "Das Medium ist bereits zum Ausmustern vorgemerkt.";

                case null, default:
                    return "Ein Fehler ist aufgetreten. Der Status des Mediums ist unbekannt.";
            }
        }
        return "Medium wurde nicht gefunden!";
    }
              

    /**
     * <p>Filtert die verfügbaren Medien.</p>
     * @param filterTyp {@link Medientyp} nach welchem gefiltert werden soll
     * @return Liste mit vorhandenen Medium-Objekten alphabetisch sortiert nach festgelegtem {@link Medientyp}
     */
    public static List<Medium> verfuegbareMedien(Medientyp filterTyp){

        // Temporäre Liste für alle Medien, die zum Filter passen
        List<Medium> sortedList = new ArrayList<>();

        for (Medium medium : medienListe){

            // Überspringt nicht zutreffende Medien, um unnötige Iterationen zu vermeiden
            if(medium.status != Status.VORHANDEN){
                continue;
            }

            // Je nach Parameter werden entweder alle oder nur ein bestimmter Medientyp übernommen
            if(filterTyp == null || filterTyp == medium.medientyp) {
                sortedList.add(medium);
            }
        }

        // Nach Alphabet sortieren
        sortedList.sort(Comparator.comparing(medium -> medium.titel));
        return sortedList;
    }


    /**
     * <p>Filtert die ausgeliehenen Medien.</p>
     * @param filterTyp {@link Medientyp} nach welchem gefiltert werden soll
     * @return Liste mit ausgeliehenen Medium-Objekten alphabetisch sortiert nach festgelegtem {@link Medientyp}
     */
    public static List<Medium> ausgelieheneMedien(Medientyp filterTyp){

        // Temporäre Liste für alle Medien, die zum Filter passen
        List<Medium> sortedList = new ArrayList<>();

        for (Medium medium : medienListe){

            // Überspringt nicht zutreffende Medien, um unnötige Iterationen zu vermeiden
            if(medium.status == Status.VORHANDEN){
                continue;
            }

            // Je nach Parameter werden entweder alle oder nur ein bestimmter Medientyp übernommen
            if(filterTyp == null || filterTyp == medium.medientyp) {
                sortedList.add(medium);
            }
        }

        // Nach Alphabet sortieren
        sortedList.sort(Comparator.comparing(medium -> medium.titel));
        return sortedList;
    }


    /**
     * <p>Filtert nach überfälligen Medien.</p>
     * @return Liste mit allen überfälligen Medien Objekten alphabetisch sortiert
     */
    public static List<Medium> ueberfaelligeMedien(){

        // Temporäre Liste für alle Medien, die zum Filter passen
        List<Medium> sortedList = new ArrayList<>();

        for (Medium medium : medienListe){

            // Überspringt nicht zutreffende Medien, um unnötige Iterationen zu vermeiden
            if(medium.status == Status.VORHANDEN){
                continue;
            }

            // Medien, deren Rückgabedatum vor dem heutigen sind
            if(medium.rueckgabeDatum.isBefore(LocalDate.now())){
                sortedList.add(medium);
            }
        }

        // Nach Alphabet sortieren
        sortedList.sort(Comparator.comparing(medium -> medium.rueckgabeDatum));
        return sortedList;
    }


    /**
     * <p>Überprüft, ob ein {@link Medium} überfällig ist.</p>
     * @param medium {@link Medium} zum Überprüfen
     * @return Meldung, ob {@link Medium} überfällig ist. Falls überfällig, wird die Anzahl der überfälligen Tage
     * angegeben
     */
    public static String istMediumUeberfaellig(Medium medium) {

        // Vergleicht das Medium mit allen überfälligen Medien
        for (Medium m : ueberfaelligeMedien()) {

            if (medium.equals(m)) {
                return medium.rueckgabeDatum.until(LocalDate.now(), ChronoUnit.DAYS) + " Tag(e) überfällig";
            }
        }

        return "Medium ist nicht überfällig";
    }


    /**
     * <p>Ändert den Standplatz eines vorhandenen {@link Medium}-Objektes.</p>
     * @param zuAenderndesMedium {@link Medium}, von welchem der Standplatz geändert werden soll
     * @param neuerStandplatz Neuer Standplatz, an welchem das {@link Medium} platziert werden soll
     * @return Meldung über (nicht) erfolgte Standplatzänderung
     */
    public static String standplatzAendern(String zuAenderndesMedium, String neuerStandplatz){

        // Überprüft den Namen des Standplatzes
        if(standplatzUngueltig(neuerStandplatz)) {
            return "Der angegebene Standplatz ist schon belegt oder ungültig!";
        }

        // Wo liegt gesuchtes Medium in der medienListe?
        for (Medium medium : medienListe){

            if(medium.titel.equals(zuAenderndesMedium)){
                medium.standplatz = neuerStandplatz; // Ändert den Standplatz
                return "Standplatz erfolgreich geändert!";
            }
        }

        // Wird nur ausgegeben, wenn die for-Schleife ohne Ergebnis durchläuft
        return "Medium wurde nicht gefunden!";
    }
  

    /**
     * <p>Überprüft einen String, ob dieser ein neuer Standplatz sein kann.</p>
     * <p>Geprüft wird Folgendes:</p>
     * <li>Ist das Format korrekt? (min. 1 Kleinbuchstabe oder Zahl + Bindestrich + min. 1 Kleinbuchstabe oder Zahl)</li>
     * <li>Ist dieser Standplatz bereits belegt?</li><br>
     * @param standplatz Standplatz welcher überprüft werden soll
     * @return Boolean Wert, ob der Name des Standplatzes valide ist
     */
    public static boolean standplatzUngueltig(String standplatz) {

        // Prüfung auf korrektes Format
        if(!standplatz.matches("^[a-z0-9]+-[a-z0-9]+$")){
            return true;
        }

        // Prüfung, ob Standplatz belegt ist
        for(Medium medium : medienListe){
            if(medium.standplatz != null && medium.standplatz.equals(standplatz)){
                return true;
            }
        }

        // Wenn das Format korrekt ist und der Standplatz nicht belegt ist, wird diese Stelle im Code erreicht
        return false;
    }

}