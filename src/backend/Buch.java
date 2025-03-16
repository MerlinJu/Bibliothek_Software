package backend;

import java.time.LocalDate;

/**
 * <p>Buch-Klasse als Boilerplate für spezifische Eigenschaften, die nur {@link Medium}-Objekte mit dem {@link Medientyp}
 * {@code BUCH} betreffen sollen.</p>
 */
public class Buch extends Medium {

    /**
     * <p>Konstruktor für ausgeliehenes Buch (ohne Standplatz).</p>
     * @param titel Titel des Buches
     * @param autor Autor des Buches
     * @param ausleiheDatum Ausleihdatum des Buches
     * @param rueckgabeDatum Rückgabedatum des Buches
     * @param status {@link Status}, ob Buch verfügbar, ausgeliehen oder vorgemerkt zum Ausmustern ist
     */
    public Buch(String titel, String autor, LocalDate ausleiheDatum, LocalDate rueckgabeDatum, Status status) {
        super(titel, autor, Medientyp.BUCH, ausleiheDatum, rueckgabeDatum, status);
    }

    /**
     * <p>Konstruktor für nicht ausgeliehenes Buch (ohne Ausleih- und Rückgabedatum).</p>
     * @param titel Titel des Buches
     * @param autor Autor des Buches
     * @param standplatz Standplatz des Buches
     * @param status {@link Status}, ob Buch verfügbar, ausgeliehen oder vorgemerkt zum Ausmustern ist
     */
    public Buch(String titel, String autor, String standplatz, Status status) {
        super(titel, autor, standplatz, Medientyp.BUCH, status);
    }
}