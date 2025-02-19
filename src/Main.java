public class Main {
    public static void main(String[] args) {
        System.out.println("Programm gestartet!");

        Buch buch = new Buch("Harry Potter", "J.K. Rowling", "2-h");

        System.out.println("Buch: " + buch.titel + " (" + buch.medientyp + ")");
    }
}