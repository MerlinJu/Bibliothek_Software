import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Bibliothek Klasse, um den Medien Bestand zu verwalten und einzulesen aus der medien.txt file
public class Bibliothek {
	
    private final static String dateipfad = "src/medien.txt"; // Pfad zu der medien datein, welche den kompletten medienbestand beinhaltet
    static List<Medium> medienListe = new ArrayList<>();
    
    // Medien aus Datei laden
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
                    LocalDate rueckgabe_datum = LocalDate.parse(teile[4]).plusDays(30);

                    Medium medium = new Medium(titel, autor, standplatz, typ,
                    		ausleihe_datum, rueckgabe_datum);
                    medienListe.add(medium);
                }
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Datei: " + e.getMessage());
        }
        return medienListe;
    }
    
    public static void speichereMedienInDatei() {
    	
    	try(BufferedWriter bw = new BufferedWriter(new FileWriter(dateipfad))){
    		for(Medium medium : medienListe) {
    			bw.write(medium.toString());
    			bw.newLine();
    		}
    	} catch (IOException e) {
            System.out.println("Fehler beim Speichern der Datei: " + e.getMessage());
        }
    }
    
    public static void mediumAusleihen() {
    	// Diese Werte sind erstmal zum Testen, sollen aber später input sein
    	String titel_zum_ausleihen = "Mensch";
    	LocalDate neues_ausleihe_datum = LocalDate.parse("2025-04-13");
    	
    	boolean mediumFound = false;
    	
    	// Wo liegt gesuchtes Buch in Tabelle
    	for(Medium medium : ladeMedienAusDatei()) {
    		if(medium.titel.equals(titel_zum_ausleihen)) {
    			medienListe.remove(medium); // Löscht "alte" Information
    			medium.ausleihe_datum = neues_ausleihe_datum;
    			medium.standplatz = "n.a";
    			System.out.println("Rückgabedatum: " + neues_ausleihe_datum.plusDays(30));
    			medienListe.add(medium); // Fügt "neue" Information ein
    			mediumFound = true;
    			break;
    		}
    	}
    		
    	// Fehlermeldung falls gesuchtes Medium nicht gefunden wird
    	if(!mediumFound) {
    		System.out.println("Medium nicht gefunden");
    	}
    	
    	speichereMedienInDatei();
    }


}
