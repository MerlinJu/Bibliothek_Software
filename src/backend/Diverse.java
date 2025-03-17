package backend;

import java.time.LocalDate;

/**
 * <p>Diverse-Klasse als Boilerplate für spezifische Eigenschaften, die nur {@link Medium}-Objekte mit dem {@link Medientyp}
 * {@code DIVERSE} betreffen sollen.</p>
 */
public class Diverse extends Medium {

    /**
     * <p>Konstruktor für ausgeliehene diverse Medien (ohne Standplatz).</p>
     * @param titel Titel des diversen Mediums
     * @param autor Autor des diversen Mediums
     * @param ausleiheDatum Ausleihdatum des diversen Mediums
     * @param rueckgabeDatum Rückgabedatum des diversen Mediums
     * @param status {@link Status}, ob diverses Medium verfügbar, ausgeliehen oder vorgemerkt zum Ausmustern ist
     */
    public Diverse(String titel, String autor, LocalDate ausleiheDatum, LocalDate rueckgabeDatum, Status status) {
        super(titel, autor, Medientyp.DIVERSE, ausleiheDatum, rueckgabeDatum, status);
    }

    /**
     * <p>Konstruktor für nicht ausgeliehenes diverses Medium (ohne Ausleih- und Rückgabedatum).</p>
     * @param titel Titel des diversen Mediums
     * @param autor Autor des diversen Mediums
     * @param standplatz Standplatz des diversen Mediums
     * @param status {@link Status}, ob diverses Medium verfügbar, ausgeliehen oder vorgemerkt zum Ausmustern ist
     */
    public Diverse(String titel, String autor, String standplatz, Status status) {
        super(titel, autor, standplatz, Medientyp.DIVERSE, status);
    }
}
