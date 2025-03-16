package backend;

import java.time.LocalDate;

/**
 * <p>Medium Basisklasse mit allen wichtigen Attributen von einem Medium.</p>
 * <p>Ein Medium-Objekt kann entweder 5 oder 6 Attribute erhalten:</p>
 * <li>Für ausgeliehene Medien: {@link #Medium(String, String, Medientyp, LocalDate, LocalDate, Status)}</li>
 * <li>Für Medien im Bestand: {@link #Medium(String, String, String, Medientyp, Status)}</li>
 */
public class Medium {

    private final String titel;
    private final String autor;
    private String standplatz;
    private final Medientyp medientyp;
    private LocalDate ausleiheDatum;
    private LocalDate rueckgabeDatum;
    private Status status;

    /**
     * <p>Konstruktor für ausgeliehenes Medium (ohne Standplatz).</p>
     * @param titel Titel des Mediums
     * @param autor Autor des Mediums
     * @param medientyp {@link Medientyp} des Mediums
     * @param ausleiheDatum Ausleihdatum des Mediums
     * @param rueckgabeDatum Rückgabedatum des Mediums
     * @param status {@link Status}, ob Medium verfügbar, ausgeliehen oder vorgemerkt zum Ausmustern ist
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
     * @param status {@link Status}, ob Medium verfügbar, ausgeliehen oder vorgemerkt zum Ausmustern ist
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
     * <p>Rückgabe von {@code titel}.</p>
     * @return Aktueller Wert von {@code titel}
     */
    public String getTitel() {
        return titel;
    }

    /**
     * <p>Rückgabe von {@code standplatz}.</p>
     * @return Aktueller Wert von {@code standplatz}
     */
    public String getStandplatz() {
        return standplatz;
    }

    /**
     * <p>Setzt {@code standplatz}.</p>
     * @param standplatz Neuer {@code standplatz} Wert
     */
    public void setStandplatz(String standplatz) {
        this.standplatz = standplatz;
    }

    /**
     * <p>Rückgabe von {@code medientyp}.</p>
     * @return Aktueller Wert von {@code medientyp}
     */
    public Medientyp getMedientyp() {
        return medientyp;
    }

    /**
     * <p>Rückgabe von {@code ausleiheDatum}.</p>
     * @return Aktueller Wert von {@code ausleiheDatum}
     */
    public LocalDate getAusleiheDatum() {
        return ausleiheDatum;
    }

    /**
     * <p>Setzt {@code ausleiheDatum}.</p>
     * @param ausleiheDatum Neuer {@code ausleiheDatum} Wert
     */
    public void setAusleiheDatum(LocalDate ausleiheDatum) {
        this.ausleiheDatum = ausleiheDatum;
    }

    /**
     * <p>Rückgabe von {@code rueckgabeDatum}.</p>
     * @return Aktueller Wert von {@code rueckgabeDatum}
     */
    public LocalDate getRueckgabeDatum() {
        return rueckgabeDatum;
    }

    /**
     * <p>Setzt {@code rueckgabeDatum}.</p>
     * @param rueckgabeDatum Neuer {@code rueckgabeDatum} Wert
     */
    public void setRueckgabeDatum(LocalDate rueckgabeDatum) {
        this.rueckgabeDatum = rueckgabeDatum;
    }

    /**
     * <p>Rückgabe von {@code status}.</p>
     * @return Aktueller Wert von {@code status}
     */
    public Status getStatus() {
        return status;
    }

    /**
     * <p>Setzt {@code status}.</p>
     * @param status Neuer {@code status} Wert
     */
    public void setStatus(Status status) {
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
