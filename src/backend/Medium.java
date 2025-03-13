package backend;

import java.time.LocalDate; // Java time Klasse, um LocalDate zu nutzen

/**
 * <p>Medium Basisklasse mit allen wichtigen Attributen von einem Medium.</p>
 */
public class Medium {
    public String titel;
    public String autor;
    public String standplatz;
    public Medientyp medientyp;
    public LocalDate ausleihe_datum;
    public LocalDate rueckgabe_datum;
    public Status status;

    /**
     * <p>Konstruktor für nicht ausgeliehenes Medium (ohne Standplatz).</p>
     * @param titel Titel des Mediums
     * @param autor Autor des Mediums
     * @param medientyp Typ des Mediums
     * @param ausleihe_datum Ausleihedatum des Mediums
     * @param rueckgabe_datum Rückgabedatum des Mediums
     */
    public Medium(String titel, String autor, Medientyp medientyp, LocalDate ausleihe_datum, LocalDate rueckgabe_datum, Status status) {
        this.titel = titel;
        this.autor = autor;
        this.standplatz = null;
        this.medientyp = medientyp;
        this.ausleihe_datum = ausleihe_datum;
        this.rueckgabe_datum = rueckgabe_datum;
        this.status = status;
    }

    /**
     * <p>Konstruktor für ausgeliehenes Medium (ohne Ausleih- und Rückgabedatum).</p>
     * @param titel Titel des Mediums
     * @param autor Autor des Mediums
     * @param standplatz Standplatz des Mediums
     * @param medientyp Typ des Mediums
     */
    public Medium(String titel, String autor, String standplatz, Medientyp medientyp, Status status){
        this.titel = titel;
        this.autor = autor;
        this.standplatz = standplatz;
        this.medientyp = medientyp;
        this.ausleihe_datum = null;
        this.rueckgabe_datum = null;
        this.status = status;
    }

    /**
     * <p>Fügt alle vorhandenen Attribute eines {@link Medium} Objektes aneinander und fügt ein Trennzeichen ein, um
     * einen String zu erhalten, dessen Formatierung dem CSV-Format entspricht.</p>
     * @return String von Attributen eines {@link Medium} Objektes, getrennt mit ';'.
     */
    @Override
    public String toString() {

        return  titel +
                ";" + autor +
                (standplatz != null ? ";" + standplatz : "") +
                ";" + medientyp +
                (ausleihe_datum != null ? ";" + ausleihe_datum: "") +
                (rueckgabe_datum != null ? ";" + rueckgabe_datum : "") +
                ";" + status;
    }

}
