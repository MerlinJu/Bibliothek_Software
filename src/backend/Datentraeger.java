package backend;

import java.time.LocalDate;

/**
 * <p>Datentraeger-Klasse als Boilerplate für spezifische Eigenschaften, die nur {@link Medium}-Objekte mit dem {@link Medientyp}
 * {@code DATENTRAEGER} betreffen sollen.</p>
 */
public class Datentraeger extends Medium {

    /**
     * <p>Konstruktor für ausgeliehenen Datenträger (ohne Standplatz).</p>
     * @param titel Titel des Datenträgers
     * @param autor Autor des Datenträgers
     * @param ausleiheDatum Ausleihdatum des Datenträgers
     * @param rueckgabeDatum Rückgabedatum des Datenträgers
     * @param status {@link Status}, ob Datenträger verfügbar, ausgeliehen oder vorgemerkt zum Ausmustern ist
     */
    public Datentraeger(String titel, String autor, LocalDate ausleiheDatum, LocalDate rueckgabeDatum, Status status) {
        super(titel, autor, Medientyp.DATENTRAEGER, ausleiheDatum, rueckgabeDatum, status);
    }

    /**
     * <p>Konstruktor für nicht ausgeliehenen Datenträger (ohne Ausleih- und Rückgabedatum).</p>
     * @param titel Titel des Datenträgers
     * @param autor Autor des Datenträgers
     * @param standplatz Standplatz des Datenträgers
     * @param status {@link Status}, ob Datenträger verfügbar, ausgeliehen oder vorgemerkt zum Ausmustern ist
     */
    public Datentraeger(String titel, String autor, String standplatz, Status status) {
        super(titel, autor, standplatz, Medientyp.DATENTRAEGER, status);
    }
}

