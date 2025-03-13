package backend;

/**
 * <p>Drei verschiedene Status für ein Medium.</p>
 * <li>{@code VERFÜGBAR} Medium im Bestand</li>
 * <li>{@code AUSGELIEHEN} Medium ausgeliehen</li>
 * <li>{@code AUSGELIEHEN_VORGEMERKT} Medium ist ausgeliehen und wird nach Rückgabe ausgemustert</li>
 */
public enum Status {
    VERFÜGBAR, AUSGELIEHEN, AUSGELIEHEN_VORGEMERKT
}
