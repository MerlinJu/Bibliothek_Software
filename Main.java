
import java.time.LocalDate;

public class Main extends Bibliothek {
	
    public static void main(String[] args) {
        //System.out.println("Programm gestartet!");

        //Buch buch = new Buch("Harry Potter", "J.K. Rowling", "2-h", LocalDate.parse("2025-12-02"),
        		//LocalDate.parse("2025-12-02").plusDays(30));

        //System.out.println("Buch: " + buch.titel + " (" + buch.medientyp + ")");
   
        mediumAusleihen();
        System.out.println(ladeMedienAusDatei());
    }
}