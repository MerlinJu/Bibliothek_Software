package backend;

import java.time.LocalDate;

/**
 * <p>Medium Basisklasse mit allen wichtigen Attributen von einem Medium.</p>
 */
public class Medium {
    public final String titel;
    public final String autor;
    public String standplatz;
    public final Medientyp medientyp;
    public LocalDate ausleiheDatum;
    public LocalDate rueckgabeDatum;
    public Status status;

    /**
     * <p>Konstruktor für ausgeliehenes Medium (ohne Standplatz).</p>
     * @param titel Titel des Mediums
     * @param autor Autor des Mediums
     * @param medientyp {@link Medientyp} des Mediums
     * @param ausleiheDatum Ausleihdatum des Mediums
     * @param rueckgabeDatum Rückgabedatum des Mediums
     * @param status {@link Status}, ob Medium verfügbar, ausgeliehen oder vorgemerkt zum Ausmustern
     */
    public Medium(String titel, String autor, Medientyp medientyp, LocalDate ausleiheDatum, LocalDate rueckgabeDatum, Status status) {
        this.titel = titel;
        this.autor = autor;
        this.standplatz = null;
        this.medientyp = medientyp;
        this.ausleiheDatum = ausleiheDatum;
        this.rueckgabeDatum = rueckgabeDatum;
        this.status = status;
    }

    /**
     * <p>Konstruktor für nicht ausgeliehenes Medium (ohne Ausleih- und Rückgabedatum).</p>
     * @param titel Titel des Mediums
     * @param autor Autor des Mediums
     * @param standplatz Standplatz des Mediums
     * @param medientyp {@link Medientyp} des Mediums
     * @param status {@link Status}, ob Medium verfügbar, ausgeliehen oder vorgemerkt zum Ausmustern
     */
    public Medium(String titel, String autor, String standplatz, Medientyp medientyp, Status status){
        this.titel = titel;
        this.autor = autor;
        this.standplatz = standplatz;
        this.medientyp = medientyp;
        this.ausleiheDatum = null;
        this.rueckgabeDatum = null;
        this.status = status;
    }

    /**
     * <p>Fügt alle vorhandenen Attribute eines {@link Medium} Objektes aneinander und fügt ein Trennzeichen ein, um
     * einen String zu erhalten, dessen Formatierung dem CSV-Format entspricht.</p>
     * @return String von Attributen eines {@link Medium} Objektes, getrennt mit Semikolon
     */
    @Override
    public String toString() {

        return  titel +
                ";" + autor +
                (standplatz != null ? ";" + standplatz : "") +
                ";" + medientyp +
                (ausleiheDatum != null ? ";" + ausleiheDatum : "") +
                (rueckgabeDatum != null ? ";" + rueckgabeDatum : "") +
                ";" + status;
    }

}
